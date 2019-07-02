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
    private AdministratorService administratorService;

    @PostMapping("/admin/register")
    public String register(@RequestBody String body){
        String message = "success";

        //构建Administrator
        Administrator administrator = paserJson2User(body);

        //处理登录
        try{
            int result = administratorService.register(administrator);
            if(result == 1)
                message = "duplicatename";
        }
        catch (Exception ex){
            ex.printStackTrace();
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
            int result = administratorService.login(administrator);
            if(result == 1)
                message = "missusername";
            if(result == 2)
                message = "wrongpassword";
        }
        catch (Exception ex){
            ex.printStackTrace();
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

