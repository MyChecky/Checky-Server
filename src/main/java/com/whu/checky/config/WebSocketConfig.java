package com.whu.checky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    // 使用内置tomcat（/调试）时，不注释；部署war包放到服务器的tomcat下时，注释掉
    // 评论区：https://blog.csdn.net/kxj19980524/article/details/88751114
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
