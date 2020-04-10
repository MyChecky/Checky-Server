package com.whu.checky.auth.filter;


import javafx.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.whu.checky.auth.TokenAuthentication;



public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc)
            throws ServletException, IOException {

        SecurityContext context = SecurityContextHolder.getContext();

        if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated() || req.getMethod().equals("GET")) {
            // do nothing
        } else {
            // 这里是从请求首部获取，也可以从请求体中获取相关认证参数
            String sessionKey = req.getHeader("sessionKey");
            String userId = req.getHeader("userId");
            if (sessionKey != null && userId != null) {
                Authentication auth = new TokenAuthentication(sessionKey, userId);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            req.setAttribute("com.whu.checky.auth.filter.TokenAuthenticationFilter.FILTERED", true);
        }

        fc.doFilter(req, res);

    }


}
