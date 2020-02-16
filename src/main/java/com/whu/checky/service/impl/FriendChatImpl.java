package com.whu.checky.service.impl;

import com.whu.checky.domain.FriendChat;
import com.whu.checky.mapper.FriendChatMapper;
import com.whu.checky.service.FriendChatService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FriendChatImpl implements FriendChatService {
    @Autowired
    private FriendChatMapper friendChatMapper;

    @Override
    public int addFriendChat(FriendChat friendChat) {
        return friendChatMapper.insert(friendChat);
    }

    @Override
    public int deleteChatsById(String userId) {
        return friendChatMapper.deleteChatsById(userId);
    }

    @Override
    public List<FriendChat> queryUserSendChatById(String userId) {
        return friendChatMapper.queryUserSendChatById(userId);
    }

    @Override
    public List<FriendChat> queryUserReceiverChatById(String userId) {
        return friendChatMapper.queryUserReceiverChatById(userId);
    }

    @Override
    public List<FriendChat> queryUserScopeSendChatById(String startDate, String endDate, String userId) {
        return friendChatMapper.queryUserScopeSendChatById(startDate, endDate, userId);
    }

    @Override
    public List<FriendChat> queryUserScopeReceiverChatById(String startDate, String endDate, String userId) {
        return friendChatMapper.queryUserScopeReceiverChatById(startDate, endDate, userId);
    }
}
