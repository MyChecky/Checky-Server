package com.whu.checky.service.impl;

import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Role;
import com.whu.checky.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    RedisServiceImpl redisService;

    @Override
    public UserDetails authenticateToken(@NonNull String token,String id) {
        Object obj = redisService.getUserOrAdminBySessionId(token);
        if(obj!=null) {
            if (obj instanceof com.whu.checky.domain.User) {

                com.whu.checky.domain.User user = (com.whu.checky.domain.User) obj;
                if (id.equals(user.getUserId())) {
                    return User.builder()
                            .username(id)
                            .password("")
                            .authorities(Role.USER)
                            .build();
                }
            } else if (obj instanceof Administrator) {
                Administrator admin = (Administrator) obj;
                if (id.equals(admin.getUserId())) {
                    return User.builder()
                            .username(id)
                            .password("")
                            .authorities(Role.ADMIN,Role.USER)
                            .build();
                }
            }
            redisService.updateExpireTime(token);

        }

        return User.builder()
                .username(id)
                .password("")
                .authorities(Role.VISITOR).build();


//        throw ResultException.of(MyError.TOKEN_NOT_FOUND)
//                .errorData(token);
//        return null;
    }
}
