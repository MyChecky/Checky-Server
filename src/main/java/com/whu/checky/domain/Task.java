package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.whu.checky.util.MyConstants;

public class Task {
    @TableId
    private String taskId;

    private String userId;

    @TableField(exist = false)
    private String userName;

    private String typeId;
    //2020/8/20
    //每个任务对应的tags
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;

    //2019/8/28
    @TableField(exist = false)
    private String typeContent;

    private String taskTitle;

    private String taskContent;
    /**
     * 任务开始时间
     */
    private String taskStartTime;
    /**
     * 任务结束时间
     */
    private String taskEndTime;
    /**
     * 任务状态
     */
    private String taskState= MyConstants.TASK_STATE_SAVE;
    /**
     * 任务押金
     */
    private Double taskMoney;
    /**
     * 监督者人数
     */
    private Integer supervisorNum;
    /**
     * 退款金额
     */
    private Double refundMoney=0.0;
    /**
     * 能获得退款的最少打卡次数
     */
    private Integer checkTimes=0;

    /**
     * 已经打卡的次数
     */
    private Integer checkNum=0;
    /**
     * 打卡频率
     * 7位，对应周日至周六，哪一位为1，代表那一天需要打卡
     */
    private String checkFrec;
    /**
     * 已匹配的监督者的数目
     */
    private int matchNum;
    /**
     *  押金是否为试玩金额
     */
    private int ifTest;
    /**
     *  系统分成
     */
    private double systemBenifit;
    /**
     * 认证通过的次数
     */
    private int checkPass=0;
    /**
     * 最低认证通过率
     */
    private double minPass;
    /**
     * 实际认证完成率
     */
    private double realPass;
    /**
     * 每次打卡最小认证通过人数/比例
     */
    private double minCheck;
    /**
     * 最小认证类型
     */
    private String minCheckType;
    /**
     * 组团策略
     */
    private int supervisorType=0;
    /**
     * 是否考虑地域
     */
    private int ifArea=0;
    /**
     * 是否考虑爱好
     */
    private int ifHobby=0;

    /**
     *  任务创建时间
     */
    private String addTime;

    /**
     * 开始公示的时间
     */
    private String taskAnnounceTime;

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getTag4() {
        return tag4;
    }

    public void setTag4(String tag4) {
        this.tag4 = tag4;
    }

    public String getTag5() {
        return tag5;
    }

    public void setTag5(String tag5) {
        this.tag5 = tag5;
    }

    public String getTaskAnnounceTime() {
        return taskAnnounceTime;
    }

    public void setTaskAnnounceTime(String taskAnnounceTime) {
        this.taskAnnounceTime = taskAnnounceTime;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getMatchNum() {
        return matchNum;
    }

    public void setMatchNum(int matchNum) {
        this.matchNum = matchNum;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public Double getTaskMoney() {
        return taskMoney;
    }

    public void setTaskMoney(Double taskMoney) {
        this.taskMoney = taskMoney;
    }

    public Integer getSupervisorNum() {
        return supervisorNum;
    }

    public void setSupervisorNum(Integer supervisorNum) {
        this.supervisorNum = supervisorNum;
    }

    public Double getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(Double refundMoney) {
        this.refundMoney = refundMoney;
    }

    public Integer getCheckTimes() {
        return checkTimes;
    }

    public void setCheckTimes(Integer checkTimes) {
        this.checkTimes = checkTimes;
    }

    public String getCheckFrec() {
        return checkFrec;
    }

    public void setCheckFrec(String checkFrec) {
        this.checkFrec = checkFrec;
    }

    public Integer getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(Integer checkNum) {
        this.checkNum = checkNum;
    }

    public String getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent;
    }

    public int getIfTest() {
        return ifTest;
    }

    public void setIfTest(int ifTest) {
        this.ifTest = ifTest;
    }

    public double getSystemBenifit() {
        return systemBenifit;
    }

    public void setSystemBenifit(double systemBenifit) {
        this.systemBenifit = systemBenifit;
    }

    public int getCheckPass() {
        return checkPass;
    }

    public void setCheckPass(int checkPass) {
        this.checkPass = checkPass;
    }

    public double getMinPass() {
        return minPass;
    }

    public void setMinPass(double minPass) {
        this.minPass = minPass;
    }

    public double getRealPass() {
        return realPass;
    }

    public void setRealPass(double realPass) {
        this.realPass = realPass;
    }

    public double getMinCheck() {
        return minCheck;
    }

    public void setMinCheck(double minCheck) {
        this.minCheck = minCheck;
    }

    public String getMinCheckType() {
        return minCheckType;
    }

    public void setMinCheckType(String minCheckType) {
        this.minCheckType = minCheckType;
    }

    public int getSupervisorType() {
        return supervisorType;
    }

    public void setSupervisorType(int supervisorType) {
        this.supervisorType = supervisorType;
    }

    public int getIfArea() {
        return ifArea;
    }

    public void setIfArea(int ifArea) {
        this.ifArea = ifArea;
    }

    public int getIfHobby() {
        return ifHobby;
    }

    public void setIfHobby(int ifHobby) {
        this.ifHobby = ifHobby;
    }
}
