package com.whu.checky.service;

public interface RedisService {

    String checkSessionId(String sessionId);

    void saveSessionId(String sessionId, String username);

    void delSessionId(String sessionId);
}
