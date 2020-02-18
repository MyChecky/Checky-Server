package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "UserMapper")
public interface UserMapper extends BaseMapper<User> {
    String getUsernameById(@Param("userId") String userId);
    String getUserAvatarById(@Param("userId") String userId);
}
