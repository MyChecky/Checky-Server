package com.whu.checky.service;

import com.whu.checky.domain.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskService {
    //用户新建打卡
    Integer addTask(Task task);
    Integer delTask(String taskid);
    Integer updataTask(Task task);
    String getTitleById(String taskId);
    List<Task> queryUserTasks(String userid,String date);
    List<Task> listTasks();
    Task queryTask(String taskid);
    HashMap<String,Double> distribute(String taskid);
//    //打卡日历
//    void getTasks();
//    //打卡清单和历史记录
//    void getTaskList();
List<Task> query(HashMap<String, String> params, int page);

    int getTasksNum(HashMap<String, String> params);
}
