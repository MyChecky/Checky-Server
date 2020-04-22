package com.whu.checky.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.mapper.TaskSupervisorMapper;
import com.whu.checky.service.TaskSupervisorService;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("TaskSupervisorService")
public class TaskSupervisorServiceImpl implements TaskSupervisorService {
    @Autowired
    private TaskSupervisorMapper taskSupervisorMapper;

    @Override
    public Integer addTaskSupervisor(TaskSupervisor taskSupervisor) {
        return taskSupervisorMapper.insert(taskSupervisor);
    }

    @Override
    public long getNumTaskSupervisors() {
        return taskSupervisorMapper.selectCount(null);
    }

    @Override
    public List<TaskSupervisor> getMostRecentRecords(int num) {
        return taskSupervisorMapper.selectPage(new RowBounds(0, num),
                new EntityWrapper<TaskSupervisor>().orderBy("add_time", false));
    }

    @Override
    public void delTaskSupervisor(TaskSupervisor taskSupervisor) {
        taskSupervisorMapper.delete(new EntityWrapper<>(taskSupervisor));
    }

    @Override
    public List<TaskSupervisor> getTasksSupByTaskId(String taskId) {
        return taskSupervisorMapper.selectList(new EntityWrapper<TaskSupervisor>()
                .eq("task_id", taskId)
                .isNull("remove_time"));
    }

    @Override
    public Integer updateTaskSup(TaskSupervisor taskSupervisor) {
        return taskSupervisorMapper.updateById(taskSupervisor);
    }
}
