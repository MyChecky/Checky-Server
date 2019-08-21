package com.whu.checky.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.Task;
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
@RequestMapping("/admin")
@Component("AdminTaskController")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @PostMapping("/tasks")
    public HashMap<String, Object> all(@RequestBody String body) {
        JSONObject json = JSONObject.parseObject(body);
        int page = json.getInteger("page");
        HashMap<String, Object> resp = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        if (json.containsKey("userId")) {
            params.put("user_id", json.getString("userId"));
            resp.put("type", "userId");
        }
        List<Task> taskList = taskService.query(params, page);
        HashMap<String, String> userList = new HashMap<>();
        List<Object> list = new ArrayList<>();
        for (Task t : taskList) {
            userList.put(t.getUserId(), userService.queryUser(t.getUserId()).getUserName());

        }
        resp.put("tasks", taskList);
        resp.put("userNames", userList);
        resp.put("tasksSize", taskService.getTasksNum(params));
        resp.put("state", "ok");
        return resp;
    }

}
