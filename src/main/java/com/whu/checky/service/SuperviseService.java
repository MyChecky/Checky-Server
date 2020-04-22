package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Supervise;
import com.whu.checky.domain.SupervisorState;
import com.whu.checky.domain.TaskSupervisor;

import java.util.List;

public interface SuperviseService {
    //每日监督(这个接口是提供给用户的)
    Integer addSupervise(Supervise supervise);

    //这个接口是提供给管理员的
    void updateSupervise(String superviseId,String newState);

    //查询审核
    List<Supervise> queryUserSupervise(String userId,String checkId);

    //具体的查询某个Supervise
    Supervise querySupervise(String superviseId);


    List<Check> userNeedToSupervise(String userId, String startDate, String endDate);

    //监督者监督状态
    List<SupervisorState> querySuperviseState(String taskId, String checkId);

    List<Supervise> getCheckSupsBySupId(Page<Supervise> supervisePage, String userId);
}
