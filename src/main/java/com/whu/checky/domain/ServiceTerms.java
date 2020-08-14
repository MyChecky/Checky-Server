package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;

public class ServiceTerms {
    @TableId
    private int serviceId;

    private String serviceContent;

    private String serviceTime;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }
}
