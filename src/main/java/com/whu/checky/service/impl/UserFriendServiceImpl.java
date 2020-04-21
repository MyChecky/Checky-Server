package com.whu.checky.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.UserFriend;
import com.whu.checky.mapper.UserFriendMapper;
import com.whu.checky.mapper.UserMapper;
import com.whu.checky.service.UserFriendService;

import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        userFriendMapper.addCooNum(fromUserId, toUserId);
    }

    @Override
    public List<UserFriend> queryUserFriends(String userId) {
        List<UserFriend> friends = userFriendMapper.selectList(new EntityWrapper<UserFriend>()
                .eq("from_user_id", userId).or()
                .eq("to_user_id", userId).and().eq("add_state", MyConstants.FRIEND_PASS));
        for (UserFriend userFriend : friends) {
            if (userFriend.getToUserId().equals(userId)) {
                userFriend.setFriendName(userMapper.getUsernameById(userFriend.getFromUserId()));
                userFriend.setFriendAvatar(userMapper.getUserAvatarById(userFriend.getFromUserId()));
            } else {
                userFriend.setFriendName(userMapper.getUsernameById(userFriend.getToUserId()));
                userFriend.setFriendAvatar(userMapper.getUserAvatarById(userFriend.getToUserId()));
            }
        }
        return friends;
    }

    @Override
    public List<UserFriend> queryUserNewFriends(String userId) {
        List<UserFriend> friends = userFriendMapper
                .selectList(new EntityWrapper<UserFriend>()
                        .eq("to_user_id", userId)
                        .eq("add_state", MyConstants.FRIEND_APPLY));
        for (UserFriend userFriend : friends) {
            userFriend.setFriendName(userMapper.getUsernameById(userFriend.getFromUserId()));
            userFriend.setFriendAvatar(userMapper.getUserAvatarById(userFriend.getFromUserId()));
        }
        return friends;
    }

    @Override
    public Integer updateUserFriend(String fromUserId, String userId, int addState) {
        return userFriendMapper.updateUserFriend(fromUserId, userId, addState);
    }

    @Override
    public void delUserFriend(UserFriend userFriend) {
        userFriendMapper.delete(new EntityWrapper<>(userFriend));
    }
}
