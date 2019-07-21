package com.whu.checky.service.impl;

import com.whu.checky.domain.User;
import com.whu.checky.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service("redisService")
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private HashOperations<String,String,Object> opsForHash;

    @Override
    public String checkSessionId(String sessionId) {

        return stringRedisTemplate.opsForValue().get(sessionId);
    }

    @Override
    public void saveSessionId(String sessionId, String username) {
        stringRedisTemplate.opsForValue().set(sessionId,username,30, TimeUnit.DAYS);

    }

    @Override
    public void delSessionId(String sessionId) {
//        stringRedisTemplate.delete(sessionId);
        redisTemplate.delete(sessionId);
    }

    @Override
    public void saveUserOrAdminBySessionId(String sessionId, Object obj) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        if(obj instanceof User) operations.set(sessionId, obj,2,TimeUnit.HOURS);
        else operations.set(sessionId, obj,30,TimeUnit.MINUTES);
    }

    @Override
    public Object getUserOrAdminBySessionId(String sessionId){
        return redisTemplate.opsForValue().get(sessionId);
    }


}
