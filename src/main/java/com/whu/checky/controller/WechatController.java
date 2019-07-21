package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whu.checky.domain.User;
import com.whu.checky.service.RedisService;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
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

    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    public HashMap<String,String> login(@RequestBody String body) throws IOException {
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

        JSONObject location = (JSONObject) object.get("location");
        double latitude = Double.parseDouble(location.get("latitude").toString());
        double longitude = Double.parseDouble(location.get("longitude").toString());

        User user = new User();
        user.setUserId(openid);
        paserJson2User(userinfo,user);
        String skey = UUID.randomUUID().toString();
        user.setSessionId(skey);
        User check = userService.queryUser(openid);
        user.setLatitude(latitude);
        user.setLongtitude(longitude);
//        boolean flag = false;
        if(check==null){
            userService.register(user);
            check = user;
        }else{
            redisService.delSessionId(check.getSessionId());
            updateFromWeixin(check,user);
            userService.updateUser(check);
        }
        redisService.saveUserOrAdminBySessionId(skey,check);

        HashMap<String,String> ret = new HashMap<>();
//        ret.put("states",sessionKey);
        ret.put("states",openid);
        ret.put("sessionKey",skey);
//        ret.put("openId",openid);
        return ret;
    }



    private void paserJson2User(JSONObject userinfo,User user){
        user.setUserName((String) userinfo.get("nickName"));
        user.setUserGender((Integer) userinfo.get("gender"));
        user.setUserAvatar((String) userinfo.get("avatarUrl"));
    }


    private void updateFromWeixin (User check, User user){
        check.setUserName(user.getUserName());
        check.setUserGender(user.getUserGender());
        check.setUserAvatar(user.getUserAvatar());
        check.setLongtitude(user.getLongtitude());
        check.setLatitude(user.getLatitude());
        check.setSessionId(user.getSessionId());
    }


}
