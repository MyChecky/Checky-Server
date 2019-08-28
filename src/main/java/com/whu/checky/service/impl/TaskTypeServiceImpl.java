package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.TaskType;
import com.whu.checky.mapper.TaskTypeMapper;
import com.whu.checky.service.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("taskTypeService")
public class TaskTypeServiceImpl implements TaskTypeService {
    @Autowired
    private TaskTypeMapper taskTypeMapper;


    @Override
    public Integer addTaskType(TaskType taskType) {
        return taskTypeMapper.insert(taskType);
    }

    @Override
    public Integer updataTaskType(TaskType taskType) {
        return taskTypeMapper.updateById(taskType);
    }


    @Override
    public List<TaskType> ListAllTaskType() {

        return taskTypeMapper.selectList(new EntityWrapper<TaskType>());
    }

    @Override
    public TaskType QueryTaskType(String typeId) {
        return taskTypeMapper.selectById(typeId);
    }

    @Override
    public Integer DeleteTaskType(String typeId) {
        return taskTypeMapper.deleteById(typeId);
    }
}
