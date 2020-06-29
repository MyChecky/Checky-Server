package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.*;
import com.whu.checky.service.*;
import com.whu.checky.util.Match;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/report")
@Component("AdminReportController")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private UserService userService;
    @Autowired
    private EssayService essayService;
    @Autowired
    private CheckService checkService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskSupervisorService taskSupervisorService;
    @Autowired
    private Match match;

    // 查看所有舉報
    @PostMapping("/all")
    public JSONObject all(@RequestBody String body) {
        JSONObject res = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(body);
        int currentPage = (Integer) object.get("page");
        Integer pageSize = (Integer) object.get("pageSize");
        if (pageSize == null) {
            pageSize = 5;
        }
        Page<Report> page = new Page<>(currentPage, pageSize);
        List<AdminReport> adminReports = new ArrayList<AdminReport>();
        List<Report> reports = reportService.displayReports(page);
        for (Report report : reports) {
            AdminReport adminReport = new AdminReport();
            User user = userService.queryUser(report.getUserId());

            switch (report.getReportType()) {
                case MyConstants.REPORT_TYPE_ESSAY:
                    adminReport.setObjectId(report.getEssayId());
                    break;
                case MyConstants.REPORT_TYPE_CHECK:
                    adminReport.setObjectId(report.getCheckId());
                    break;
                case MyConstants.REPORT_TYPE_TASK:
                    adminReport.setObjectId(report.getTaskId());
                    break;
                case MyConstants.REPORT_TYPE_SUPERVISOR:
                    adminReport.setObjectId(report.getSupervisorId());
                    break;
            }

            adminReport.setReportContent(report.getReportContent());
            adminReport.setReportId(report.getReportId());
            adminReport.setReportTime(report.getReportTime());
            adminReport.setReportType(report.getReportType());
            adminReport.setUserId(report.getUserId());
            adminReport.setUserName(user.getUserName());
            adminReport.setReportState(report.getProcessResult());
            adminReports.add(adminReport);
        }
        res.put("state", MyConstants.RESULT_OK);
        res.put("reports", adminReports);
        res.put("size", (int) Math.ceil(page.getTotal() / (double) pageSize));
        res.put("total", page.getTotal());
        return res;
    }

    // 处理申诉
    @PostMapping("/process")
    public JSONObject process(@RequestBody String body) {
        JSONObject res = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(body);
        int updateFlag = 0, expectFlag = 0;

        String result = object.getString("result");
        Report report = reportService.getReportById(object.getString("reportId"));
        if (result.equals(MyConstants.PROCESS_STATE_PASS)) {
            switch (report.getReportType()) {
                case MyConstants.REPORT_TYPE_ESSAY:
                    // 对文章假删除
                    expectFlag++; // 多执行一次数据库更新
                    Essay essayToDelete = essayService.queryEssayById(report.getEssayId());
                    essayToDelete.setIfDelete(1);
                    updateFlag += essayService.updateEssay(essayToDelete);
                    break;
                case MyConstants.REPORT_TYPE_CHECK:
                    // check设为deny
                    expectFlag++; // 多执行一次数据库更新
                    Check check = checkService.queryCheckById(report.getCheckId());
                    check.setCheckState(MyConstants.CHECK_STATE_DENY);
                    updateFlag += checkService.updateCheck(check);
                    break;
                case MyConstants.REPORT_TYPE_TASK:
                    // task直接失败->未分成（没收）
                    expectFlag++;
                    Task task = taskService.queryTask(report.getTaskId());
                    task.setTaskState(MyConstants.TASK_STATE_FAIL);
                    task.setTaskAnnounceTime(MyConstants.DATETIME_FORMAT.format(new Date()));
                    updateFlag += taskService.updateTask(task);
                    break;
                case MyConstants.REPORT_TYPE_SUPERVISOR:
                    // remove supervisor, add new.
                    List<TaskSupervisor> taskSupervisors = taskSupervisorService
                            .getTasksSupByTaskId(report.getTaskId());
                    for (TaskSupervisor taskSupervisor : taskSupervisors) {
                        if (taskSupervisor.getSupervisorId().equals(report.getSupervisorId())) {
                            taskSupervisor.setRemoveTime(new SimpleDateFormat().format(new Date()));
                            taskSupervisorService.updateTaskSup(taskSupervisor);

                            // add a new supervisor
                            Set<String> supervisorIds = taskSupervisorService.getTasksSupByTaskId(report.getTaskId())
                                    .stream().map(su -> su.getSupervisorId()).collect(Collectors.toSet());
                            match.matchSupervisorForOneTask(taskService.queryTask(report.getTaskId()), supervisorIds,
                                    1);

                            break;
                        }
                    }
                    break;
            }
            // 多执行两次数据库更新
            expectFlag += 2;
            // 被举报者被举报成功次数+1
            User userReported = userService.queryUser(report.getUserReportedId());
            userReported.setReportedPassed(userReported.getReportedPassed() + 1);
            updateFlag += userService.updateUser(userReported);
            // 举报者举报成功次数+1
            User userReport = userService.queryUser(report.getUserId());
            userReport.setReportPassed(userReport.getReportPassed() + 1);
            updateFlag += userService.updateUser(userReport);
        }

        // 这里获取历史所有对该对象的举报，并统一定为此次处理结果
        List<Report> reports = reportService.getSameReportsByReport(report);
        expectFlag += reports.size(); // 多执行reports.size()次数据库更新
        for (Report reportToUpdate : reports) {
            reportToUpdate.setProcessResult(result);
            reportToUpdate.setProcessTime(MyConstants.DATETIME_FORMAT.format(new Date()));
            updateFlag += reportService.updateReport(reportToUpdate);
        }

        res.put("state", updateFlag == expectFlag ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL);
        return res;
    }

    // 根据username模糊搜索的举报
    @RequestMapping("/query")
    public JSONObject query(@RequestBody String jsonstr) {
        JSONObject res = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String startTime = object.getString("startTime");
        startTime = startTime != null && !startTime.equals("") ? startTime : MyConstants.START_TIME;
        String endTime = object.getString("endTime");
        endTime = endTime != null && !endTime.equals("") ? endTime : MyConstants.END_TIME;

        String keyword = object.getString("keyword");
        String searchType = object.getString("searchType");
        Integer page = object.getInteger("page");
        Integer pageSize = object.getInteger("pageSize");
        Page<Report> p = new Page<>(page, pageSize);
        List<Report> reports = new ArrayList<>();

        if (keyword == null || keyword.equals("")) {
            reports = reportService.queryAppealsAll(p, startTime, endTime);
        } else if (searchType.equals("nickname")) {
            reports = reportService.queryAppealsLikeNickname(p, startTime, endTime, keyword);
        } else if (searchType.equals("content")) {
            reports = reportService.queryAppealsLikeContent(p, startTime, endTime, keyword);
        } else {
            res.put("state", MyConstants.RESULT_FAIL);
            return res;
        }

        List<AdminReport> adminReports = new ArrayList<AdminReport>();
        for (Report report : reports) {
            AdminReport adminReport = new AdminReport();
            User user = userService.queryUser(report.getUserId());
            String objectId = report.getCheckId() != null ? report.getCheckId()
                    : report.getEssayId() != null ? report.getEssayId()
                            : report.getSupervisorId() != null ? report.getSupervisorId()
                                    : report.getTaskId() != null ? report.getTaskId() : null;
            adminReport.setObjectId(objectId);
            adminReport.setReportContent(report.getReportContent());
            adminReport.setReportId(report.getReportId());
            adminReport.setReportTime(report.getReportTime());
            adminReport.setReportType(report.getReportType());
            adminReport.setUserId(report.getUserId());
            adminReport.setUserName(user.getUserName());
            adminReport.setObjectId(report.getEssayId());
            adminReport.setReportState(report.getProcessResult());
            adminReports.add(adminReport);
        }
        res.put("state", MyConstants.RESULT_OK);
        res.put("reports", adminReports);
        res.put("size", (int) Math.ceil(p.getTotal() / (double) pageSize));
        res.put("total", p.getTotal());
        return res;
    }

    class AdminReport {

        private String reportContent;
        private String reportId;
        private String reportTime;
        private String reportType;
        private String userId;
        private String userName;
        private String objectId;
        private String reportState;

        public String getReportState() {
            return reportState;
        }

        public void setReportState(String reportState) {
            this.reportState = reportState;
        }

        public String getReportContent() {
            return reportContent;
        }

        public void setReportContent(String reportContent) {
            this.reportContent = reportContent;
        }

        public String getReportId() {
            return reportId;
        }

        public void setReportId(String reportId) {
            this.reportId = reportId;
        }

        public String getReportTime() {
            return reportTime;
        }

        public void setReportTime(String reportTime) {
            this.reportTime = reportTime;
        }

        public String getReportType() {
            return reportType;
        }

        public void setReportType(String reportType) {
            this.reportType = reportType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }
    }

}