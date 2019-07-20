package com.whu.checky.service.impl;

import com.whu.checky.domain.Role;
import com.whu.checky.service.TokenService;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public UserDetails authenticateToken(@NonNull String token) {
        if (token.equals("abcdefg")) {
            return User.builder()
                    .username("api")
                    .password("")
                    .authorities(Role.API)
                    .build();
        }

//        throw ResultException.of(MyError.TOKEN_NOT_FOUND)
//                .errorData(token);
        return null;
    }
}
