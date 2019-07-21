package com.whu.checky.service;

import java.util.HashMap;

public interface RedisService {

    String checkSessionId(String sessionId);

    void saveSessionId(String sessionId, String username);

    void delSessionId(String sessionId);

    void saveUserOrAdminBySessionId(String sessionId,Object obj);

    Object getUserOrAdminBySessionId(String sessionId);
}
