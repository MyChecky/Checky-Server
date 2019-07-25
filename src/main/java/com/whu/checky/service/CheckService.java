package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Check;

import java.util.List;

public interface CheckService {
    //每日打卡
    void addCheck(Check check);
    //上传文件
    void uploadFile();
    //查询打卡
    List<Check> queryCheck(String column, String checkId);

    List<Check> queryCheckByUserId(String userId, Page<Check> page);

    void getCheckHistory();

    void updateCheck(Check check);

    void deleteCheck(String checkId);

    Check getCheckByTask(String taskId,String date);

    void updatePassSuperviseCheck(String checkId);

    void updateDenySuperviseCheck(String checkId);

}
