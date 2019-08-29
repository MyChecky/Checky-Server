package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.TaskType;
import com.whu.checky.service.AdministratorService;
import com.whu.checky.service.RedisService;
import com.whu.checky.service.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/taskType")
public class TaskTypeController {
    @Autowired
    private TaskTypeService taskTypeService;


    //查看打卡詳情
    @PostMapping("/update")
    public JSONObject update(@RequestBody String body) {
        JSONObject res=new JSONObject();
        TaskType taskType= JSON.parseObject(body,new TypeReference<TaskType>(){});
        if(taskTypeService.updataTaskType(taskType)==1) {
            res.put("state","ok");
        }else {
            res.put("state","fail");
        }
        return res;
    }

    //查看打卡詳情
    @PostMapping("/delete")
    public JSONObject delete(@RequestBody String body) {
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(body);
        String typeId=(String)object.get("typeId");
        if(taskTypeService.DeleteTaskType(typeId)==1) {
            res.put("state","ok");
        }else {
            res.put("state","fail");
        }
        return res;
    }


}

