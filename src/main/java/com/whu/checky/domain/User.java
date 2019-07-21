package com.whu.checky.domain;



import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User implements Serializable{
    /**
     * 用户id
     * GeneratedValue
     */
    @TableId
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
    private Integer userGender;
    /**
     * 用户余额
     */
    private Double userMoney;
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
    private Integer wantpush;
    /**
     * 经度
     */
    private double longtitude=0.0;
    /**
     * 纬度
     */
    private double latitude=0.0;

    private String sessionId;

    private int userCredit;

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(int userCredit) {
        this.userCredit = userCredit;
    }

    private String userTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

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

    public Integer getWantpush() {
        return wantpush;
    }

    public void setWantpush(Integer wantpush) {
        this.wantpush = wantpush;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getUserGender() {
        return userGender;
    }

    public void setUserGender(Integer userGender) {
        this.userGender = userGender;
    }

    public Double getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(Double userMoney) {
        this.userMoney = userMoney;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
