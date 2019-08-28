package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.User;
import com.whu.checky.service.AppealService;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/appeal")
@Component("AdminAppealController")
public class AppealController {

    @Autowired
    private AppealService appealService;
    @Autowired
    private UserService userService;

    //查看所有的申訴
    @PostMapping("/all")
    public JSONObject all(@RequestBody String body) {
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(body);
        int currentPage = (Integer) object.get("page");
        Page<Appeal> page=new Page<>(currentPage,5);
        List<AdminAppeal> adminAppeals=new ArrayList<AdminAppeal>();
        List<Appeal> appeals=appealService.displayAppeals(page);
        for (Appeal appeal:appeals){
            AdminAppeal adminAppeal=new AdminAppeal();
            User user=userService.queryUser(appeal.getUserId());
            adminAppeal.setAppealContent(appeal.getAppealContent());
            adminAppeal.setAppealId(appeal.getAppealId());
            adminAppeal.setAppealTime(appeal.getAppealId());
            adminAppeal.setCheckId(appeal.getCheckId());
            adminAppeal.setTaskId(appeal.getTaskId());
            adminAppeal.setUserId(appeal.getUserId());
            adminAppeal.setUserName(user.getUserName());
            adminAppeals.add(adminAppeal);
        }
        res.put("state","ok");
        res.put("appeals",adminAppeals);
        return res;
    }


    class AdminAppeal{
        private String appealContent;
        private String appealId;
        private String appealTime;
        private String checkId;
        private String taskId;
        private String userId;
        private String userName;

        public String getAppealContent() {
            return appealContent;
        }

        public void setAppealContent(String appealContent) {
            this.appealContent = appealContent;
        }

        public String getAppealId() {
            return appealId;
        }

        public void setAppealId(String appealId) {
            this.appealId = appealId;
        }

        public String getAppealTime() {
            return appealTime;
        }

        public void setAppealTime(String appealTime) {
            this.appealTime = appealTime;
        }

        public String getCheckId() {
            return checkId;
        }

        public void setCheckId(String checkId) {
            this.checkId = checkId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
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
