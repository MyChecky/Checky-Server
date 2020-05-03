package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.User;
import com.whu.checky.service.AppealService;
import com.whu.checky.service.CheckService;
import com.whu.checky.service.UserService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/appeal")
@Component("AdminAppealController")
public class AppealController {

    @Autowired
    private AppealService appealService;
    @Autowired
    private UserService userService;
    @Autowired
    private CheckService checkService;

    //查看所有的申訴
    @PostMapping("/all")
    public JSONObject all(@RequestBody String body) {
        JSONObject res = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(body);
        int currentPage = (Integer) object.get("page");
        Integer pageSize = JSON.parseObject(body).getInteger("pageSize");
        if (pageSize == null) {
            pageSize = 5;
        }
        Page<Appeal> page = new Page<Appeal>(currentPage, pageSize);
        List<AdminAppeal> adminAppeals = new ArrayList<AdminAppeal>();
        List<Appeal> appeals = appealService.displayAppeals(page);
        for (Appeal appeal : appeals) {
            AdminAppeal adminAppeal = new AdminAppeal();
            User user = userService.queryUser(appeal.getUserId());
            adminAppeal.setAppealContent(appeal.getAppealContent());
            adminAppeal.setAppealId(appeal.getAppealId());
            adminAppeal.setAppealTime(appeal.getAppealId());
            adminAppeal.setCheckId(appeal.getCheckId());
            adminAppeal.setTaskId(appeal.getTaskId());
            adminAppeal.setUserId(appeal.getUserId());
            adminAppeal.setUserName(user.getUserName());
            adminAppeal.setAppealState(appeal.getProcessResult());
            adminAppeals.add(adminAppeal);
        }
//        res.put("size", appealService.queryAllAppealNum());
        res.put("state", MyConstants.RESULT_OK);
        res.put("appeals", adminAppeals);
        res.put("size", (int) Math.ceil(page.getTotal() / (double) pageSize));
        res.put("total", page.getTotal());
        return res;
    }

    //处理申诉
    @PostMapping("/process")
    public JSONObject process(@RequestBody String body) {
        JSONObject res = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(body);
        int updateFlag = 0;

        String result = object.getString("result");
        if (result.equals(MyConstants.PROCESS_STATE_PASS) || result.equals(MyConstants.PROCESS_STATE_DENY)) {
            // 更新appeal
            Appeal appeal = appealService.getAppealById(object.getString("appealId"));
            appeal.setProcessResult(result);
            appeal.setProcessTime(MyConstants.DATETIME_FORMAT.format(new Date()));
            updateFlag += appealService.updateAppeal(appeal);
            // 更新check状态
            Check check = checkService.queryCheckById(appeal.getCheckId());
            check.setCheckState(result.equals(MyConstants.PROCESS_STATE_PASS) ?
                    MyConstants.CHECK_STATE_PASS : MyConstants.CHECK_STATE_DENY);
            updateFlag += checkService.updateCheck(check);
        }

        res.put("state", updateFlag == 2 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL);
        return res;
    }

    @RequestMapping("/query")
    public JSONObject query(@RequestBody String jsonstr) {
        JSONObject res = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String startTime = object.getString("startTime");
        startTime = startTime != null ? startTime : "1970-01-01";
        String endTime = object.getString("endTime");
        endTime = endTime != null ? endTime : "2999-01-01";

        String keyword = object.getString("keyword");
        String searchType = object.getString("searchType");
        Integer page = object.getInteger("page");
        Integer pageSize = object.getInteger("pageSize");
        Page<Appeal> p = new Page<>(page, pageSize);
        List<Appeal> appeals = new ArrayList<>();

        if (keyword == null || keyword.equals("")) {
            appeals = appealService.queryAppealsAll(p, startTime, endTime);
        } else if (searchType.equals("nickname")) {
            appeals = appealService.queryAppealsLikeNickname(p, startTime, endTime, keyword);
        } else if (searchType.equals("content")) {
            appeals = appealService.queryAppealsLikeContent(p, startTime, endTime, keyword);
        } else {
            res.put("state", MyConstants.RESULT_FAIL);
            return res;
        }
        List<AdminAppeal> adminAppeals = new ArrayList<AdminAppeal>();
        for (Appeal appeal : appeals) {
            AdminAppeal adminAppeal = new AdminAppeal();
            User user = userService.queryUser(appeal.getUserId());
            adminAppeal.setAppealContent(appeal.getAppealContent());
            adminAppeal.setAppealId(appeal.getAppealId());
            adminAppeal.setAppealTime(appeal.getAppealId());
            adminAppeal.setCheckId(appeal.getCheckId());
            adminAppeal.setTaskId(appeal.getTaskId());
            adminAppeal.setUserId(appeal.getUserId());
            adminAppeal.setUserName(user.getUserName());
            adminAppeal.setAppealState(appeal.getProcessResult());
            adminAppeals.add(adminAppeal);
        }
        res.put("state", MyConstants.RESULT_OK);
        res.put("appeals", appeals);
        res.put("size", (int) Math.ceil(p.getTotal() / (double) pageSize));
        res.put("total", p.getTotal());
        return res;
    }


    class AdminAppeal {
        private String appealContent;
        private String appealId;
        private String appealTime;
        private String checkId;
        private String taskId;
        private String userId;
        private String userName;
        private String appealState;

        public String getAppealState() {
            return appealState;
        }

        public void setAppealState(String appealState) {
            this.appealState = appealState;
        }

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
