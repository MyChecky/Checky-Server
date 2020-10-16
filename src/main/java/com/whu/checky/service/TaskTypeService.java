package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Suggestion;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskType;

import java.util.List;

public interface TaskTypeService {
    //新加一条打卡建议
    Integer addTaskType(TaskType taskType);
    //对一条打卡建议进行处理
    Integer updataTaskType(TaskType taskType);
    //对打卡建议进行查询
//    Suggestion QuerySuggestion(String suggestionId);
    //查询所有的打卡类型(管理端使用，需要分页)
    List<TaskType> ListAllTaskType(Page<TaskType> p);
    //小程序使用
    List<TaskType> AllTaskType();
    //使用ID查询某条TaskType
    TaskType queryTaskType(String typeId);
    //删除打卡类型
    Integer DeleteTaskType(String typeId);
    //自增totalNum
    void incTotalNum(String typeId);
    //自增passNum
    void incPassNum(String typeId);
    //根据TypeId返回Type内容
    String getTypeContentByTypeId(String typeId);
    //根据typeContent查找是否已经存在
    Boolean alreadyExist(String typeContent);
}
