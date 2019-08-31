package com.whu.checky.service;

import com.whu.checky.domain.Suggestion;
import com.whu.checky.domain.TaskType;

import java.util.List;

public interface TaskTypeService {
    //新加一条打卡建议
    Integer addTaskType(TaskType taskType);
    //对一条打卡建议进行处理
    Integer updataTaskType(TaskType taskType);
    //对打卡建议进行查询
//    Suggestion QuerySuggestion(String suggestionId);
    //查询所有的打卡类型
    List<TaskType> ListAllTaskType();
    //使用ID查询某条TaskType
    TaskType QueryTaskType(String typeId);
    //删除打卡类型
    Integer DeleteTaskType(String typeId);
    //
}
