package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.User;
import com.whu.checky.service.MoneyService;
import com.whu.checky.service.TaskService;
import com.whu.checky.service.UserService;
import com.whu.checky.util.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

    //新建打卡
    @RequestMapping("/addTask")
    public String addTask(@RequestBody String jsonstr) {
        Task task = JSON.parseObject(jsonstr, new TypeReference<Task>() {
        });
        task.setTaskId(UUID.randomUUID().toString());
        int result = taskService.addTask(task);
        if (result == 0) {
            //添加失败
            return "addTaskFail";
        } else {
            //添加成功
            return updateTaskAboutMoney(task);
        }
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
            res.put("userAvatar", user.getUserAvatar());
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
        //微信支付相关
        User user = userService.queryUser(task.getUserId());
        if (task.getIfTest() == 1) {
            if (task.getTaskMoney() <= user.getTestMoney()) {
                user.setTestMoney(user.getTestMoney() - task.getTaskMoney());
                userService.updateUser(user);
            } else {
                return "noEnoughTestMoney";
            }
        } else if (task.getIfTest() == 0) {
            if (task.getTaskMoney() <= user.getUserMoney()) {
                user.setUserMoney(user.getUserMoney() - task.getTaskMoney());
                userService.updateUser(user);
            } else {
                return "noEnoughUserMoney";
            }
        }
        // 已扣款，更改任务状态
        try{
            if(match.matchSupervisorForOneTask(task)){
                task.setTaskState("during");
            }else{
                task.setTaskState("nomatch");
            }
        }catch (Exception ex){
            task.setTaskState("nomatch");
        }

        int updateResult = taskService.updateTask(task);
        if (updateResult == 0) {
            //出现异常，用户新建任务保存并付款后，未能更待任务匹配监督者的状态
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

