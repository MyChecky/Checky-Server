package com.whu.checky.domain;



import java.sql.Date;

public class User {
    /**
     * 用户id
     * GeneratedValue
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户性别
     */
    private Integer gender;
    /**
     * 用户余额
     */
    private Double money;
    /**
     * 用户发布的任务数
     */
    private Integer taskNum;
    /**
     * 用户成功完成的任务数
     */
    private Integer taskNumSuc;
    /**
     * 用户监督的次数
     */
    private Integer superviseNum;
    /**
     * 用户用户最少需要监督的次数
     */
    private Integer superviseNumMin;
    /**
     * 用户是否需要推送通知
     * 0：不需要 1：需要
     */
    private Integer wantPush;
    /**
     * 经度
     */
    private String longtitude;
    /**
     * 纬度
     */
    private String latitude;

    private String sessionID;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public Integer getTaskNumSuc() {
        return taskNumSuc;
    }

    public void setTaskNumSuc(Integer taskNumSuc) {
        this.taskNumSuc = taskNumSuc;
    }

    public Integer getSuperviseNum() {
        return superviseNum;
    }

    public void setSuperviseNum(Integer superviseNum) {
        this.superviseNum = superviseNum;
    }

    public Integer getSuperviseNumMin() {
        return superviseNumMin;
    }

    public void setSuperviseNumMin(Integer superviseNumMin) {
        this.superviseNumMin = superviseNumMin;
    }

    public Integer getWantPush() {
        return wantPush;
    }

    public void setWantPush(Integer wantPush) {
        this.wantPush = wantPush;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String id){
        sessionID=id;
    }



}
