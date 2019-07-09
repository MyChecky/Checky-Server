package com.whu.checky.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.TaskSuspervisorMapper;
import com.whu.checky.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Wrapper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//匹配模块
public class Match {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskSuspervisorMapper taskSuspervisorMapper;

    @Autowired
    UserMapper userMapper;

//    暂时先做简单的匹配，思路为从数据库取出未匹配任务
//    取出用户记录并按监督次数排序
//    暂时只选取前三个建立监督关系，不足则取所有
    public void matchSupervisor(){
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
            .eq("task_state","nomatch")
        );

//        String [] judgements = {"supervise_num",""};
        List<User> userList = userMapper.selectList(new EntityWrapper<User>()
            .orderBy("supervise_num",true)
            .orderBy("supervise_num_min",false)
            .last("limit 70")
        );

        for(Task t:taskList){
            String taskId = t.getTaskId();
            for(int i=0;i<3&&i<userList.size();i++){
                TaskSupervisor temp = new TaskSupervisor();
                User user = userList.get(i);
                temp.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                temp.setTaskId(taskId);
                temp.setSupervisorId(user.getUserId());
                taskSuspervisorMapper.insert(temp);
                user.setSuperviseNum(user.getSuperviseNum()+1);
                userMapper.updateById(user);
                t.setSupervisorNum(t.getSupervisorNum()+1);
            }
            userList.sort(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getSuperviseNum()-o2.getSuperviseNum();
                }
            });
            taskMapper.updateById(t);
        }
    }
}
