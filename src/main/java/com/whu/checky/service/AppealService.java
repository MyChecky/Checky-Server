package com.whu.checky.service;

public interface AppealService {
    //添加申诉
    void addAppeal();
    //撤销申诉
    void deleteAppeal();
    //对申诉处理
    void updateAppeal();
    //查询申诉
    void queryAppeal();
}
