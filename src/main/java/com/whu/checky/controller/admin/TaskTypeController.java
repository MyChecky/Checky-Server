package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.TaskType;
import com.whu.checky.service.TaskTypeService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/taskType")
@Component("AdminTaskTypeController")
public class TaskTypeController {
    @Autowired
    private TaskTypeService taskTypeService;


    //查看打卡詳情
    @PostMapping("/update")
    public JSONObject update(@RequestBody String body) {
        JSONObject res=new JSONObject();
        TaskType taskType= JSON.parseObject(body,new TypeReference<TaskType>(){});
        if(taskTypeService.updataTaskType(taskType)==1) {
            res.put("state", MyConstants.RESULT_OK);
        }else {
            res.put("state",MyConstants.RESULT_FAIL);
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
            res.put("state",MyConstants.RESULT_OK);
        }else {
            res.put("state",MyConstants.RESULT_FAIL);
        }
        return res;
    }


}

