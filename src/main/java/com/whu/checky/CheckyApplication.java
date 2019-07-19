package com.whu.checky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
public class CheckyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckyApplication.class, args);
    }





//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
//    {
//        return application.sources(CheckyApplication.class);
//    }
}
