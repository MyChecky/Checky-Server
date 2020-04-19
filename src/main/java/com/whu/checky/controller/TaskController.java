package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.*;
import com.whu.checky.service.*;
import com.whu.checky.util.Match;
import com.whu.checky.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    Match match;

    @Autowired
    private TaskService taskService;
    @Autowired
    private MoneyService moneyService;
    @Autowired
    private UserService userService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private TaskTypeService taskTypeService;
    @Autowired
    private TaskSupervisorService taskSupervisorService;

    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping("/initConfig")
    public HashMap<String, Object> initConfig(@RequestBody String body) {
        HashMap<String, Object> ret = new HashMap<>();
        JSONObject jsonObject = (JSONObject) JSON.parseObject(body);
//        String userId = jsonObject.getString("userId"); // 未来也许会用
        ret.put("minPass", parameterService.getValueByParam("task_lowest_pass").getParamValue());
        ret.put("minCheck", parameterService.getValueByParam("check_lowest_pass").getParamValue());

        String ymd = JSONObject.parseObject(body).getString("ymd");
        String state = Util.judgeDate(ymd) ? "ok" : "fail";
        ret.put("state", state);
        return ret;
    }

    @PostMapping("/taskDetail")
    public HashMap<String, Object> taskDetail(@RequestBody String body) {
        JSONObject object = (JSONObject) JSON.parse(body);
        HashMap<String, Object> ret = new HashMap<>();
        String taskId = object.getString("taskId");
        Task task = taskService.queryTask(taskId);
        ret.put("taskTitle", task.getTaskTitle());
        ret.put("taskState", task.getTaskState());
        if (task.getIfTest() == 1) {
            ret.put("taskMoneyType", "测试金额");
        } else {
            ret.put("taskMoneyType", "真实金额");
        }
        ret.put("checkTimes", task.getCheckNum() + "/" + task.getCheckTimes());
        ret.put("passTimes", task.getCheckPass() + "/" + task.getCheckNum());
        ret.put("taskDescribe", task.getTaskContent());
        // 金额与退还数
        if (task.getTaskState().equals("complete"))
            ret.put("taskMoneyState", "总:" + task.getTaskMoney() + "￥/返回:" + task.getRefundMoney() + "￥");
        else if (task.getTaskState().equals("during")) {
            ret.put("taskMoneyState", "总:" + task.getTaskMoney() + "￥");
        } else {
            ret.put("taskMoneyState", "总:" + task.getTaskMoney() + "￥/即将返回:" + task.getRefundMoney() + "￥");
        }
        // 任务类型
        ret.put("taskType", taskTypeService.QueryTaskType(task.getTypeId()).getTypeContent());
        // 监督者昵称
        List<TaskSupervisor> taskSupervisors = taskSupervisorService.getTasksSupByTaskId(taskId);
        List<String> sups = new ArrayList<>();
        for (TaskSupervisor taskSupervisor : taskSupervisors) {
            sups.add(userService.queryUser(taskSupervisor.getSupervisorId()).getUserName());
        }
        ret.put("taskSups", sups);
        ret.put("state", "ok");
        return ret;
    }

    //新建打卡
    @RequestMapping("/addTask")
    public HashMap<String, Object> addTask(@RequestBody String jsonstr) {
        Task task = JSON.parseObject(jsonstr, new TypeReference<Task>() {
        });
        if (task.getTaskId() == null || task.getTaskId().equals("")) {
            task.setTaskId(UUID.randomUUID().toString());
        } else {
            taskService.updateTask(task);
            return updateTaskAboutMoney(task);
        }
        int result = taskService.addTask(task);
        if (result == 0) {
            //添加失败
            HashMap<String, Object> ret = new HashMap<>();
            ret.put("state", "addTaskFail");
            return ret;
        } else {
            //添加成功
            return updateTaskAboutMoney(task);
        }
    }

    @RequestMapping("/queryPassPercentage")
    public HashMap<String, Object> queryPassPercentage(@RequestBody String body) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        Double passCheckDouble = Double.parseDouble(parameterService.getValueByParam("check_lowest_pass").getParamValue());
        Double passTaskDouble = Double.parseDouble(parameterService.getValueByParam("task_lowest_pass").getParamValue());
        DecimalFormat decimalFormat = new DecimalFormat("00%");
        String passCheck = decimalFormat.format(passCheckDouble);
        String passTask = decimalFormat.format(passTaskDouble);
        ret.put("passCheck", passCheck);
        ret.put("passTask", passTask);
        ret.put("state", "ok");
        return ret;
    }

    //查询属于某个用户的tasks
    @RequestMapping("/queryUserTasks")
    public List<Task> queryUserTasks(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        return taskService.queryUserTasks(userId, null);
    }

    //查询属于某个用户的tasks
    @RequestMapping("/queryDayUserTasks")
    public List<Task> queryDayUserTasks(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String date = (String) object.get("date");//？
        String userid = (String) object.get("userid");
        return taskService.queryUserTasks(userid, date);
    }

    //列举出所有的Tasks
    @RequestMapping("/listTasks")
    public List<Task> listTasks() {
        return taskService.listTasks();
    }

    //查询某个task
    @RequestMapping("/queryTask")
    public JSONObject queryTask(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String taskId = (String) object.get("taskId");
        Task task = taskService.queryTask(taskId);
        JSONObject res = new JSONObject();
        String ymd = object.getString("ymd");
        if(! Util.judgeDate(ymd)){
            res.put("state", "fail");
            return res;
        }

        if (task != null) {
            User user = userService.queryUser(task.getUserId());
            res.put("state", "OK");
            task.setUserName(user.getUserName());
            res.put("task", task);
            res.put("userName", user.getUserName());
            if (user.getUserAvatar().substring(0, 11).equals("/resources/")) {
                String baseIp = parameterService.getValueByParam("baseIp").getParamValue();
                res.put("userAvatar", baseIp + user.getUserAvatar());
            } else {
                res.put("userAvatar", user.getUserAvatar());
            }
            TaskType taskType = taskTypeService.QueryTaskType(task.getTypeId());
            res.put("typeContent", taskType.getTypeContent());
        } else {
            res.put("state", "FAIL");
        }
        return res;
    }

    //Task结束，结算状态
    @RequestMapping("/finishTask")
    public String finishTask(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String taskId = (String) object.get("taskId");
        Task task = taskService.queryTask(taskId);
        if (task != null) {
            String state = (String) object.get("state");
            task.setTaskState(state);
            taskService.updateTask(task);
            return "updateTaskStateSuccess";
        } else {
            return "updateTaskStateFail";
        }

    }

    @RequestMapping("/getDistribute")
    public HashMap<String, Double> getDistribute(@RequestBody String body) {
        JSONObject object = (JSONObject) JSON.parse(body);
        String taskId = (String) object.get("taskId");

        return taskService.distribute(taskService.queryTask(taskId));
    }

    //发布已经保存的打卡任务
//    @RequestMapping("/publicSavedTask")
//    public HashMap<String, Object> publicSavedTask(@RequestBody String jsonstr) {
//        Task tmp = JSON.parseObject(jsonstr, new TypeReference<Task>() {
//        });
//        Task task = taskService.queryTask(tmp.getTaskId());
//        return updateTaskAboutMoney(task);
//    }

    private HashMap<String, Object> updateTaskAboutMoney(Task task) {
        HashMap<String, Object> ret = new HashMap<>();
        // 判断余额是否充足
        User user = userService.queryUser(task.getUserId());
        if (task.getIfTest() == 1 && task.getTaskMoney() > user.getTestMoney()) {
            ret.put("state", "noEnoughTestMoney");
            return ret;
        } else if (task.getIfTest() == 0 && task.getTaskMoney() > user.getUserMoney()) {
            ret.put("state", "noEnoughUserMoney");
            return ret;
        }
        return ret;
    }
}

