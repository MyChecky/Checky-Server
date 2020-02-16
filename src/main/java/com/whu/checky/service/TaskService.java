package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskService {
    //用户新建打卡
    Integer addTask(Task task);
    Integer delTask(String taskId);
    Integer updateTask(Task task);
    String getTitleById(String taskId);
    List<Task> queryUserTasks(String userId,String date);
    List<Task> listTasks();
    Task queryTask(String taskId);
    HashMap<String,Double> distribute(String taskId);
//    //打卡日历
//    void getTasks();
//    //打卡清单和历史记录
//    void getTaskList();
List<Task> query(HashMap<String, String> params, Page page);

    int getTasksNum(HashMap<String, String> params);
    List<Task> queryTaskByUserName(String username);
}
