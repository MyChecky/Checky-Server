package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

public class Tag {
    @TableId
    private String tagId;

    private String tagContent;

    private int TagCount;

    private int passCount;

    @TableField(exist = false)
    private String tagBelongedTypes;

    public String getTagBelongedTypes() {
        return tagBelongedTypes;
    }

    public void setTagBelongedTypes(String tagBelongedTypes) {
        this.tagBelongedTypes = tagBelongedTypes;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }

    public int getTagCount() {
        return TagCount;
    }

    public void setTagCount(int tagCount) {
        TagCount = tagCount;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }
}
