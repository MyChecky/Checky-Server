package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Supervise;
import com.whu.checky.service.SuperviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuperviseController {

    @Autowired
    private SuperviseService superviseService;

    //监督者进行认证的
    @RequestMapping()
    public void addSupervise(){}


    //还需等待认证的（需要设置一个有效期之类的东西）
    @RequestMapping("")
    public void queryUserDaySupervise(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String date= (String) object.get("taskId");
        String userid= (String) object.get("userid");
    }

    //查询属于某个用户的所有Supervise
    @RequestMapping("")
    public void queryUserSupervise(@RequestBody String jsonstr){
        String userid= (String) JSON.parse(jsonstr);
        superviseService.querySupervise(userid);
    }

    //查询具体的某一个Supervise
    @RequestMapping("")
    public void querySupervise(@RequestBody String jsonstr){
        String superviseId= (String) JSON.parse(jsonstr);
        superviseService.querySupervise(superviseId);
    }


    @RequestMapping("")
    public void modifySupervise(@RequestBody String jsonstr){
        Supervise supervise= JSON.parseObject(jsonstr,new TypeReference<Supervise>(){});
//        superviseService.updateSuperviseFail();
    }

    void deleteSupervise(){}
}
