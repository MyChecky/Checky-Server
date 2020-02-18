package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.whu.checky.domain.FriendChat;
import com.whu.checky.mapper.FriendChatMapper;
import com.whu.checky.service.FriendChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("/FriendChat")
public class FriendChatImpl implements FriendChatService {
    @Autowired
    private FriendChatMapper friendChatMapper;

    @Override
    public int addFriendChat(FriendChat friendChat) {
        return friendChatMapper.insert(friendChat);
    }

    @Override
    public Integer updateFriendChat(FriendChat friendChat) {
        return friendChatMapper.updateFriendChatIfRead(friendChat.getSendId(), friendChat.getReceiverId(),
                friendChat.getChatTime(), friendChat.getIfRead());
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

    @Override
    public List<FriendChat> queryMsgsFromFriends(String userId1, String userId2) {
        return friendChatMapper.queryMsgsFromFriends(userId1, userId2);
    }

    @Override
    public FriendChat queryLatestMessageByIds(String userId1, String userId2) {
        return friendChatMapper.queryLatestMessageByIds(userId1, userId2);
    }
}
