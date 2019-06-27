package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Suggestion;
import com.whu.checky.domain.TaskType;
import com.whu.checky.service.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/TaskType")
public class TaskTypeController {
    //管理端用
    @Autowired
    private TaskTypeService taskTypeService;

    //管理端用于新添类型的
    @PostMapping("/addType")
    public void addType(@RequestBody String jsonstr){
        TaskType taskType= JSON.parseObject(jsonstr,new TypeReference<TaskType>(){});
        int result=taskTypeService.addTaskType(taskType);
        if(result==0){
            //添加失败

        }else {
            //添加成功

        }

    }

    @PostMapping("/delType")
    public void delType(@RequestBody String jsonstr) {
        String taskTypeid=(String)JSON.parse(jsonstr);
        int result=taskTypeService.DeleteTaskType(taskTypeid);
        if (result==0){
            //删除失败
        }else {
            //删除成功
        }


    }

}
