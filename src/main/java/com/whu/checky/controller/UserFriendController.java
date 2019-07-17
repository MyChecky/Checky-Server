package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.UserFriend;
import com.whu.checky.service.AppealService;
import com.whu.checky.service.UserFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/userFriend")
public class UserFriendController {

    @Autowired
    private UserFriendService userFriendService;


    @PostMapping("/addUserFriend")
    public void addUserFriend(@RequestBody String jsonstr){
        UserFriend userFriend= JSON.parseObject(jsonstr,new TypeReference<UserFriend>(){});
        int res=userFriendService.addUserFriend(userFriend);
        if(res==1){
            //添加好友关系成功
        }else{

        }
    }



    @RequestMapping("/queryUserFriends")
    public List<UserFriend> queryUserFriends(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String userId= (String)object.get("userId");
        List<UserFriend> userFriends=userFriendService.queryUserFriends(userId);
        return userFriends;
    }

}
