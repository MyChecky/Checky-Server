package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.Administrator;
import com.whu.checky.mapper.AdministratorMapper;
import com.whu.checky.service.AdministratorService;
import com.whu.checky.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/register")
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
    @PostMapping("/login")
    public HashMap<String,String> login(@RequestBody String body) {
        String message = "success";
        HashMap<String,String> ans = new HashMap<>();

        //构建Administrator
        Administrator administrator = paserJson2User(body);
        String sessionKey = UUID.randomUUID().toString();
        //处理登录
        try{
            int result = administratorService.login(administrator);
            if(result == 1)
                message = "missusername";
            if(result == 2)
                message = "wrongpassword";
            else{
                redisService.saveUserOrAdminBySessionId(sessionKey,administrator);
                ans.put("sessionKey",sessionKey);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        //
        ans.put("state",message);
        return ans;
    }


    @PostMapping("logout")
    public HashMap<String,String> logout(@RequestBody String body){
        HashMap<String,String> ans = new HashMap<>();
        try {
            JSONObject data = JSON.parseObject(body);
            String sessionKey = data.getString("sessionKey");
            redisService.delSessionId(sessionKey);
            ans.put("state","ok");
        }catch (Exception e){
            ans.put("state","fail");
        }

        return ans;
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

