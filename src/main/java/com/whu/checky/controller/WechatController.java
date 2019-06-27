package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whu.checky.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.UUID;

@RestController
public class WechatController {

    private String wxspAppid="wx4b2f117b30cebf29";
    private String wxspSecret="0d5252f3d3f436db67909d50f2dbefd9";

    @Autowired
    ObjectMapper mapper;

    @PostMapping("/login")
    @ResponseBody
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


//        logger.info("response:"+response);
//        WeixinRespense weixinRespense = objectMapper.readValue(response,WeixinRespense.class); // 逆序列化 ，将字符串中的有效信息取出
//        String session_key = weixinRespense.getSession_key();//如果解密encryptData获取unionId，会用的到
//        openid= weixinRespense.getOpenid();//微信小程序 用户唯一标识
//        logger.info("session_key:"+session_key);
//        logger.info("openid:"+openid);

        // 注册用户，将查询到的oponid作为id
        User user = new User();
        user.setUserId(openid);
        paserJson2User(userinfo,user);
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



    public void paserJson2User(JSONObject userinfo,User user){
        user.setUserId((String) userinfo.get("nickName"));
        user.setGender((Integer) userinfo.get("gender"));
        user.setUserAvatar((String) userinfo.get("avatarUrl"));
    }



}
