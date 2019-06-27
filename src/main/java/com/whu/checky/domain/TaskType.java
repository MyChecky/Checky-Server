package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class TaskType {
    @TableId
    private String typeId;

    private String typeContent;

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
