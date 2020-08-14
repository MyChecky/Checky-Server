package com.whu.checky.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ports")
public class PortsConfig {
    private int http;

    private int https;

    public int getHttp() {
        return http;
    }

    public void setHttp(int http) {
        this.http = http;
    }

    public int getHttps() {
        return https;
    }

    public void setHttps(int https) {
        this.https = https;
    }
}
