<<<<<<< HEAD
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
    @TableField(exist = false)
    private String friendAvatar;

    private int add_state;
    private String addContent;

    public int getAdd_state() {
        return add_state;
    }

    public void setAdd_state(int add_state) {
        this.add_state = add_state;
    }

    public String getAddContent() {
        return addContent;
    }

    public void setAddContent(String addContent) {
        this.addContent = addContent;
    }

    public String getFriendAvatar() {
        return friendAvatar;
    }

    public void setFriendAvatar(String friendAvatar) {
        this.friendAvatar = friendAvatar;
    }

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
=======
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
    @TableField(exist = false)
    private String friendAvatar;

    private int add_state;
    private String addContent;

    public int getAdd_state() {
        return add_state;
    }

    public void setAdd_state(int add_state) {
        this.add_state = add_state;
    }

    public String getAddContent() {
        return addContent;
    }

    public void setAddContent(String addContent) {
        this.addContent = addContent;
    }

    public String getFriendAvatar() {
        return friendAvatar;
    }

    public void setFriendAvatar(String friendAvatar) {
        this.friendAvatar = friendAvatar;
    }

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
>>>>>>> e6b0da8bd1a7a087f07027969ed1183606b33320
