package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.Check;
import com.whu.checky.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/check")
public class CheckController {
    @Autowired
    private CheckService checkService;

    @PostMapping("/addCheck")
    public String addCheck(@RequestBody String body){
        JSONObject object = JSONObject.parseObject(body);
        Check check = paserJson2NewCheck(object);

        try{
            checkService.addCheck(check);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @PostMapping("/queryCheck")
    public Check queryCheck(@RequestBody String body){
        String checkId = (String) JSON.parse(body);
        List<Check> list = checkService.queryCheck("check_id", checkId);
        if(list.size()==1)
            return list.get(0);
        else
            return null;
    }

    @PostMapping("/Checky/check/listCheck")
    public List<Check> listCheck(@RequestBody String body){
        String userId = (String) JSON.parse(body);
        return checkService.queryCheck("user_id", userId);
    }

    @PostMapping("/Checky/check/updateCheck")
    public String updateCheck(@RequestBody String body){
        JSONObject object = JSONObject.parseObject(body);
        Check check = paserJasonUpdateUser(object);

        try{
            checkService.updateCheck(check);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "fail";
        }

        return "success";
    }

    @PostMapping("/Checky/check/deleteCheck")
    public String deleteCheck(@RequestBody String body){
        String checkId = (String) JSON.parse(body);

        try{
            checkService.deleteCheck(checkId);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "fail";
        }

        return "success";
    }

    private Check paserJson2NewCheck(JSONObject object){
        //解析json获取登录信息
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

    private Check paserJasonUpdateUser(JSONObject object){
        Check check = paserJson2NewCheck(object);
        check.setCheckState(object.getString("checkState"));
        check.setCheckTime(object.getString("checkTime"));
        check.setPassNum(object.getInteger("passNum"));
        check.setSuperviseNum(object.getInteger("superviseNum"));

        return check;
    }
}
