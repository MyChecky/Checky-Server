package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

import java.util.List;

public class TaskType {
    @TableId
    private String typeId;

    private String typeContent;

    private Long totalNum;

    private Long passNum;

    @TableField(exist = false)
    private List<Tag> tags;

    @TableField(exist = false)
    private int subTagsNum;

    public int getSubTagsNum() {
        return subTagsNum;
    }

    public void setSubTagsNum(int subTagsNum) {
        this.subTagsNum = subTagsNum;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void appendTag(List<Tag> tags) {
        this.tags.addAll(tags);
    }

    public void appendTag(Tag tag) {
        this.tags.add(tag);
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getPassNum() {
        return passNum;
    }

    public void setPassNum(Long passNum) {
        this.passNum = passNum;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent;
    }
}
