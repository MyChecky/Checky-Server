package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.UserMapper;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public boolean register(User user) {
        mapper.insert(user);
        return true;
    }

    @Override
    public void updateUser(User user) {
//        user.setSessionId(id);
        mapper.updateById(user);
//        return true;
    }

    @Override
    public User queryUser(String userId) {
        return mapper.selectById(userId);
    }

    @Override
    public void deleteUser(String userId) {
        mapper.deleteById(userId);
    }

    @Override
    public List<User> getAllUsers(int page) {
        return mapper.selectPage(new Page<User>(page,10),new EntityWrapper<User>().orderBy("user_time"));
    }
}
