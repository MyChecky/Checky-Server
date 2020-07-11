package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class AddApiTest {
    @TableId
    private String addApiId;

    private String addApiName;

    private String addApiContent;

    private String addApiTime;

    public String getAddApiId() {
        return addApiId;
    }

    public void setAddApiId(String addApiId) {
        this.addApiId = addApiId;
    }

    public String getAddApiName() {
        return addApiName;
    }

    public void setAddApiName(String addApiName) {
        this.addApiName = addApiName;
    }

    public String getAddApiContent() {
        return addApiContent;
    }

    public void setAddApiContent(String addApiContent) {
        this.addApiContent = addApiContent;
    }

    public String getAddApiTime() {
        return addApiTime;
    }

    public void setAddApiTime(String addApiTime) {
        this.addApiTime = addApiTime;
    }
}
