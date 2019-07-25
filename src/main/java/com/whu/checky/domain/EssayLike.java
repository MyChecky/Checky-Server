package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class EssayLike {
    @TableId
    private String userId;
    @TableId
    private String essayId;
    private String addTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEssayId() {
        return essayId;
    }

    public void setEssayId(String essayId) {
        this.essayId = essayId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
