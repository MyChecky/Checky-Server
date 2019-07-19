package com.whu.checky.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.TaskSupervisorMapper;
import com.whu.checky.mapper.UserMapper;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

//匹配模块
@Component
public class Match {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskSupervisorMapper taskSuspervisorMapper;

    @Autowired
    UserMapper userMapper;

    @Value("${jobs.match.maxNum}")
    private int matchMax;


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
//                t.setSupervisorNum(t.getSupervisorNum()+1);
            }
            userList.sort(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getSuperviseNum()-o2.getSuperviseNum();
                }
            });
//            taskMapper.updateById(t);
        }
    }




    @Scheduled(cron = "${jobs.match.cron}")
    public void matchSupervisorBySimilarity(){
        System.out.println("Task start!");
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
                .eq("task_state","nomatch")
                .or()
                .eq("task_state","during")
                .and()
                .lt("match_num",matchMax)
//                .or()
//                .lt("supervisor_num",matchMax)
        );



        for(Task t:taskList){
//            if(t.getTaskState().equals("during")) continue;
            PriorityQueue<Pair<Integer,Double>> queue = new PriorityQueue<>(
                    new Comparator<Pair<Integer,Double>>() {
                    @Override
                    public int compare(Pair<Integer,Double> o1, Pair<Integer,Double> o2) {
                        return (int)(o1.getValue() - o2.getValue());
                    }
            });
            for(int i=0;i<taskList.size();i++){
                Task another = taskList.get(i);
                if(t==another) continue;
                double value = computeSimiV1(t,another);
                queue.add(new Pair<>(i,value));
            }

            HashMap<String,Task> add = new HashMap<>();

            for(int i=t.getSupervisorNum();i<matchMax&&!queue.isEmpty();){
//                while(!queue.isEmpty()){
                int idx = queue.poll().getKey();
                Task temp = taskList.get(idx);
                if(temp.getMatchNum()==matchMax||add.containsKey(temp.getUserId())||temp.getUserId().equals(t.getUserId())) continue;
                add.put(temp.getUserId(),temp);
//                }
                i++;
            }

            if(add.size()==matchMax){
                for(Map.Entry<String,Task> entry:add.entrySet()){
                    Task temp = entry.getValue();
                    TaskSupervisor taskSupervisor = new TaskSupervisor();
                    taskSupervisor.setTaskId(t.getTaskId());
                    taskSupervisor.setSupervisorId(temp.getUserId());
                    taskSupervisor.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    taskSuspervisorMapper.insert(taskSupervisor);
                    t.setSupervisorNum(t.getSupervisorNum()+1);
                    t.setTaskState("during");
                    temp.setMatchNum(temp.getMatchNum()+1);
                }
            }
        }

        for(Task t:taskList) taskMapper.updateById(t);
    }

    private double computeSimiV1(Task t1, Task t2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        double ans = 0.0;
        try {
            int trans = (1000*60*60*24);
            double startTimeDiff = (sdf.parse(t1.getTaskStartTime()).getTime() - sdf.parse(t2.getTaskStartTime()).getTime()) / trans;
            double endTimeDiff = (sdf.parse(t1.getTaskEndTime()).getTime() - sdf.parse(t2.getTaskEndTime()).getTime()) / trans;
            double typeDiff = t1.getTaskId().equals(t2.getTypeId())?0.5:1;
            double goldDiff = t1.getTaskMoney()-t2.getTaskMoney();
            ans = startTimeDiff*0.3+endTimeDiff*0.3+typeDiff+goldDiff*0.2;
        }catch (Exception e){
            return 0.0;
        }
        return ans;
    }


}
