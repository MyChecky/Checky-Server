package com.whu.checky.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.User;

import java.util.List;

public interface AdministratorService {
    //管理员注册
    int register(Administrator administrator) throws Exception;
    //管理员登录
    int login(Administrator administrator) throws Exception;
    //修改管理员信息
    int update(Administrator administrator);
    //删除管理员
    boolean deleteById(Administrator administrator);
    //获取管理员列表
    List<Administrator> getAllAdmins(int page, int pageSize);
    //获取管理员数量
    int getAllAdminsNum();
    //搜索管理员
    List<Administrator> queryAdmins(int page,String keyword);
    //单个管理员
    Administrator queryAdmin(String userId);
}
