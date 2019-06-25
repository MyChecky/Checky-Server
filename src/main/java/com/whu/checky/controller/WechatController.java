package com.whu.checky.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whu.checky.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.UUID;

@RestController
public class WechatController {

    private String wxspAppid="";
    private String wxspSecret="";

    @Autowired
    ObjectMapper mapper;

    @PostMapping("/login")
    public User login(@RequestParam String code) throws IOException {
        RestTemplate restTemplate = new RestTemplate();// 发送request请求
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" +code;//参数
        String url = "https://api.weixin.qq.com/sns/jscode2session?"+params;// 微信接口 用于查询oponid
        String response = restTemplate.getForObject(url,String.class);



        JsonNode node = this.mapper.readTree(response);
        String openid = node.get("opendid").asText();
        String sessionKey = node.get("session_key").asText();


//        logger.info("response:"+response);
//        WeixinRespense weixinRespense = objectMapper.readValue(response,WeixinRespense.class); // 逆序列化 ，将字符串中的有效信息取出
//        String session_key = weixinRespense.getSession_key();//如果解密encryptData获取unionId，会用的到
//        openid= weixinRespense.getOpenid();//微信小程序 用户唯一标识
//        logger.info("session_key:"+session_key);
//        logger.info("openid:"+openid);

        // 注册用户，将查询到的oponid作为id
        User user = new User();
        String skey = UUID.randomUUID().toString();
        user.setSessionID(skey);
//        user.setUid(openid);
//        user.setBalence(Float.valueOf(0));
//        if (userService.existbyid(openid))
//        {
//            return userService.getUserById(openid);
//        }
//        else {
//            if (userService.signupUser(user) == 1)
//            {
//                return userService.getUserById(openid);
//            }
//            else {
//                return null;
//            }
//        }
        return user;
    }



}
