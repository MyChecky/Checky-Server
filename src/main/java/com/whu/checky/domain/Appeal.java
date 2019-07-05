package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 申诉记录，针对某次打卡
 */
public class Appeal {

    @TableId
    private String appealId;

    private String userId;

    private String taskId;

    private String checkId;
    /**
     * 申诉时间
     */
    private String appealTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    private String appealContent;
    /**
     * 处理结果
     */
    private String processResult;
    /**
     * 处理时间
     */
    private String processTime;

    public String getAppealId() {
        return appealId;
    }

    public void setAppealId(String appealId) {
        this.appealId = appealId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getAppealTime() {
        return appealTime;
    }

    public void setAppealTime(String appealTime) {
        this.appealTime = appealTime;
    }

    public String getAppealContent() {
        return appealContent;
    }

    public void setAppealContent(String appealContent) {
        this.appealContent = appealContent;
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }
}
