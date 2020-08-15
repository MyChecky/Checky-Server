package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class Essay {
    @TableId
    private String essayId;

    private String userId;

//    private String essayTittle;

    private String essayContent;

    private String essayTime;
    /**
     * 点赞次数
     */
    private Integer likeNum=0;

    /**
     * 评论次数
     */
    private Integer commentNum=0;
    /**
     * 乐观锁字段
     */
    private Integer version;
//    /**
//     * 可能有关联的打卡记录
//     */
//    private String recordId="";
    /**
     * 经度
     */
    private String longtitude;
    /**
     * 纬度
     */
    private String latitude;
    private int ifDelete;

    public int getIfDelete() {
        return ifDelete;
    }

    public void setIfDelete(int ifDelete) {
        this.ifDelete = ifDelete;
    }

    public String getEssayId() {
        return essayId;
    }

    public void setEssayId(String essayId) {
        this.essayId = essayId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public String getEssayTittle() {
//        return essayTittle;
//    }
//
//    public void setEssayTittle(String essayTittle) {
//        this.essayTittle = essayTittle;
//    }

    public String getEssayContent() {
        return essayContent;
    }

    public void setEssayContent(String essayContent) {
        this.essayContent = essayContent;
    }

    public String getEssayTime() {
        return essayTime;
    }

    public void setEssayTime(String essayTime) {
        this.essayTime = essayTime;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

//    public String getRecordId() {
//        return recordId;
//    }
//
//    public void setRecordId(String recordId) {
//        this.recordId = recordId;
//    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
