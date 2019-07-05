package com.whu.checky.service;

import com.whu.checky.domain.Check;
import com.whu.checky.domain.Supervise;

import java.util.List;

public interface SuperviseService {
    //每日监督(这个接口是提供给用户的)
    void addSupervise(Supervise supervise);

    //这个接口是提供给管理员的
    void updateSupervise(String superviseId,String newState);

    //查询审核
    List<Supervise> queryUserSupervise(String userId,String checkId);

    //具体的查询某个Supervise
    Supervise querySupervise(String superviseId);


    List<Check> userNeedToSupervise(String userid, String date1, String date2);
}
