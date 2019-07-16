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
    private double benefit;
    /**
     * 监督次数
     */
    private Integer superviseNum;
    /**
     * 监督者被移除的时间
     */
    private String removeTime;


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
}
