package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.FriendChat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("friendChatMapper")
public interface FriendChatMapper  extends BaseMapper<FriendChat> {
    int deleteChatsById(@Param("userId") String userId);
    List<FriendChat> queryUserSendChatById(@Param("userId") String userId);
    List<FriendChat> queryUserReceiverChatById(String userId);
    List<FriendChat> queryUserScopeSendChatById(@Param("startDate") String startDate,
                                                @Param("endDate") String endDate, @Param("userId") String userId);
    List<FriendChat> queryUserScopeReceiverChatById(@Param("startDate") String startDate,
                                                    @Param("endDate") String endDate, @Param("userId") String userId);
    FriendChat queryLatestMessageByIds(@Param("userId1") String userId1, @Param("userId2") String userId2);
    List<FriendChat> queryMsgsFromFriends(@Param("userId1") String userId1, @Param("userId2") String userId2);
    List<FriendChat> queryMsgsFromFriendsByPage(@Param("userId1") String userId1, @Param("userId2") String userId2,
                                          @Param("page") int page);

    Integer updateFriendChatIfRead(@Param("sendId") String sendId, @Param("receiverId") String receiverId,
                          @Param("chatTime") String chatTime, @Param("ifRead") int ifRead);
}
