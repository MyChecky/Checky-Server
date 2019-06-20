package com.whu.checky.service;

public interface TaskService {
    //用户新建打卡
    void addTask();
    //审核成功后添加打卡类型
    void addTaskType();
    //打卡日历
    void getTasks();
    //打卡清单和历史记录
    void getTaskList();
}
