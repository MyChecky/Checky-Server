package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Task;
import com.whu.checky.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/Task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    //新建打卡
    @RequestMapping("add")
    public void addTask(@RequestBody String jsonstr){
        Task task= JSON.parseObject(jsonstr,new TypeReference<Task>(){});
        int result=taskService.addTask(task);
        if(result==0){
            //添加失败

        }else {
            //添加成功

        }

    }

    void queryTasks(){

    }

    void finishTask(){}

}
