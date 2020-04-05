package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.*;
import com.whu.checky.service.*;
import com.whu.checky.util.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

    //新建打卡
    @RequestMapping("/addTask")
    public String addTask(@RequestBody String jsonstr) {
        Task task = JSON.parseObject(jsonstr, new TypeReference<Task>() {
        });
        if(task.getTaskId() == null || task.getTaskId().equals("")){
            task.setTaskId(UUID.randomUUID().toString());
        }else{
            taskService.updateTask(task);
            return updateTaskAboutMoney(task);
        }
        int result = taskService.addTask(task);
        if (result == 0) {
            //添加失败
            return "addTaskFail";
        } else {
            //添加成功
            return updateTaskAboutMoney(task);
        }
    }

    @RequestMapping("/queryPassPercentage")
    public HashMap<String, Object> queryPassPercentage(@RequestBody String body){
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
        if (task != null) {
            User user = userService.queryUser(task.getUserId());
            res.put("state", "OK");
            task.setUserName(user.getUserName());
            res.put("task", task);
            res.put("userName", user.getUserName());
            if(user.getUserAvatar().substring(0,11).equals("/resources/")) {
                String baseIp = parameterService.getValueByParam("baseIp").getParamValue();
                res.put("userAvatar", baseIp + user.getUserAvatar());
            }else{
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

        return taskService.distribute(taskId);
    }

    @RequestMapping("/getIfHighSetting")
    public int getIfHighSetting(@RequestBody String jsonstr) {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        return 0;//1允许，0不允许进行高级设置（指的是总任务与单词打卡通过率及监督者类型,地域，爱好等）
    }

    //发布已经保存的打卡任务
    @RequestMapping("/publicSavedTask")
    public String publicSavedTask(@RequestBody String jsonstr) {
        Task tmp = JSON.parseObject(jsonstr, new TypeReference<Task>() {
        });
        Task task = taskService.queryTask(tmp.getTaskId());
        return updateTaskAboutMoney(task);
    }

    //进行余额判断，更新余额，更新流水记录
    private String updateTaskAboutMoney(Task task) {
        // 判断余额是否充足；这里余额不足会导致task的save状态
        User user = userService.queryUser(task.getUserId());
        if(task.getIfTest() == 1 && task.getTaskMoney() > user.getTestMoney()){
            return "noEnoughTestMoney";
        }else if(task.getIfTest() == 0 && task.getTaskMoney() > user.getUserMoney()){
            return "noEnoughUserMoney";
        }
        // 监督者匹配以及扣款相关
        try{
            if(match.matchSupervisorForOneTask(task)){ // 匹配监督者成功，进行扣款
                if (task.getIfTest() == 1){
                    user.setTestMoney(user.getTestMoney() - task.getTaskMoney());
                    userService.updateUser(user);
                }else if(task.getIfTest() == 0){
                    user.setUserMoney(user.getUserMoney() - task.getTaskMoney());
                    userService.updateUser(user);
                }
                task.setTaskState("during");
                taskService.updateTask(task);
            }else{ // 匹配监督者失败，保存任务为未匹配状态，用户可继续修改任务
                task.setTaskState("nomatch");
                taskService.updateTask(task);
                return "noEnoughSupervisor";
            }
        }catch (Exception ex){
            task.setTaskState("nomatch");
            taskService.updateTask(task);
            return ("matchSupervisorError");
        }
        // 已扣款且已更改任务状态，在Money表当中插入一条用户交付押金的记录
        MoneyFlow moneyFlow = new MoneyFlow();
        moneyFlow.setUserID(task.getUserId());
        moneyFlow.setIfTest(task.getIfTest());
        moneyFlow.setFlowIo("O");//阿拉伯字母大写O
        moneyFlow.setFlowType("pay");
        moneyFlow.setFlowMoney(task.getTaskMoney());
        moneyFlow.setTaskId(task.getTaskId());
        moneyFlow.setFlowTime(ft.format(new Date()));
        moneyFlow.setFlowId(UUID.randomUUID().toString());
        int addMoneyRes = moneyService.addTestMoneyRecord(moneyFlow);
        if (addMoneyRes == 1) {
            return "addTaskSuccess";
        } else {
            return "insertMoneyFlowError";
        }
    }
}

