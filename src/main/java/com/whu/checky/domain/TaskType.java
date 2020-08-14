package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class TaskType {
    @TableId
    private String typeId;

    private String typeContent;

    private Long totalNum;

    private Long passNum;

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
