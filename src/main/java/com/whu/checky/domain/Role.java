package com.whu.checky.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    USER,
    VISITOR;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
