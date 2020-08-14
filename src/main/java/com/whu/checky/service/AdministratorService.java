package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.User;

import java.util.List;
import java.util.Map;

public interface AdministratorService {
    //管理员注册
    int register(Administrator administrator) throws Exception;
    //管理员登录
    int login(Administrator administrator) throws Exception;
    //修改管理员信息
    int update(Administrator administrator);
    //删除管理员
    Integer deleteById(String userId);
    //获取管理员列表
    List<Administrator> getAllAdmins(Page<Administrator> page);
    //获取管理员数量
    int getAllAdminsNum();
    //搜索管理员
    List<Administrator> queryAdmins(int page,String keyword);
    //单个管理员
    Administrator queryAdmin(String userId);

    List<Administrator> queryAdminsWithPage(Page<Administrator> p, String keyWord);

    Map<String, Boolean> getAdminPowers(java.lang.String userId);

    List<String> getPermissionsById(String userId);

    void updateAdminMenus(String userId, List<String> menus);
}
