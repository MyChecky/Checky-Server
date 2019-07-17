package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Essay;
import com.whu.checky.service.EssayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/essay")
public class EassyController {
    @Autowired
    private EssayService essayService;

    //发表动态
    public void addEassy(@RequestBody String jsonstr){
        Essay essay= JSON.parseObject(jsonstr,new TypeReference<Essay>(){});
        essay.setEssayId(UUID.randomUUID().toString());
        int result=essayService.addEssay(essay);
        if(result==1){
            //插入成功
        }else {
            //插入失败
        }
    }

    //删除动态
    public void delEassy(@RequestBody String jsonstr){
        String essayId= (String) JSON.parse(jsonstr);
        int result=essayService.deleteEssay(essayId);
        if(result==1){
            //删除成功
        }else {
            //删除失败
        }
    }

    //修改动态
    public void modifyEassy(@RequestBody String jsonstr) {
        Essay essay= JSON.parseObject(jsonstr,new TypeReference<Essay>(){});
        essay.setEssayId(UUID.randomUUID().toString());
        int result=essayService.modifyEssay(essay);
        if(result==1){
            //删除成功
        }else {
            //删除失败
        }

    }


    //查看自己的动态
    @RequestMapping("/queryUserEssays")
    public List<Essay> queryUserEssays(@RequestBody String jsonstr) {
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String userId= (String)object.get("userId");
        return essayService.queryUserEssays(userId);
    }


    //展示动态
    public List<Essay> displayEassy(){
        return essayService.displayEssay();
    }

    //点赞
    public void likeEssay(@RequestBody String jsonstr){


    }

    //评论
    public void commentEssay(@RequestBody String jsonstr){





    }



}
