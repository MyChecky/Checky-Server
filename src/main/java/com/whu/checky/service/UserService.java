package com.whu.checky.service;

import com.whu.checky.domain.User;

import java.util.List;

public interface UserService {
    //第一次登录
     void register();
     //再次登录
    void update();
    //查询用户
    void queryUser();
    //删除用户
    void deleteUser();


}
