package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.UserFriend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "userFriendMapper")
public interface UserFriendMapper extends BaseMapper<UserFriend> {
    void addCooNum(@Param("fromUserId") String fromUserId,@Param("toUserId") String toUserId);

    Integer updateUserFriend(@Param("fromUserId") String fromUserId, @Param("userId") String userId,
                             @Param("addState") int addState);
}
