package com.whu.checky.controller;

import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.Administrator;
import com.whu.checky.mapper.AdministratorMapper;
import com.whu.checky.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @Autowired
    private AdministratorMapper mapper;

    @Autowired
    private AdministratorService administratorService;

    @PostMapping("/Admin/register")
    public String register(@RequestBody String body){
        String message = "success";

        //构建Administrator
        Administrator administrator = paserJson2User(body);

        //处理登录
        try{
            administratorService.register(administrator);
        }
        catch (Exception ex){
            if(ex.getMessage().equals("已存在的用户名"))
                message = "duplicatename";
        }

        //
        return message;
    }
    @PostMapping("/admin/login")
    public String login(@RequestBody String body) {
        String message = "success";

        //构建Administrator
        Administrator administrator = paserJson2User(body);

        //处理登录
        try{
            administratorService.login(administrator);
        }
        catch (Exception ex){
            if(ex.getMessage().equals("不存在的用户名"))
                message = "missusername";
            if(ex.getMessage().equals("密码错误"))
                message = "wrongpassword";
        }

        //
        return message;
    }



    private Administrator paserJson2User(String body){
        //解析json获取登录信息
        JSONObject object = JSONObject.parseObject(body);
        String name = object.getString("userName");
        String password = object.getString("userPassword");

        //构造登录用户
        Administrator administrator = new Administrator();
        administrator.setUserName(name);
        administrator.setUserPassword(password);

        return administrator;
    }

}

