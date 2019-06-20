package com.whu.checky.service;

public interface CheckService {
    //每日打卡
    void addCheck();
    //上传文件
    void uploadFile();
    //查询打卡
    void queryCheck();

    void getCheckHistory();

    void updateCheck();

    void deleteCheck();

}
