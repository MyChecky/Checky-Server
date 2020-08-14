package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

/**
 * 表示监督关系的记录
 */
public class TaskSupervisor{
    @TableId
    private String taskId;
    @TableId
    private String supervisorId;
    /**
     * 开始监督的时间
     */
    private String addTime;
    /**
     * 监督者收益分成
     */
    private double benefit=0;
    /**
     * 监督次数
     */
    private Integer superviseNum=0;
    /**
     * 监督者被移除的时间
     */
    private String removeTime;
    /**
     * 离开原因
     */
    private String removeReason;
    /**
     * 被投诉次数
     */
    private int reportNum=0;
    /**
     * 投诉成功次数/不良记录数
     */
    private int badNum=0;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public double getBenefit() {
        return benefit;
    }

    public void setBenefit(double benefit) {
        this.benefit = benefit;
    }

    public Integer getSuperviseNum() {
        return superviseNum;
    }

    public void setSuperviseNum(Integer superviseNum) {
        this.superviseNum = superviseNum;
    }

    public String getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(String removeTime) {
        this.removeTime = removeTime;
    }

    public String getRemoveReason() {
        return removeReason;
    }

    public void setRemoveReason(String removeReason) {
        this.removeReason = removeReason;
    }

    public int getReportNum() {
        return reportNum;
    }

    public void setReportNum(int reportNum) {
        this.reportNum = reportNum;
    }

    public int getBadNum() {
        return badNum;
    }

    public void setBadNum(int badNum) {
        this.badNum = badNum;
    }
}
