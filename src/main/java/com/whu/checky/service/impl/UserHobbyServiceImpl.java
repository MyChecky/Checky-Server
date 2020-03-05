package com.whu.checky.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.UserHobby;
import com.whu.checky.mapper.UserHobbyMapper;
import com.whu.checky.service.UserHobbyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userHobbyService")
public class UserHobbyServiceImpl implements UserHobbyService {

    @Autowired
    private UserHobbyMapper userHobbyMapper;

    @Override
    public List<UserHobby> getUserHobbies(String userId) {
        return userHobbyMapper.selectList(new EntityWrapper<UserHobby>().eq("USER_ID", userId));
    }

}
