package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.service.AppealService;
import com.whu.checky.service.TaskService;
import com.whu.checky.service.TaskSupervisorService;
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
@RequestMapping("/appeal")
public class AppealController {

    @Autowired
    private AppealService appealService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskSupervisorService taskSupervisorService;

    @PostMapping("/add")
    HashMap<String,String> addAppeal(@RequestBody String body){
        JSONObject data = JSONObject.parseObject(body);

        String taskId = data.getString("taskId");
        Appeal appeal = new Appeal();
        appeal.setAppealId(UUID.randomUUID().toString());
        appeal.setUserId(data.getString("userId"));
        appeal.setTaskId(taskId);
        appeal.setCheckId(data.getString("checkId"));
        appeal.setAppealContent(data.getString("appealContent"));

        HashMap<String,String> ans = new HashMap<>();
        if(appealService.addAppeal(appeal)){
            ans.put("state", MyConstants.RESULT_OK);
        }else{
            ans.put("state",MyConstants.RESULT_FAIL);
        }

        // 判断任务是否为公示状态
        Task task = taskService.queryTask(taskId);
        if (task.getTaskState().equals(MyConstants.TASK_STATE_SUCCESS) ||
                task.getTaskState().equals(MyConstants.TASK_STATE_FAIL)) {

            task.setTaskState(MyConstants.TASK_STATE_APPEAL);
            task.setRefundMoney(0.0);
            task.setSystemBenifit(0.0);
            taskService.updateTask(task);

            List<TaskSupervisor> taskSupsToInit = taskSupervisorService.getTasksSupByTaskId(taskId);
            for (TaskSupervisor taskSupervisor: taskSupsToInit){
                taskSupervisor.setBenefit(0.0);
                taskSupervisorService.updateTaskSup(taskSupervisor);
            }
        }

        return ans;
    }

    void dealAppeal(){}

    @PostMapping("/display2User")
    String displayAppeal(@RequestBody String body){
        String userId= (String) JSON.parse(body);
        List<Appeal> ans = appealService.queryAppealFromUser(userId);
        JSONObject json = new JSONObject();
        if(ans==null) json.put("state",MyConstants.RESULT_FAIL);
        else{
            json.put("state",MyConstants.RESULT_OK);
            json.put("size",ans.size());
            json.put("appeals",ans);
        }
        return json.toJSONString();
    }

    @PostMapping("/del")
    HashMap<String,String> deleteAppeal(@RequestBody String body){
        HashMap<String,String> ans = new HashMap<>();
        try{
            String appealId = (String)JSONObject.parse(body);
            if(appealService.deleteAppeal(appealId))
                ans.put("state",MyConstants.RESULT_OK);
            else
                ans.put("state",MyConstants.RESULT_FAIL);
        }catch (Exception e){
            e.printStackTrace();
            ans.put("state",MyConstants.RESULT_FAIL);
        }
        return ans;

    }


    @RequestMapping("/queryUserAppeal")
    public List<Appeal> queryUserAppeal(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String userId= (String)object.get("userId");
        List<Appeal> appeals=appealService.queryAppealFromUser(userId);
        return appeals;
    }

}
