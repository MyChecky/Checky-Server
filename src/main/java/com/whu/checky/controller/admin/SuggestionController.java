package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Suggestion;
import com.whu.checky.domain.TaskType;
import com.whu.checky.domain.User;
import com.whu.checky.service.SuggestionService;
import com.whu.checky.service.TaskTypeService;
import com.whu.checky.service.UserService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/suggestion")
@Component("AdminSuggestionController")
public class SuggestionController {
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private TaskTypeService taskTypeService;
    @Autowired
    private UserService userService;

    //展示动态
    @RequestMapping("/all")
    public JSONObject all(@RequestBody String jsonstr){
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        int currentPage = (Integer) object.get("page");
        Integer pageSize = (Integer) object.get("pageSize");
        if(pageSize == null){
            pageSize = 5;
        }
        Page<Suggestion> page=new Page<>(currentPage,pageSize);
        List<AdminSuggestion> adminSuggestions=new ArrayList<AdminSuggestion>();
        List<Suggestion> suggestions=suggestionService.displaySuggestions(page);
        for (int i = 0;i<suggestions.size();i++){
            Suggestion suggestion=suggestions.get(i);
            AdminSuggestion adminSuggestion=new AdminSuggestion();
            User user=userService.queryUser(suggestion.getUserId());
            adminSuggestion.setSuggestionContent(suggestion.getSuggestionContent());
            adminSuggestion.setSuggestionId(suggestion.getSuggestionId());
            adminSuggestion.setSuggestionTime(suggestion.getSuggestionTime());
            adminSuggestion.setUserId(user.getUserId());
            adminSuggestion.setUserName(user.getUserName());
            adminSuggestion.setSuggestionState(suggestion.getSuggestionState());

            adminSuggestions.add(adminSuggestion);
        }
        res.put("state", MyConstants.RESULT_OK);
        res.put("suggestions",adminSuggestions);
        res.put("size", (int)Math.ceil(page.getTotal() / (double)pageSize));
        res.put("total", page.getTotal());
        return res;
    }

    //否决建议
    @RequestMapping("/deny")
    public JSONObject deny(@RequestBody String jsonstr){
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String suggestionId=object.getString("suggestionId");
        Suggestion suggestion = suggestionService.QuerySuggestion(suggestionId);
        suggestion.setSuggestionState(MyConstants.SUGGESTION_STATE_DENY);
        if(suggestionService.updateSuggestionByKeyId(suggestion)==1) {
            res.put("state",MyConstants.RESULT_OK);
        }else {
            res.put("state",MyConstants.RESULT_FAIL);
        }
        return res;
    }

    //通过建议
    @RequestMapping("/pass")
    public JSONObject pass(@RequestBody String jsonstr){
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String suggestionId=object.getString("suggestionId");
        Suggestion suggestion = suggestionService.QuerySuggestion(suggestionId);
        suggestion.setSuggestionState(MyConstants.SUGGESTION_STATE_PASS);
        if(suggestionService.updateSuggestionByKeyId(suggestion)==1) {
            res.put("state",MyConstants.RESULT_OK);
        }else {
            res.put("state",MyConstants.RESULT_FAIL);
        }
        return res;
//        JSONObject res=new JSONObject();
//        JSONObject object= (JSONObject) JSON.parse(jsonstr);
//        String suggestionId=object.getString("suggestionId");
//        String typeContent=object.getString("typeContent");
//        TaskType taskType=new TaskType();
//        taskType.setTypeContent(typeContent);
//        taskType.setTypeId(UUID.randomUUID().toString());
//        if(suggestionService.deleteSuggestion(suggestionId)==1&&taskTypeService.addTaskType(taskType)==1) {
//            res.put("state",MyConstants.RESULT_OK);
//        }else {
//            res.put("state",MyConstants.RESULT_FAIL);
//        }
//        return res;
    }




    class AdminSuggestion{
        private String suggestionContent;
        private String suggestionId;
        private String suggestionTime;
        private String userId;
        private String userName;
        private String suggestionState;

        public String getSuggestionState() {
            return suggestionState;
        }

        public void setSuggestionState(String suggestionState) {
            this.suggestionState = suggestionState;
        }

        public String getSuggestionContent() {
            return suggestionContent;
        }

        public void setSuggestionContent(String suggestionContent) {
            this.suggestionContent = suggestionContent;
        }

        public String getSuggestionId() {
            return suggestionId;
        }

        public void setSuggestionId(String suggestionId) {
            this.suggestionId = suggestionId;
        }

        public String getSuggestionTime() {
            return suggestionTime;
        }

        public void setSuggestionTime(String suggestionTime) {
            this.suggestionTime = suggestionTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }


}
