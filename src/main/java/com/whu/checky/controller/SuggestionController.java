package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Suggestion;
import com.whu.checky.domain.TaskType;
import com.whu.checky.service.SuggestionService;
import com.whu.checky.service.TaskTypeService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/suggestion")
public class SuggestionController {
    //管理端用
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private TaskTypeService taskTypeService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //管理端用于新添类型的
    @PostMapping("/addSuggestion")
    public String addSuggestion(@RequestBody String jsonstr){
        Suggestion suggestion= JSON.parseObject(jsonstr,new TypeReference<Suggestion>(){});
        suggestion.setSuggestionId(UUID.randomUUID().toString());
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
        if(state.equals("pass")){
            //审核通过，新添类型
            TaskType taskType= JSON.parseObject((String) object.get("taskType"),new TypeReference<TaskType>(){});
            int addTyperesult=taskTypeService.addTaskType(taskType);
            int updateSuggesionState=suggestionService.updataSuggestion((String) object.get("suggesionid"),"pass");
            if(addTyperesult==1&&updateSuggesionState==1)
                return MyConstants.RESULT_OK;
        }else {
            //审核未通过
            int updateSuggesionState=suggestionService.updataSuggestion((String) object.get("suggesionid"),"fail");
            return "updateToFailfail";
        }

        return MyConstants.RESULT_FAIL;
    }

    @RequestMapping("/listSuggestion")
    public List<Suggestion> ListSuggesion(){
        return suggestionService.ListSuggestion();
    }

    @RequestMapping("/userSuggest")
    public JSONObject userSuggest(@RequestBody String jsonstr){
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        String suggestionContent = (String) object.get("suggestionContent");
        Suggestion suggestion = new Suggestion();
        suggestion.setSuggestionId(UUID.randomUUID().toString());
        suggestion.setUserId(userId);
        suggestion.setSuggestionContent(suggestionContent);
        suggestion.setSuggestionState("waiting");
        suggestion.setSuggestionTime(dateFormat.format(new Date()));
        int result = suggestionService.addSuggestion(suggestion);

        JSONObject ans = new JSONObject();
        if (result == 1) {
            ans.put("state", MyConstants.RESULT_OK);
        } else {
            ans.put("state", MyConstants.RESULT_FAIL); // 插入失败
        }
        return ans;
    }

}
