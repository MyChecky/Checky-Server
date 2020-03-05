package com.whu.checky.service.impl;

import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.mapper.TaskSupervisorMapper;
import com.whu.checky.service.TaskSupervisorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("taskSupervisorService")
public class TaskSupervisorServiceImpl implements TaskSupervisorService {
    @Autowired
    private TaskSupervisorMapper taskSupervisorMapper;

    @Override
    public void addTaskSupervisor(TaskSupervisor taskSupervisor) {
        taskSupervisorMapper.insert(taskSupervisor);
    }
}
