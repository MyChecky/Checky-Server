package com.whu.checky.service;

public interface TaskTypeService {
    //新加一条打卡建议
    void addTaskType();
    //对一条打卡建议进行处理
    void updataTaskType();
    //对打卡建议进行查询
    void QuerySuggestion();
    //对打卡类型进行查询
    void QueryTaskType();
    //删除打卡类型
    void DeleteTaskType();

}
