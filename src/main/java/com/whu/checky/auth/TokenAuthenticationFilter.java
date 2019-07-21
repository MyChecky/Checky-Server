package com.whu.checky.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Grab the `?token=xxx` parameter into authentication, so as to trigger spring security's
 * authentication
 */

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc)
            throws ServletException, IOException {

        SecurityContext context = SecurityContextHolder.getContext();

        if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated() || req.getMethod().equals("GET")) {
            // do nothing
        } else {

            BufferedReader reader = req.getReader();
            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String json = builder.toString();
            if(json.equals("")) {
                fc.doFilter(req, res);
                return;
            }

//            int len = req.getContentLength();
//            ServletInputStream iii = req.getInputStream();
//            byte[] buffer = new byte[len];
//            iii.read(buffer, 0, len);
//            String json = new String(buffer);
            JSONObject data = JSON.parseObject(json);

//            Map<String, String[]> params = req.getParameterMap();
            if (data.containsKey("sessionKey") && data.containsKey("userId")) {
                String token = data.getString("sessionKey");
                String userId = data.getString("userId");
                if (token != null && userId != null) {
                    Authentication auth = new TokenAuthentication(token,userId);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            req.setAttribute("com.whu.checky.auth.TokenAuthenticationFilter.FILTERED", true);
        }

        fc.doFilter(req, res);

    }

    class TokenAuthentication implements Authentication {
        private String token;

        private String userId;

        private TokenAuthentication(String token, String userId) {
            this.token = token;
            this.userId = userId;
        }

        public String getUserId() {
            return userId;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public Object getCredentials() {
            return new Pair<>(token,userId);
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        }

        @Override
        public String getName() {
            return "token";
        }
    }
}
