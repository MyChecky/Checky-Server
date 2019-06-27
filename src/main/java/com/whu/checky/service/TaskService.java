package com.whu.checky.service;

import com.whu.checky.domain.Task;

public interface TaskService {
    //用户新建打卡
    Integer addTask(Task task);
    Integer delTask(String taskid);
    Integer updataTask(Task task);
//    //打卡日历
//    void getTasks();
//    //打卡清单和历史记录
//    void getTaskList();
}
