package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Report;
import com.whu.checky.domain.User;
import com.whu.checky.service.ReportService;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/admin/report")
@Component("AdminReportController")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private UserService userService;

    //查看所有舉報
    @PostMapping("/all")
    public JSONObject all(@RequestBody String body) {
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(body);
        int currentPage = (Integer) object.get("page");
        Page<Report> page=new Page<>(currentPage,5);
        List<AdminReport> adminReports=new ArrayList<AdminReport>();
        List<Report> reports=reportService.displayReports(page);
        for (Report report:reports){
            AdminReport adminReport=new AdminReport();
            User user=userService.queryUser(report.getUserId());
            adminReport.setReportContent(report.getReportContent());
            adminReport.setReportId(report.getReportId());
            adminReport.setReportTime(report.getReportTime());
            adminReport.setReportType(report.getReportType());
            adminReport.setUserId(report.getUserId());
            adminReport.setUserName(user.getUserName());
            adminReport.setObjectId(report.getEssayId());
            adminReports.add(adminReport);
        }
        res.put("state","ok");
        res.put("reports",adminReports);
        return res;
    }


    class AdminReport{
        private String reportContent;
        private String reportId;
        private String reportTime;
        private String reportType;
        private String userId;
        private String userName;
        private String objectId;

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
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
    }

}