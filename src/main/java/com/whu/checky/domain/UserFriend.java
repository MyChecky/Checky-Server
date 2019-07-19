package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 表示好友关系的记录
 */
public class UserFriend {
    private String toUserId;
    private String fromUserId;
    private String cooNum;
    private String addTime;
    @TableField(exist = false)
    private String friendName;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getCooNum() {
        return cooNum;
    }

    public void setCooNum(String cooNum) {
        this.cooNum = cooNum;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
