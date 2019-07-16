package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Supervise;
import com.whu.checky.domain.SupervisorState;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.mapper.SuperviseMapper;
import com.whu.checky.mapper.TaskSupervisorMapper;
import com.whu.checky.mapper.UserMapper;
import com.whu.checky.service.SuperviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("superviseServiceImpl")
public class SuperviseServiceImpl implements SuperviseService {
    @Autowired
    private SuperviseMapper superviseMapper;
    @Autowired
    private TaskSupervisorMapper taskSupervisorMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public void addSupervise(Supervise supervise) {
        superviseMapper.insert(supervise);
    }

    @Override
    public void updateSupervise(String superviseId, String newState) {
        superviseMapper.updateState(superviseId,newState);

    }

    @Override
    public List<Supervise> queryUserSupervise(String userId, String checkId) {
        //查询某个用户的所有Supervise
        if(checkId==null) return superviseMapper.selectList(new EntityWrapper<Supervise>().
                eq("user_id",userId));
        //查询某个用户的某个check的所有Supervise
        else  return superviseMapper.selectList(new EntityWrapper<Supervise>().
                eq("user_id",userId).eq("check_id",checkId));
    }



    @Override
    public Supervise querySupervise(String superviseId) {
        return superviseMapper.selectById(superviseId);
    }


    @Override
    public List<Check> userNeedToSupervise(String userid, String date1, String date2) {
        //查询某个用户需要监督的check
        List<Check> result= superviseMapper.needToSupervise(userid,date1,date2);
        for (Check check: result ) {
            check.setCheckContent(superviseMapper.getContent(check.getTaskId()));

        }
            return superviseMapper.needToSupervise(userid,date1,date2);

    }


    @Override
    public List<SupervisorState> querySuperviseState(String taskId, String checkId){
        String[] supervisorsId=(String[]) taskSupervisorMapper.getTaskSupervisors(taskId).toArray();
        List<SupervisorState> supervisorsState=new ArrayList<SupervisorState>();
        for(String supervisorId:supervisorsId){
            SupervisorState supervisorState=new SupervisorState();
            supervisorState.setSupervisorId(supervisorId);
            supervisorState.setSupervisorName(userMapper.getUsernameById(supervisorId));
            supervisorState.setSupervisorState(superviseMapper.getStateByIds(supervisorId,checkId));
            if (supervisorState.getSupervisorState()==null){
                supervisorState.setSupervisorState("unknown");
            }
        }
        return supervisorsState;
    }
}
