package com.whu.checky.domain;



import java.sql.Date;

public class User {
    //用户id
    private String userId;
//    //用户微信名
    private String nickName;
//    //用户微信头像url
    private String userAvatar;
//    //用户性别
    private Integer gender;

    private String sessionID;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSessionID() {
        return sessionID;
    }

    //    private String openid;
//
    public void setSessionID(String id){
        sessionID=id;
    }



}
