package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

/**
 * 举报记录，对象可以是动态、打卡、任务和监督人
 */
public class Report {
    @TableId
    private String reportId;
    /**
     * 举报人
     */
    private String userId;
    /**
     * 被举报任务
     */
    private String taskId;
    /**
     * 被举报打卡
     */
    private String checkId;
    /**
     * 被举报监督者
     */
    private String supervisorId;
    /**
     * 被举报动态
     */
    private String essayId;

    private String reportTime;

    private String reportContent;
    /**
     * 举报类型
     * 0：动态    1：打卡    2：任务    3：监督人
     */
    private String reportType;
    /**
     * 处理结果
     */
    private String processResult;
    /**
     * 处理时间
     */
    private String processTime;
    /**
     * 被举报人ID
     */
    private String userReportedID;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
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

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getEssayId() {
        return essayId;
    }

    public void setEssayId(String essayId) {
        this.essayId = essayId;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
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

    public String getUserReportedID() {
        return userReportedID;
    }

    public void setUserReportedID(String userReportedID) {
        this.userReportedID = userReportedID;
    }
}
