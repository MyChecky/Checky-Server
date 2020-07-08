package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class Medal {
    @TableId
    private String medalId;

    private String userId;

    private String medalUrl;

    private String expireTime;

    private String ifExpired;

    public String getMedalId() {
        return medalId;
    }

    public void setMedalId(String medalId) {
        this.medalId = medalId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedalUrl() {
        return medalUrl;
    }

    public void setMedalUrl(String medalUrl) {
        this.medalUrl = medalUrl;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getIfExpired() {
        return ifExpired;
    }

    public void setIfExpired(String ifExpired) {
        this.ifExpired = ifExpired;
    }
}
