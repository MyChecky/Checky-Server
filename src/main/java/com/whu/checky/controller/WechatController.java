package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whu.checky.domain.User;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/wechat")
public class WechatController {

    private String wxspAppid="wx4b2f117b30cebf29";
    private String wxspSecret="0d5252f3d3f436db67909d50f2dbefd9";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public User login(@RequestBody String body) throws IOException {
        JSONObject object= JSONObject.parseObject(body);
        String code=(String)object.get("code");
        JSONObject userinfo= (JSONObject) object.get("userInfo");
        RestTemplate restTemplate = new RestTemplate();// 发送request请求
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" +code+"&grant_type=authorization_code";//参数
        String url = "https://api.weixin.qq.com/sns/jscode2session?"+params;// 微信接口 用于查询oponid
        String response = restTemplate.getForObject(url,String.class);



        JsonNode node = this.mapper.readTree(response);
        String openid = node.get("openid").asText();
        String sessionKey = node.get("session_key").asText();

        User user = new User();
        user.setUserId(openid);
        paserJson2User(userinfo,user);
        String skey = UUID.randomUUID().toString();
        user.setSessionId(skey);
        User check = userService.queryUser(user);
        boolean flag = false;
        if(check==null){
            flag = userService.register(user);
        }else{
            flag = userService.updateSessionID(check,sessionKey);
        }

        if(flag) return user;
        else return null;
    }



    private void paserJson2User(JSONObject userinfo,User user){
        user.setUserName((String) userinfo.get("nickName"));
        user.setUserGender((Integer) userinfo.get("gender"));
        user.setUserAvatar((String) userinfo.get("avatarUrl"));
    }



}
