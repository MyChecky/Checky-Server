package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    //新建打卡
    @RequestMapping("/addTask")
    public String addTask(@RequestBody String jsonstr){
        Task task= JSON.parseObject(jsonstr,new TypeReference<Task>(){});
        task.setTaskId(UUID.randomUUID().toString());
        int result=taskService.addTask(task);
        if(result==0){
            //添加失败
            return "addTaskFail";
        }else {
            //添加成功
            return "addTaskSuccess";
        }

    }

    //查询属于某个用户的tasks
    @RequestMapping("/queryUserTasks")
    public List<Task> queryUserTasks(@RequestBody String jsonstr){
        String userid= (String) JSON.parse(jsonstr);
        return taskService.queryUserTasks(userid,null);
    }


    //查询属于某个用户的tasks
    @RequestMapping("/queryDayUserTasks")
    public List<Task> queryDayUserTasks(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String date= (String) object.get("taskId");
        String userid= (String) object.get("userid");
        return taskService.queryUserTasks(userid,date);
    }

    //列举出所有的Tasks
    @RequestMapping("/listTasks")
    public List<Task> listTasks(){
        return taskService.listTasks();
    }

    //查询某个task
    @RequestMapping("/queryTask")
    public Task queryTask(@RequestBody String jsonstr){
        String taskid= (String) JSON.parse(jsonstr);
        Task res=taskService.queryTask(taskid);
        if(res==null){
            return null;
        }else {
            return res;
        }
    }

    //Task结束，结算状态
    @RequestMapping("/finishTask")
    public String finishTask(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String taskId= (String) object.get("taskId");
        Task task=taskService.queryTask(taskId);
        if(task!=null) {
            String state = (String) object.get("state");
            task.setTaskState(state);
            taskService.updataTask(task);
            return "updateTaskStateSuccess";
        }else {
            return "updateTaskStateFail";
        }

    }

    @RequestMapping("/getDistribute")
    public HashMap<String, Double> getDistribute(@RequestBody String body){
        JSONObject object= (JSONObject) JSON.parse(body);
        String taskId= (String) object.get("taskId");

        HashMap<String, Double> distribute = taskService.getDistribute(taskId);

        return distribute;
    }
}
