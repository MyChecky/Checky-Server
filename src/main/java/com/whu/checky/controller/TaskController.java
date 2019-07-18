package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.service.MoneyService;
import com.whu.checky.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private MoneyService moneyService;

    private SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");

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
            //微信支付相关
            //在Money表当中插入一条用户交付押金的记录
            MoneyFlow moneyFlow=new MoneyFlow();
            moneyFlow.setToUserId("System");
            moneyFlow.setFromUserId(task.getUserId());
            moneyFlow.setFlowMoney(task.getTaskMoney());
            moneyFlow.setTaskId(task.getTaskId());
            moneyFlow.setFlowTime(ft.format(new Date()));
            moneyFlow.setFlowId(UUID.randomUUID().toString());
            int addMoneyRes=moneyService.addMoneyRecord(moneyFlow);
            if(addMoneyRes==1) {
                return "addTaskSuccess";
            }else{
                return "insertMoneyFlowError";
            }
        }
    }

    //查询属于某个用户的tasks
    @RequestMapping("/queryUserTasks")
    public List<Task> queryUserTasks(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String userId= (String) object.get("userId");
        return taskService.queryUserTasks(userId,null);
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
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String taskId= (String)object.get("taskId");
        Task res=taskService.queryTask(taskId);
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

        return taskService.distribute(taskId);
    }
}
