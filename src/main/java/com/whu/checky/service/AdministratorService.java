package com.whu.checky.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.whu.checky.domain.Administrator;

public interface AdministratorService {
    //管理员注册
    int register(Administrator administrator) throws Exception;
    //管理员登录
    int login(Administrator administrator) throws Exception;
    //修改管理员信息
    boolean update(Administrator administrator);
    //删除管理员
    boolean deleteById(Administrator administrator);
}
