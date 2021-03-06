package com.whu.checky.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.User;
import com.whu.checky.service.TaskService;
import com.whu.checky.service.TaskTypeService;
import com.whu.checky.service.UserService;
import com.whu.checky.util.MyConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminTests {
    @Autowired
    UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskTypeService taskTypeService;

    @Test
    public void getAllUsers(){
        int page = 1;
        int pageSize = 10;
        boolean isAsc = false;
        Page<User> p = new Page<User>(page, pageSize);
        List<User> userList = userService.getAllUsers(p, isAsc);
        HashMap<String,Object> resp = new HashMap<>();
        resp.put("state", MyConstants.RESULT_OK);
        resp.put("size",(int)Math.floor(userService.getAllUsersNum()/ (double)pageSize) );
        resp.put("users",userList);
        System.out.println(resp.toString());
    }

    @Test
    public void getTasks(){
        int page = 1;
        Page<Task> p = null;
        if (page != -1) {
            p = new Page<>(page, 10);
        }
        HashMap<String, Object> resp = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
//        if (json.containsKey("userId")) {
//            params.put("user_id", json.getString("userId"));
//            resp.put("type", "userId");
//        }
        List<Task> taskList = taskService.query(params, p);
        for(Task task:taskList){
            task.setTypeContent(taskTypeService.queryTaskType(task.getTypeId()).getTypeContent());
        }
        resp.put("tasks", taskList);
        if (p != null) resp.put("tasksSize", p.getTotal());
        resp.put("state", MyConstants.RESULT_OK);
        System.out.println(resp);
    }
}
