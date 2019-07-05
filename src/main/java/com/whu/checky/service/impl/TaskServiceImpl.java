package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.whu.checky.domain.Task;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Task> queryUserTasks(String userid, String date) {
        if(date==null) return taskMapper.selectList(new EntityWrapper<Task>().eq("user_id",userid));
        else return taskMapper.selectList(new EntityWrapper<Task>().eq("user_id",userid).ge("task_start_time",date)
                .le("task_end_time",date));
    }



    @Override
    public List<Task> listTasks() {
        return taskMapper.selectList(new EntityWrapper<Task>().orderBy("task_start_time"));
    }

    @Override
    public Task queryTask(String taskid) {
        return taskMapper.selectById(taskid);
    }


}
