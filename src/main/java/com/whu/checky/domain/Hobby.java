package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class Hobby {
    @TableId
    private int hobbyId;

    private String hobbyValue;

    private String addTime;

    public int getHobbyId() {
        return hobbyId;
    }

    public void setHobbyId(int hobbyId) {
        this.hobbyId = hobbyId;
    }

    public String getHobbyValue() {
        return hobbyValue;
    }

    public void setHobbyValue(String hobbyValue) {
        this.hobbyValue = hobbyValue;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
