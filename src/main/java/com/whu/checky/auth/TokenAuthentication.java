package com.whu.checky.auth;

import javafx.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthentication implements Authentication {
    private String token;

    private String userId;

    public TokenAuthentication(String token, String userId) {
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
        return new Pair<>(token, userId);
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
