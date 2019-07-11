package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.TaskSupervisorMapper;
import com.whu.checky.service.TaskService;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service("taskService")
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskSupervisorMapper taskSupervisorMapper;

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

    @Override
    public HashMap <String, Double> getDistribute(String taskid) {
        HashMap<String, Double> result = new HashMap <>();

        Task t = taskMapper.selectById(taskid);

        List<TaskSupervisor> supervisors = taskSupervisorMapper.selectList(new EntityWrapper <TaskSupervisor>().eq("task_id",taskid));
        supervisors.sort(Comparator.comparingInt(TaskSupervisor::getSuperviseNum));

        int length = supervisors.size();
        double money = 0;
        if(length != 0)
            money = t.getTaskMoney()*0.8;

        for(int i = length - 1; i >=0; i--){
            TaskSupervisor taskSupervisor = supervisors.get(i);
            if(i != 0)
                money *= 0.6;
            taskSupervisor.setBenefit(money);
            taskSupervisorMapper.update(taskSupervisor, new EntityWrapper <TaskSupervisor>().eq("supervisor_id",taskSupervisor.getSupervisorId()));
            result.put(supervisors.get(i).getSupervisorId(),money);
        }

        return result;
    }


}
