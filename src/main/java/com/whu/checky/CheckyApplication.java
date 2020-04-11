package com.whu.checky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
// 这个文件不改的话，添加war app一直不启动springboot这个项目，接口也是404
//public class CheckyApplication extends SpringBootServletInitializer {
public class CheckyApplication{
    public static void main(String[] args) {
        SpringApplication.run(CheckyApplication.class, args);
    }

    /*
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CheckyApplication.class);
    }
    */
}
