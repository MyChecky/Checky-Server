package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Check;
import com.whu.checky.service.AdministratorService;
import com.whu.checky.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class CheckController {
    @Autowired
    private CheckService checkService;

    @PostMapping("/Checky/check/addCheck")
    public String addCheck(@RequestBody String body){

        Check check = paserJson2User(body);

        try{
            checkService.addCheck(check);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @PostMapping("/Checky/check/queryCheck")
    public Check queryCheck(@RequestBody String body){
        String checkId= (String) JSON.parse(body);
        List<Check> list = checkService.queryCheck(checkId);
        if(list.size()==1){
            return list.get(0);
        }
        else
            return null;
    }

    void updateCheck(){

    }

    void deleteCheck(){}

    private Check paserJson2User(String body){
        //解析json获取登录信息
        JSONObject object = JSONObject.parseObject(body);
        String userId = object.getString("userId");
        String taskId = object.getString("taskId");
        String checkTime = object.getString("checkTime");

        //构造登录用户
        Check check = new Check();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        check.setCheckId(uuid);
        check.setUserId(userId);
        check.setTaskId(taskId);
        check.setCheckTime(checkTime);

        return check;
    }
}
