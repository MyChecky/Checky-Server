package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.whu.checky.util.MyConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一次打卡对应的详细记录
 */
public class Record {
    @TableId
    private String recordId;

    private String checkId;

    private String essayId;
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
    private String recordTime = MyConstants.DATETIME_FORMAT.format(new Date());
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

    public String getEssayId() {
        return essayId;
    }

    public void setEssayId(String essayId) {
        this.essayId = essayId;
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
