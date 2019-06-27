package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Suggestion;
import com.whu.checky.domain.TaskType;
import com.whu.checky.service.SuggestionService;
import com.whu.checky.service.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/Suggestion")
public class SuggestionController {
    //管理端用
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private TaskTypeService taskTypeService;


    //管理端用于新添类型的
    @PostMapping("/addSuggestion")
    public String addSuggestion(@RequestBody String jsonstr){
        Suggestion suggestion= JSON.parseObject(jsonstr,new TypeReference<Suggestion>(){});
        int result=suggestionService.addSuggestion(suggestion);
        if(result==0){
            //添加失败
            return "addSuggestion Fail";
        }else {
            //添加成功
            return "addSuggestion Success";
        }

    }


    //管理端用于处理Sussestion
    @PostMapping("/dealSuggestion")
    public String dealSuggestion(@RequestBody String jsonstr){
        //改变这个suggestion的state
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String state= (String) object.get("state");
        if(state=="pass"){
            //审核通过，新添类型
            TaskType taskType= JSON.parseObject((String) object.get("taskType"),new TypeReference<TaskType>(){});
            int addTyperesult=taskTypeService.addTaskType(taskType);
            int updateSuggesionState=suggestionService.updataSuggestion((String) object.get("suggesionid"),"pass");
            if(addTyperesult==1&&updateSuggesionState==1)
                return "success";
        }else {
            //审核未通过
            int updateSuggesionState=suggestionService.updataSuggestion((String) object.get("suggesionid"),"fail");
            return "updateToFailfail";
        }

        return "fail";
    }

    @RequestMapping("/listSuggestion")
    public List<Suggestion> ListSuggesion(){
        return suggestionService.ListSuggestion();
    }



}
