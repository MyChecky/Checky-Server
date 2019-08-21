package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

public class Task {
    @TableId
    private String taskId;

    private String userId;

    @TableField(exist = false)
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String typeId;

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
     * success：任务完成    fail：任务未完成    during：任务进行中 nomatch 任务未建立匹配关系
     */
    private String taskState="nomatch";
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

    private int matchNum;

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
}
