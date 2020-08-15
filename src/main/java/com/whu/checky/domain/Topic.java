package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class Topic {
    @TableId
    private String topicId;

    private String topicContent;

    //话题参与次数
    private int topicCount;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public int getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(int topicCount) {
        this.topicCount = topicCount;
    }
}
