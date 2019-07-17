package com.whu.checky.service;

import com.whu.checky.domain.Essay;

import java.util.List;

public interface EssayService {
    //发布动态
    int addEssay(Essay essay);
    //修改动态
    int modifyEssay(Essay essay);
    //删除动态
    int deleteEssay(String essayId);
    //上传文件
    void uploadFile();
    //展示动态
    List<Essay> displayEssay();
    //展示动态
    List<Essay> getMyEssay(String userId);



}
