package com.whu.checky.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private UploadConfig uploadConfig;

//    private String path = "/resources/**";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println(uploadConfig.getStaticPath());
        System.out.println(uploadConfig.getUploadPath());
        registry.addResourceHandler(uploadConfig.getStaticPath()+"/**").addResourceLocations("file:"+uploadConfig.getUploadPath());
    }
}
