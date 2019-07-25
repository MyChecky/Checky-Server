package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.User;
import com.whu.checky.domain.UserFriend;
import com.whu.checky.mapper.UserFriendMapper;
import com.whu.checky.mapper.UserMapper;
import com.whu.checky.service.UserFriendService;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userFriendService")
public class UserFriendServiceImpl implements UserFriendService {

    @Autowired
    private UserFriendMapper userFriendMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public Integer addUserFriend(UserFriend userFriend) {
        return userFriendMapper.insert(userFriend);
    }

    @Override
    public void addCooNum(String fromUserId, String toUserId) {
        userFriendMapper.addCooNum(fromUserId,toUserId);
    }

    @Override
    public List<UserFriend> queryUserFriends(String userId) {
        List<UserFriend> friends=userFriendMapper.selectList(new EntityWrapper<UserFriend>()
        .eq("from_user_id",userId)
        .or()
        .eq("to_user_id",userId));
        for (UserFriend userFriend:friends){
            if(userFriend.getToUserId().equals(userId)){
                userFriend.setFriendName(userMapper.getUsernameById(userFriend.getFromUserId()));
            }else {
                userFriend.setFriendName(userMapper.getUsernameById(userFriend.getToUserId()));
            }
        }
        return friends;
    }
}