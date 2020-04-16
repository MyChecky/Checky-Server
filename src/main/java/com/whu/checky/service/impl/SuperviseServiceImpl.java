package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.*;
import com.whu.checky.mapper.*;
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
    private CheckMapper checkMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskMapper taskMapper;


    @Override
    public void addSupervise(Supervise supervise) {
        superviseMapper.insert(supervise);
    }

    @Override
    public void updateSupervise(String superviseId, String newState) {
        superviseMapper.updateState(superviseId, newState);

    }

    @Override
    public List<Supervise> queryUserSupervise(String userId, String checkId) {
        //查询某个用户的所有Supervise
        if (checkId == null) return superviseMapper.selectList(new EntityWrapper<Supervise>().
                eq("user_id", userId));
            //查询某个用户的某个check的所有Supervise
        else return superviseMapper.selectList(new EntityWrapper<Supervise>().
                eq("user_id", userId).eq("check_id", checkId));
    }


    @Override
    public Supervise querySupervise(String superviseId) {
        return superviseMapper.selectById(superviseId);
    }


    @Override
    public List<Check> userNeedToSupervise(String userId, String startDate, String endDate) {
        //查询某个用户需要监督的check
        List<String> allCheckId = superviseMapper.allNeedToCheck(userId, startDate, endDate);
        List<Check> result = new ArrayList<Check>();
        for (String checkId : allCheckId) {
            if (superviseMapper.haveSupervised(checkId, userId) == null) {
                Check check = checkMapper.selectById(checkId);
                Task task = taskMapper.selectById(check.getTaskId());
                check.setTaskTitle(task.getTaskTitle());
                check.setTaskContent(task.getTaskContent());
                result.add(check);
            }
        }
        return result;

//
//
//        List<Check> result= superviseMapper.needToSupervise(userId,startDate,endDate);
//        for (Check check: result ) {
//            check.setCheckContent(superviseMapper.getContent(check.getTaskId()));
//        }
//            return superviseMapper.needToSupervise(userId,startDate,endDate);

    }


    @Override
    public List<SupervisorState> querySuperviseState(String taskId, String checkId) {
        List<String> supervisorsId = taskSupervisorMapper.getTaskSupervisors(taskId);
        List<SupervisorState> supervisorsState = new ArrayList<SupervisorState>();
        for (String supervisorId : supervisorsId) {
            SupervisorState supervisorState = new SupervisorState();
            supervisorState.setSupervisorId(supervisorId);
            supervisorState.setSupervisorName(userMapper.getUsernameById(supervisorId));
            supervisorState.setSupervisorState(superviseMapper.getStateByIds(supervisorId, checkId));
            if (supervisorState.getSupervisorState() == null) {
                supervisorState.setSupervisorState("unknown");
            }
            supervisorsState.add(supervisorState);
        }
        return supervisorsState;
    }

    @Override
    public List<Supervise> getCheckSupsBySupId(Page<Supervise> supervisePage, String userId) {
        return superviseMapper.selectPage(supervisePage, new EntityWrapper<Supervise>()
                .eq("supervisor_id", userId)
                .orderBy("supervise_time"));
    }
}
