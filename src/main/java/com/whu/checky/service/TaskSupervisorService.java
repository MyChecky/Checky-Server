package com.whu.checky.service;

import java.util.List;

import com.whu.checky.domain.TaskSupervisor;

public interface TaskSupervisorService {
    void addTaskSupervisor(TaskSupervisor taskSupervisor);
    long getNumTaskSupervisors();
    List<TaskSupervisor> getMostRecentRecords(int num);
    void delTaskSupervisor(TaskSupervisor taskSupervisor);
}
