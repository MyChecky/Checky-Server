package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.User;
import com.whu.checky.service.AppealService;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin/appeal")
public class AppealController {

    @Autowired
    private AppealService appealService;
    @Autowired
    private UserService userService;

    @PostMapping("/all")
    public List<AdminAppeal> all(@RequestBody String body) {
        JSONObject object= (JSONObject) JSON.parse(body);
        int currentPage=(Integer) object.get("Page");
        Page<Appeal> page=new Page<>(currentPage,5);
        List<AdminAppeal> res=new ArrayList<AdminAppeal>();
        List<Appeal> appeals=appealService.displayAppeal(page);
        for (Appeal appeal:appeals){
            AdminAppeal adminAppeal=new AdminAppeal();
            User user=userService.queryUser(appeal.getUserId());
            adminAppeal.setAppealContent(adminAppeal.getAppealContent());
            adminAppeal.setAppealId(adminAppeal.getAppealId());
            adminAppeal.setAppealTime(adminAppeal.getAppealId());
            adminAppeal.setCheckId(adminAppeal.getCheckId());
            adminAppeal.setTaskId(adminAppeal.getTaskId());
            adminAppeal.setUserId(adminAppeal.getUserId());
            adminAppeal.setUserName(user.getUserName());
            res.add(adminAppeal);
        }
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
