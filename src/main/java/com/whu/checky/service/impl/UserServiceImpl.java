package com.whu.checky.service.impl;

import com.whu.checky.domain.User;
import com.whu.checky.mapper.UserMapper;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean updateSessionID(User user,String id) {
        user.setSessionId(id);
        mapper.updateById(user);
        return true;
    }

    @Override
    public User queryUser(User user) {
        return mapper.selectById(user);
    }

    @Override
    public void deleteUser() {

    }
}
