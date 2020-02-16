package com.whu.checky.service;

import com.whu.checky.domain.FriendChat;

import java.util.List;

public interface FriendChatService {
    int addFriendChat(FriendChat friendChat);
    int deleteChatsById(String userId);
    List<FriendChat> queryUserSendChatById(String userId);
    List<FriendChat> queryUserReceiverChatById(String userId);
    List<FriendChat> queryUserScopeSendChatById(String startDate, String endDate, String userId);
    List<FriendChat> queryUserScopeReceiverChatById(String startDate, String endDate, String userId);
}
