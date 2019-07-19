package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Report;
import com.whu.checky.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping("/addReport")
    public void addReport(@RequestBody String jsonstr){
        Report report= JSON.parseObject(jsonstr,new TypeReference<Report>(){});
        report.setReportId(UUID.randomUUID().toString());
        int result=reportService.addReport(report);
        if(result==1){
            //插入成功
        }else {
            //插入失败
        }

    }

    @RequestMapping("/queryUserReports")
    public List<Report> queryUserReports(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String userId= (String)object.get("userId");
        List<Report> reports=reportService.queryUserReports(userId);
        return reports;
    }


    @RequestMapping("/dealReport")
    public void dealReport(@RequestBody String jsonstr){
        Report report= JSON.parseObject(jsonstr,new TypeReference<Report>(){});
        int result=reportService.dealReport(report);
        if(result==1){
            //处理成功
        }else {
            //处理失败
        }
    }

    @RequestMapping("/deleteReport")
    public void deleteReport(@RequestBody String jsonstr){
        String reportId= (String) JSON.parse(jsonstr);
        int result=reportService.deleteReport(reportId);
        if(result==1){
            //删除成功
        }else {
            //删除失败
        }
    }
}
