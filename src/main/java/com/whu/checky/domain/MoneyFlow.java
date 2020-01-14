package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("moneyflow")
public class MoneyFlow {
    @TableId
    private String flowId;
    private String userID;

    @TableField(exist = false)
    private String userName = "";
    /**
     * 是否试玩金额
     */
    private int ifTest;
    /**
     * 进账出账，针对用户！
     */
    private String flowIO;
    private Double flowMoney;
    /**
     * 流动类型
     */
    private String flowType;
    private String flowTime;
    private String taskId;
    /**
     * 补充说明
     */
    private String remark;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIfTest() {
        return ifTest;
    }

    public void setIfTest(int ifTest) {
        this.ifTest = ifTest;
    }

    public String getFlowIO() {
        return flowIO;
    }

    public void setFlowIO(String flowIO) {
        this.flowIO = flowIO;
    }

    public Double getFlowMoney() {
        return flowMoney;
    }

    public void setFlowMoney(Double flowMoney) {
        this.flowMoney = flowMoney;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String floeType) {
        this.flowType = floeType;
    }

    public String getFlowTime() {
        return flowTime;
    }

    public void setFlowTime(String flowTime) {
        this.flowTime = flowTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
