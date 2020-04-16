package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Supervise;
import com.whu.checky.domain.SupervisorState;
import com.whu.checky.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/supervise")
public class SuperviseController {

    @Autowired
    private SuperviseService superviseService;
    @Autowired
    private CheckService checkService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;

    @PostMapping("/history")
    public HashMap<String, Object> history(@RequestBody String body) {
        HashMap<String, Object> ret = new HashMap<>();
        JSONObject jsonObject = (JSONObject) JSON.parseObject(body);
        String userId = jsonObject.getString("userId");
        Integer pageRequest = jsonObject.getInteger("pageRequest");
        Integer pageSize = jsonObject.getInteger("pageSize");
        List<Supervise> supervises =
                superviseService.getCheckSupsBySupId(new Page<Supervise>(pageRequest, pageSize), userId);

        List<SupHistory> supHistories = new ArrayList<>();
        for (Supervise supervise : supervises) {
            Check check = checkService.queryCheckById(supervise.getCheckId());
            String title = taskService.getTitleById(check.getTaskId());
            String state = supervise.getSuperviseState().equals("pass") ? "通过" : "拒绝";
            String avatar = userService.queryUser(check.getUserId()).getUserAvatar();
            supHistories.add(new SupHistory(supervise.getSuperviseId(), supervise.getCheckId(),
                    check.getTaskId(), title, state, avatar));
        }
        ret.put("supList", supHistories);
        ret.put("state", "ok");
        return ret;
    }

    //监督者对一个Check进行验证
    @RequestMapping("/addSupervise")
    public void addSupervise(@RequestBody String jsonstr) {
        Supervise supervise = JSON.parseObject(jsonstr, new TypeReference<Supervise>() {
        });
        supervise.setSuperviseId(UUID.randomUUID().toString());
        superviseService.addSupervise(supervise);
        String checkId = supervise.getCheckId();
        if (supervise.getSuperviseState().equals("pass")) {
            //如果通过,对应check的supervise_num+1
            //同时pass_num也+1
            checkService.updatePassSuperviseCheck(checkId);
        } else {
            //如果没有通过，那么只有check的supervise_num+1
            checkService.updateDenySuperviseCheck(checkId);
        }


    }


    //还需等待认证的（需要设置一个有效期之类的东西）
    @RequestMapping("/needToSupervise")
    public List<Check> needToSupervise(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String supervisorId = (String) object.get("userId");
        String startDate = (String) object.get("startDate");
        String endDate = (String) object.get("endDate");
        return superviseService.userNeedToSupervise(supervisorId, startDate, endDate);

    }

    //查询属于某个用户的所有Supervise
    @RequestMapping("/queryUserAllSupervise")
    public List<Supervise> queryUserAllSupervise(@RequestBody String jsonstr) {
        String userid = (String) JSON.parse(jsonstr);
        return superviseService.queryUserSupervise(userid, null);
    }

    //查询属于某个用户的关于某个check的所有Supervise
    @RequestMapping("/queryUserCheckSupervise")
    public List<Supervise> queryUserCheckSupervise(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        String checkId = (String) object.get("checkId");
        return superviseService.queryUserSupervise(userId, checkId);
    }

    //查询具体的某一个Supervise
    @RequestMapping("/querySupervise")
    public Supervise querySupervise(@RequestBody String jsonstr) {
        String superviseId = (String) JSON.parse(jsonstr);
        return superviseService.querySupervise(superviseId);
    }


    //提供给管理员修改状态的
    @RequestMapping("/modifySuperviseToSuccess")
    public void modifySuperviseToSuccess(@RequestBody String jsonstr) {
        String superviseId = (String) JSON.parse(jsonstr);
        superviseService.updateSupervise(superviseId, "Success");
    }

    //提供给管理员修改状态的
    @RequestMapping("/modifySuperviseToFail")
    public void modifySuperviseToFail(@RequestBody String jsonstr) {
        String superviseId = (String) JSON.parse(jsonstr);
        superviseService.updateSupervise(superviseId, "Fail");
    }


    @RequestMapping("/querySupervisorState")
    public List<SupervisorState> querySuperviseState(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String taskId = (String) object.get("taskId");
        String checkId = (String) object.get("checkId");
        return superviseService.querySuperviseState(taskId, checkId);
    }

}

class SupHistory {
    private String supId;
    private String checkId;
    private String title;
    private String state;
    private String avatar;
    private String taskId;

    public SupHistory(String supId, String checkId, String taskId, String title, String state, String avatar) {
        this.supId = supId;
        this.checkId = checkId;
        this.title = title;
        this.state = state;
        this.avatar = avatar;
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
