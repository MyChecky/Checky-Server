package com.whu.checky.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一次打卡对应的详细记录
 */
public class Record {
    private String recordId;

    private String checkId;
    /**
     * 记录类型
     * image    sound    video
     */
    private String recordType;
    /**
     * 文件地址
     */
    private String fileAddr;
    /**
     * 记录时间
     */
    private String recordTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    /**
     * 记录内容
     */
    private String recordContent;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getFileAddr() {
        return fileAddr;
    }

    public void setFileAddr(String fileAddr) {
        this.fileAddr = fileAddr;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getRecordContent() {
        return recordContent;
    }

    public void setRecordContent(String recordContent) {
        this.recordContent = recordContent;
    }
}
