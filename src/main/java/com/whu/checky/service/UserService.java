package com.whu.checky.service;

import com.whu.checky.domain.User;

import java.util.List;

public interface UserService {
    //第一次登录
    boolean register(User user);
     //再次登录
//    boolean updateSessionID(User user,String id);
    void updateUser(User user);
    //查询用户
    User queryUser(String userId);
    //删除用户
    void deleteUser(String userId);

    List<User> getAllUsers(int page);

    int getAllUsersNum();

    List<User> queryUsers(int page,String keyword);

}
