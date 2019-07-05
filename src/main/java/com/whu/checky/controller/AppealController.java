package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.Appeal;
import com.whu.checky.service.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appeal")
public class AppealController {

    @Autowired
    private AppealService appealService;


    @PostMapping("/add")
    HashMap<String,String> addAppeal(@RequestBody String body){
        JSONObject data = JSONObject.parseObject(body);
        Appeal appeal = new Appeal();
        appeal.setAppealId(UUID.randomUUID().toString().substring(0,20));
        appeal.setUserId(data.getString("userId"));
        appeal.setTaskId(data.getString("taskId"));
        appeal.setCheckId(data.getString("checkId"));
        appeal.setAppealContent(data.getString("content"));

        HashMap<String,String> ans = new HashMap<>();
        if(appealService.addAppeal(appeal)){
            ans.put("state","ok");
        }else{
            ans.put("state","fail");
        }

        return ans;
    }

    void dealAppeal(){}

    @PostMapping("/display2User")
    String displayAppeal(@RequestBody String body){
        JSONObject data = JSON.parseObject(body);
        List<Appeal> ans = appealService.queryAppealFromUser(data.getString("userId"));
        JSONObject json = new JSONObject();
        if(ans==null) json.put("state","fail");
        else{
            json.put("state","ok");
            json.put("size",ans.size());
            json.put("appeals",ans);
        }
        return json.toJSONString();

    }

    @PostMapping("/del")
    HashMap<String,String> deleteAppeal(@RequestBody String body){
        HashMap<String,String> ans = new HashMap<>();
        try{
            JSONObject data = JSONObject.parseObject(body);
            if(appealService.deleteAppeal(data.getString("appealId")))
                ans.put("state","ok");
            else
                ans.put("state","fail");
        }catch (Exception e){
            e.printStackTrace();
            ans.put("state","fail");
        }
        return ans;

    }

}
