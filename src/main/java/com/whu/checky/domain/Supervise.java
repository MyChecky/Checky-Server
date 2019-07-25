package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class Supervise {
    @TableId
    private String superviseId;

    private String checkId;

    private String supervisorId;
    /**
     * 监督完成时间
     */
    private String superviseTime;

    private String superviseContent;
    /**
     * 监督状态
     * pass    deny
     */
    private String superviseState;

    public String getSuperviseId() {
        return superviseId;
    }

    public void setSuperviseId(String superviseId) {
        this.superviseId = superviseId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getSuperviseTime() {
        return superviseTime;
    }

    public void setSuperviseTime(String superviseTime) {
        this.superviseTime = superviseTime;
    }

    public String getSuperviseContent() {
        return superviseContent;
    }

    public void setSuperviseContent(String superviseContent) {
        this.superviseContent = superviseContent;
    }

    public String getSuperviseState() {
        return superviseState;
    }

    public void setSuperviseState(String superviseState) {
        this.superviseState = superviseState;
    }
}
