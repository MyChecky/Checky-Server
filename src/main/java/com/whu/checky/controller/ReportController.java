package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Report;
import com.whu.checky.domain.User;
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

    @RequestMapping("/addReport")
    public JSONObject addReport(@RequestBody String jsonstr) {
        Report report = JSON.parseObject(jsonstr, new TypeReference<Report>() {
        });
        report.setReportId(UUID.randomUUID().toString());
        report.setReportTime(MyConstants.DATETIME_FORMAT.format(new Date()));

        JSONObject ret = new JSONObject();
        String state = reportService.addReport(report)==1?MyConstants.RESULT_OK:MyConstants.RESULT_FAIL;
        if(report.getReportType().equals(MyConstants.REPORT_TYPE_SUPERVISOR) && state.equals(MyConstants.RESULT_OK)){
            User user = userService.queryUser(report.getSupervisorId());
            user.setReportedTotal(user.getReportTotal()+1);
            userService.updateUser(user);
        }
        ret.put("state", state);
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
