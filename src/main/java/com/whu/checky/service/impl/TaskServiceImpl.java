package com.whu.checky.service.impl;

import com.whu.checky.domain.Task;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("taskService")
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Override
    public Integer addTask(Task task) {
        return taskMapper.insert(task);
    }

    @Override
    public Integer delTask(String taskid) {
        return taskMapper.deleteById(taskid);
    }

    @Override
    public Integer updataTask(Task task) {
        return taskMapper.updateById(task);
    }
}
