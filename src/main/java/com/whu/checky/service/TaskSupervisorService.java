<<<<<<< HEAD
package com.whu.checky.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.TaskSupervisor;

public interface TaskSupervisorService {
    Integer addTaskSupervisor(TaskSupervisor taskSupervisor);
    long getNumTaskSupervisors();
    List<TaskSupervisor> getMostRecentRecords(int num);
    void delTaskSupervisor(TaskSupervisor taskSupervisor);

    List<TaskSupervisor> getTasksSupByTaskId(String taskId);

    Integer updateTaskSup(TaskSupervisor taskSupervisor);
}
=======
package com.whu.checky.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.TaskSupervisor;

public interface TaskSupervisorService {
    Integer addTaskSupervisor(TaskSupervisor taskSupervisor);
    long getNumTaskSupervisors();
    List<TaskSupervisor> getMostRecentRecords(int num);
    void delTaskSupervisor(TaskSupervisor taskSupervisor);

    List<TaskSupervisor> getTasksSupByTaskId(String taskId);

    Integer updateTaskSup(TaskSupervisor taskSupervisor);
}
>>>>>>> e6b0da8bd1a7a087f07027969ed1183606b33320
