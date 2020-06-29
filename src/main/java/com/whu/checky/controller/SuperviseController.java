package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.*;
import com.whu.checky.service.*;
import com.whu.checky.util.MyConstants;
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
    @Autowired
    private TaskSupervisorService taskSupervisorService;
    @Autowired
    private TaskTypeService taskTypeService;

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
            String state = supervise.getSuperviseState().equals(MyConstants.SUPERVISE_STATE_PASS) ? "通过" : "拒绝";
            String userName = userService.queryUser(check.getUserId()).getUserName();
            Task task = taskService.queryTask(check.getTaskId());
            String taskType = taskTypeService.queryTaskType(task.getTypeId()).getTypeContent();
//            String avatar = userService.queryUser(check.getUserId()).getUserAvatar();
            supHistories.add(new SupHistory(supervise.getSuperviseId(), supervise.getCheckId(),
                    check.getTaskId(), task.getTaskTitle(), state, /*avatar,*/ check.getCheckTime(),
                    supervise.getSuperviseTime(), userName, taskType, check.getCheckState()));
        }
        ret.put("supList", supHistories);
        ret.put("state", MyConstants.RESULT_OK);
        return ret;
    }

    //监督者对一个Check进行验证
    @RequestMapping("/addSupervise")
    public HashMap<String, Object> addSupervise(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>();
        Supervise supervise = JSON.parseObject(jsonstr, new TypeReference<Supervise>() {
        });
        int stateFlag = 0;
        supervise.setSuperviseId(UUID.randomUUID().toString());
        stateFlag += superviseService.addSupervise(supervise) == 1 ? 1 : 0;
        // check表 SUPERVISE_NUM+=1; pass_num+= 0/1
        String checkId = supervise.getCheckId();
        Check check = checkService.queryCheckById(checkId);
        check.setSuperviseNum(check.getSuperviseNum() + 1);
        if (supervise.getSuperviseState().equals(MyConstants.SUPERVISE_STATE_PASS)) {
            check.setPassNum(check.getPassNum() + 1);
        }
        stateFlag += checkService.updateCheck(check) == 1 ? 1 : 0;
        // task_sup表 SUPERVISE_NUM+=1
        List<TaskSupervisor> taskSupervisors = taskSupervisorService.getTasksSupByTaskId(check.getTaskId());
        for (TaskSupervisor taskSupervisor : taskSupervisors) {
            if (taskSupervisor.getSupervisorId().equals(supervise.getSupervisorId())) {
                taskSupervisor.setSuperviseNum(taskSupervisor.getSuperviseNum() + 1);
                stateFlag += taskSupervisorService.updateTaskSup(taskSupervisor) == 1 ? 1 : 0;
            }
        }
        // user表 SUPERVISE_NUM+=1
        User user = userService.queryUser(supervise.getSupervisorId());
        user.setSuperviseNum(user.getSuperviseNum() + 1);
        stateFlag += userService.updateUser(user) == 1 ? 1 : 0;

        String state = stateFlag == 4 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
        ret.put("state", state);
        return ret;
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
        superviseService.updateSupervise(superviseId, MyConstants.TASK_STATE_SUCCESS);
    }

    //提供给管理员修改状态的
    @RequestMapping("/modifySuperviseToFail")
    public void modifySuperviseToFail(@RequestBody String jsonstr) {
        String superviseId = (String) JSON.parse(jsonstr);
        superviseService.updateSupervise(superviseId, MyConstants.SUPERVISE_STATE_DENY); // 这里应该有问题，看impl
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
    private String checkState;
    private String title;
    private String state;
//    private String avatar;
    private String taskId;
    private String checkName;
    private String checkTime;
    private String supTime;
    private String taskType;

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getSupTime() {
        return supTime;
    }

    public SupHistory(String supId, String checkId, String taskId, String title,
                      String state, /*String avatar,*/ String checkTime, String supTime,
                      String checkName, String taskType, String checkState) {
        this.supId = supId;
        this.checkId = checkId;
        this.title = title;
        this.state = state;
//        this.avatar = avatar;
        this.taskId = taskId;
        this.checkTime = checkTime;
        this.supTime = supTime;
        this.checkName = checkName;
        this.taskType = taskType;
        this.checkState = checkState;
    }

    public void setSupTime(String supTime) {
        this.supTime = supTime;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

//    public String getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(String avatar) {
//        this.avatar = avatar;
//    }

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
