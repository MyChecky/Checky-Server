package com.whu.checky.service;

import com.whu.checky.domain.Check;

import java.util.List;

public interface CheckService {
    //每日打卡
    void addCheck(Check check);
    //上传文件
    void uploadFile();
    //查询打卡
    List<Check> queryCheck(String checkId);

    void getCheckHistory();

    void updateCheck(Check check);

    void deleteCheck(Check check);

}
