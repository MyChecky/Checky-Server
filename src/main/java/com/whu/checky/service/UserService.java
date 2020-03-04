package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

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

    List<User> getAllUsers(Page<User> page, boolean isAsc);

    int getAllUsersNum();

    List<User> queryUsers(int page,String keyword, int pageSize);

    List<User> getUserListForMatch();

    List<User> queryUserByNickname(String nickname);

    int queryUsersNum(String keyWord);

    List<User> queryUsersWithPage(Page<User> p, String keyWord);
}
