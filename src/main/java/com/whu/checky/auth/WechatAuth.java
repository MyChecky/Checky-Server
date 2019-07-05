package com.whu.checky.auth;

import com.whu.checky.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

public class WechatAuth {

    @Autowired
    private static RedisService redisService;

    static String userAuth(String sessionId){
        //根据用户传来的sessionId查询用户名，不合法时返回NULL
        return redisService.checkSessionId(sessionId);
    }

}
