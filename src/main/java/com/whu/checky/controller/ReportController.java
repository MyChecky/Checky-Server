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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    private SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("/addReport")
    public JSONObject addReport(@RequestBody String jsonstr){
        Report report= JSON.parseObject(jsonstr,new TypeReference<Report>(){});
        report.setReportId(UUID.randomUUID().toString());
        report.setReportTime(dateFormat.format(new Date()));
        int result=reportService.addReport(report);
        JSONObject object=new JSONObject();
        if(result==1){
            //插入成功
            object.put("state","OK");
        }else {
            //插入失败
            object.put("state","FAIL");
        }
        return object;

    }

    @RequestMapping("/queryUserReports")
    public List<Report> queryUserReports(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String userId= (String)object.get("userId");
        List<Report> reports=reportService.queryUserReports(userId);
        return reports;
    }


    @RequestMapping("/dealReport")
    public JSONObject dealReport(@RequestBody String jsonstr){
        Report report= JSON.parseObject(jsonstr,new TypeReference<Report>(){});
        int result=reportService.dealReport(report);
        JSONObject object=new JSONObject();
        if(result==1){
            //插入成功
            object.put("state","OK");
        }else {
            //插入失败
            object.put("state","FAIL");
        }
        return object;

    }

    @RequestMapping("/deleteReport")
    public JSONObject deleteReport(@RequestBody String jsonstr){
        String reportId= (String) JSON.parse(jsonstr);
        int result=reportService.deleteReport(reportId);
        JSONObject object=new JSONObject();
        if(result==1){
            //插入成功
            object.put("state","OK");
        }else {
            //插入失败
            object.put("state","FAIL");
        }
        return object;

    }
}
