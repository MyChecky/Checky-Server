package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class TopicCount {
//    public TopicCount(String topicId, String countDate) {
//        this.topicId = topicId;
//        this.countDate = countDate;
//    }
//
//    public TopicCount() {
//    }
//
//    public TopicCount(String topicId, String countDate, int countNumber) {
//        this.topicId = topicId;
//        this.countDate = countDate;
//        this.countNumber = countNumber;
//    }

    private String topicId;

    private String countDate;

    private int countNumber = 0;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getCountDate() {
        return countDate;
    }

    public void setCountDate(String countDate) {
        this.countDate = countDate;
    }

    public int getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(int countNumber) {
        this.countNumber = countNumber;
    }
}
