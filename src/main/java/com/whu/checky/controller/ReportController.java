package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
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

    @RequestMapping
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

    @RequestMapping
    public List<Report> queryUserReports(@RequestBody String jsonstr){
        String userId= (String) JSON.parse(jsonstr);
        return reportService.queryUserReports(userId);
    }

    @RequestMapping("/deal")
    public void dealReport(@RequestBody String jsonstr){
        Report report= JSON.parseObject(jsonstr,new TypeReference<Report>(){});
        int result=reportService.dealReport(report);
        if(result==1){
            //处理成功
        }else {
            //处理失败
        }
    }

    @RequestMapping("/del")
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
