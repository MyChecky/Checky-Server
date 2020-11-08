package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class UserMedal {

    private String medalId;

    private String userId;

    private String time;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
