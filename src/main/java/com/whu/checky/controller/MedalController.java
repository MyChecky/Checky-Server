package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.Medal;
import com.whu.checky.service.MedalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medal")
public class MedalController {
    @Autowired
    private MedalService medalService;

    @RequestMapping("/get")
    public Map<String,Medal> getAllMedal(@RequestBody String jsonstr) throws Exception {
        String userId = ((JSONObject) JSON.parse(jsonstr)).getString("userId");
        return medalService.getMedalsByUserId(userId);
    }
}
