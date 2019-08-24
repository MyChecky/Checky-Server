package com.whu.checky.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Appeal;

import java.util.List;

public interface AppealService {
    //添加申诉
    boolean addAppeal(Appeal appeal);
    //撤销申诉
    boolean deleteAppeal(String appealId);
    //对申诉处理
    void updateAppeal();
    //查询申诉
    List<Appeal> queryAppealFromUser(String userId);
    //管理员查看所有申诉
    List<Appeal> displayAppeals(Page<Appeal> page);

}
