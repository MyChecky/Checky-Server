package com.whu.checky.domain;

public class UserTag {
    private String userId;

    private String tagId;

    private int tagCount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }
}
