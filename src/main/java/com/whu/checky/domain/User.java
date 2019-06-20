package com.whu.checky.domain;

import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import java.sql.Date;

public class User {
    //用户id
    @Id
    @GeneratedValue
    private int userid;
    //用户微信名
    @Column(nullable = false)
    private String nickname;
    //用户微信头像url
    @Column(nullable = false)
    private String avatar;
    //用户性别
    @Column(nullable = false)
    private String gender;
    //用户使用小程序的时间
    @Column(nullable = false)
    private Date signindate;
    //地理位置

}
