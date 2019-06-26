package com.whu.checky.domain;



import java.sql.Date;

public class User {
    //用户id
//    @Id
//    @GeneratedValue
    private String userId;
//    //用户微信名
//    @Column(nullable = false)
    private String nickName;
//    //用户微信头像url
//    @Column(nullable = false)
    private String userAvatar;
//    //用户性别
//    @Column(nullable = false)
    private Integer gender;
//    //用户使用小程序的时间
//    @Column(nullable = false)
//    private Date signindate;
//    //地理位置

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
