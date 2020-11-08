package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class TaskTag {
    @TableId
    private String taskId;

    @TableId
    private String tagId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
