package com.whu.checky.service;

import com.whu.checky.domain.User;
import com.whu.checky.domain.UserFriend;

import java.util.List;

public interface UserFriendService {
    //任务结束之后添加好友关系
    Integer addUserFriend(UserFriend userFriend);
    //再次匹配增加合作次数
    void addCooNum(String fromUserId,String toUserId);
    //获取当前用户的朋友
    List<UserFriend> queryUserFriends(String userId);
}
