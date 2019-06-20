package com.whu.checky.service;

public interface SuperviseService {
    //每日监督
    void addSupervise();
    //监督审核通过
    void updateSupervisePass();
    //监督审核未通过
    void updateSuperviseFail();
    //查询审核
    void querySupervise();
}
