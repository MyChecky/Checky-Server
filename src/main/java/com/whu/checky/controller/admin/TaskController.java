package com.whu.checky.controller.admin;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.TaskSupervisorMapper;
import com.whu.checky.service.CheckService;
import com.whu.checky.service.TaskService;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin/task")
@Component("AdminTaskController")
//public class TaskController {
//
//    @Autowired
//    TaskService taskService;
//
//    @Autowired
//    UserService userService;
//
//    @PostMapping("/tasks")
//    public HashMap<String, Object> all(@RequestBody String body) {
//        JSONObject json = JSONObject.parseObject(body);
//        int page = json.getInteger("page");
//        Page<Task> p = null;
//        if (page != -1) {
//            p = new Page<>(page, 10);
//        }
//        HashMap<String, Object> resp = new HashMap<>();
//        HashMap<String, String> params = new HashMap<>();
//        if (json.containsKey("userId")) {
//            params.put("user_id", json.getString("userId"));
//            resp.put("type", "userId");
//        }
//        List<Task> taskList = taskService.query(params, p);
//        resp.put("tasks", taskList);
//        if (p != null) resp.put("tasksSize", p.getTotal());
//        resp.put("state", "ok");
//        return resp;
//    }
//
//}

public class TaskController {

    @Autowired
    TaskService taskService;
    @Autowired
    CheckService checkService;

    @Autowired
    UserService userService;
    @Autowired
    TaskSupervisorMapper taskSupervisorMapper;

//    @PostMapping("/all")
//    public JSONObject all(@RequestBody String body) {
//
//    }
        @PostMapping("/tasks")
    public HashMap<String, Object> all(@RequestBody String body) {
        JSONObject json = JSONObject.parseObject(body);
        int page = json.getInteger("page");
        Page<Task> p = null;
        if (page != -1) {
            p = new Page<>(page, 10);
        }
        HashMap<String, Object> resp = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        if (json.containsKey("userId")) {
            params.put("user_id", json.getString("userId"));
            resp.put("type", "userId");
        }
        List<Task> taskList = taskService.query(params, p);
        resp.put("tasks", taskList);
        if (p != null) resp.put("tasksSize", p.getTotal());
        resp.put("state", "ok");
        return resp;
    }

    //查看任務詳情
    @PostMapping("/detail")
    public JSONObject detail(@RequestBody String body) {
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(body);
        String taskId=(String)object.get("taskId");
        Task task=taskService.queryTask(taskId);
        res.put("state","ok");
        res.put("task",task);
        return res;
    }

    //查看打卡詳情
    @PostMapping("/check")
    public JSONObject check(@RequestBody String body) {
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(body);
        String taskId=(String)object.get("taskId");
        List<Check> checks=checkService.getTaskChecks(taskId);
        res.put("state","ok");
        res.put("checks",checks);
        return res;
    }

    //查看监督者
    @PostMapping("/supervisors")
    public JSONObject supervisors(@RequestBody String body) {
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(body);
        String taskId=(String)object.get("taskId");
        List<String> supervisorsId= taskSupervisorMapper.getTaskSupervisors(taskId);
        List<AdminTaskSupervisor> adminTaskSupervisors=new ArrayList<AdminTaskSupervisor>();
        for(String supervisorId:supervisorsId){
            AdminTaskSupervisor adminTaskSupervisor=new AdminTaskSupervisor();
            User user=userService.queryUser(supervisorId);
            TaskSupervisor taskSupervisor=taskSupervisorMapper.getTaskSupervisor(taskId,supervisorId);
            adminTaskSupervisor.setAddTime(taskSupervisor.getAddTime());
            adminTaskSupervisor.setBenefit(taskSupervisor.getBenefit());
            adminTaskSupervisor.setSuperviseNum(taskSupervisor.getSuperviseNum());
            adminTaskSupervisor.setUserId(supervisorId);
            adminTaskSupervisor.setUserName(user.getUserName());
            adminTaskSupervisors.add(adminTaskSupervisor);
        }
        res.put("state","ok");
        res.put("supervisors",adminTaskSupervisors);
        return res;
    }

    class AdminTaskSupervisor{
        private String addTime;
        private double benefit;
        private int superviseNum;
        private String userId;
        private String userName;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public double getBenefit() {
            return benefit;
        }

        public void setBenefit(double benefit) {
            this.benefit = benefit;
        }

        public int getSuperviseNum() {
            return superviseNum;
        }

        public void setSuperviseNum(int superviseNum) {
            this.superviseNum = superviseNum;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }


}
