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
}
