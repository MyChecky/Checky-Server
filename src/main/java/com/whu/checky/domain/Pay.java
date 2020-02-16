package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("pay")
public class Pay {
    @TableId
    private String payId;
    private String payOrderinfo;
    private String payUserid;

    @TableField(exist = false)
    private String payUserName = "";

    private String payType;
    private String payTime;
    private double payMoney;
    private String payState;

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payID) {
        this.payId = payID;
    }

    public String getPayOrderinfo() {
        return payOrderinfo;
    }

    public void setPayOrderinfo(String payOrderInfo) {
        this.payOrderinfo = payOrderInfo;
    }

    public String getPayUserid() {
        return payUserid;
    }

    public void setPayUserid(String payUserid) {
        this.payUserid = payUserid;
    }

    public String getPayUserName() {
        return payUserName;
    }

    public void setPayUserName(String payUserName) {
        this.payUserName = payUserName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }
}
