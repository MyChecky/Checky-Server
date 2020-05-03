package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.*;
import com.whu.checky.service.*;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/report")
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

    @RequestMapping("/addReport")
    public JSONObject addReport(@RequestBody String jsonstr) {
        Report report = JSON.parseObject(jsonstr, new TypeReference<Report>() {
        });
        report.setReportId(UUID.randomUUID().toString());
        report.setReportTime(MyConstants.DATETIME_FORMAT.format(new Date()));
        int stateFlag = 0;
        int rightFlag = 3;

        // 确认被举报者userId
        String reportedUserId = "";
        switch (report.getReportType()) {
            case MyConstants.REPORT_TYPE_ESSAY: // essay
                reportedUserId = essayService.queryEssayById(report.getEssayId()).getUserId();
                break;
            case MyConstants.REPORT_TYPE_CHECK: // check
                reportedUserId = checkService.queryCheckById(report.getCheckId()).getUserId();
                break;
            case MyConstants.REPORT_TYPE_TASK:  // task
                reportedUserId = taskService.queryTask(report.getTaskId()).getUserId();
                break;
            case MyConstants.REPORT_TYPE_SUPERVISOR:    // supervisor
                // 此时必须同时存入taskId，否则仅靠举报者id与被举报监督id无法定位到哪个任务
                reportedUserId = report.getSupervisorId();
                rightFlag += 1;
                // task_supervisor表 REPORT_NUM+=1
                List<TaskSupervisor> taskSupervisors = taskSupervisorService.getTasksSupByTaskId(report.getTaskId());
                for (TaskSupervisor taskSupervisor : taskSupervisors) {
                    if (taskSupervisor.getSupervisorId().equals(report.getSupervisorId())) {
                        taskSupervisor.setReportNum(taskSupervisor.getReportNum() + 1);
                        stateFlag += taskSupervisorService.updateTaskSup(taskSupervisor) == 1 ? 1 : 0;
                    }
                }
//                // 判断任务是否为公示状态
//                Task task = taskService.queryTask(report.getTaskId());
//                if (task.getTaskState().equals(MyConstants.TASK_STATE_SUCCESS) ||
//                        task.getTaskState().equals(MyConstants.TASK_STATE_FAIL)) {
//
//                    task.setTaskState(MyConstants.TASK_STATE_APPEAL);
//                    task.setRefundMoney(0.0);
//                    task.setSystemBenifit(0.0);
//                    taskService.updateTask(task);
//
//                    List<TaskSupervisor> taskSupsToInit = taskSupervisorService.getTasksSupByTaskId(report.getTaskId());
//                    for (TaskSupervisor taskSupervisor: taskSupsToInit){
//                        taskSupervisor.setBenefit(0.0);
//                        taskSupervisorService.updateTaskSup(taskSupervisor);
//                    }
//                }
                break;
        }

        report.setUserReportedId(reportedUserId);

        JSONObject ret = new JSONObject();

        stateFlag += reportService.addReport(report) == 1 ? 1 : 0;

        // user 被举报者 reportedTotal += 1
        User reportedUser = userService.queryUser(reportedUserId);
        reportedUser.setReportedTotal(reportedUser.getReportedTotal() + 1);
        stateFlag += userService.updateUser(reportedUser) == 1 ? 1 : 0;

        // user 举报者 reportTotal += 1
        User user = userService.queryUser(report.getUserId());
        user.setReportTotal(user.getReportTotal() + 1);
        stateFlag += userService.updateUser(user) == 1 ? 1 : 0;

        ret.put("state", stateFlag == rightFlag ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL);
        return ret;

    }

    @RequestMapping("/queryUserReports")
    public List<Report> queryUserReports(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        List<Report> reports = reportService.queryUserReports(userId);
        return reports;
    }


    @RequestMapping("/dealReport")
    public JSONObject dealReport(@RequestBody String jsonstr) {
        Report report = JSON.parseObject(jsonstr, new TypeReference<Report>() {
        });
        int result = reportService.dealReport(report);
        JSONObject object = new JSONObject();
        if (result == 1) {
            //插入成功
            object.put("state", MyConstants.RESULT_OK);
        } else {
            //插入失败
            object.put("state", MyConstants.RESULT_FAIL);
        }
        return object;

    }

    @RequestMapping("/deleteReport")
    public JSONObject deleteReport(@RequestBody String jsonstr) {
        String reportId = (String) JSON.parse(jsonstr);
        int result = reportService.deleteReport(reportId);
        JSONObject object = new JSONObject();
        if (result == 1) {
            //插入成功
            object.put("state", MyConstants.RESULT_OK);
        } else {
            //插入失败
            object.put("state", MyConstants.RESULT_FAIL);
        }
        return object;

    }
}
