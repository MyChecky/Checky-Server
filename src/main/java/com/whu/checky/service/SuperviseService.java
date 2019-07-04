package com.whu.checky.service;

import com.whu.checky.domain.Check;
import com.whu.checky.domain.Supervise;

import java.util.List;

public interface SuperviseService {
    //每日监督(这个接口是提供给用户的)
    void addSupervise(Supervise supervise);

    //这个接口是提供给管理员的
    void updateSupervise(String superviseId,String newState);
//    //监督审核通过
//    void updateSupervisePass();
//    //监督审核未通过
//    void updateSuperviseFail();
    //查询审核
    Supervise querySupervise(String superviseId);

    List<Supervise> queryUserSupervise(String superviseId);

    List<Check> userNeedToSupervise(String userid, String date1, String date2);
}
