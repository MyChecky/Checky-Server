package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.service.MoneyService;
import com.whu.checky.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/money")
public class MoneyController {
    @Autowired
    private MoneyService moneyService;
    @Autowired
    private TaskService taskService;

    @RequestMapping("/pay")
    public void pay(@RequestBody String jsonstr){
        //微信支付相关

        //成功之后在服务器数据库记录流水
        MoneyFlow moneyFlow= JSON.parseObject(jsonstr,new TypeReference<MoneyFlow>(){});
        int result=moneyService.addMoneyRecord(moneyFlow);
        if(result==1){
            //记录成功
        }else {
            //记录失败
        }
    }

    @RequestMapping("/payback")
    public void payback(){
        //进行分成算法
        //然后对每个监督者进行退款并且记录在数据库当中

    }

    @RequestMapping("/queryMoneyRecord")
    public List<MyFlow> queryMoneyRecord(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String userId= (String)object.get("userId");
        List<MoneyFlow> MoneyRecords=moneyService.queryUserMoneyFlow(userId);
        List<MyFlow> res=new ArrayList<MyFlow>();
        for(MoneyFlow record:MoneyRecords){
            MyFlow flow=new MyFlow();
            flow.setFlowMoney(record.getFlowMoney());
            flow.setFlowTime(record.getFlowTime());
            flow.setTaskTitle(taskService.queryTask(record.getTaskId()).getTaskTitle());
            if (record.getFromUserId().equals(userId)) {
                flow.setType("cost");
            }else {
                flow.setType("income");
            }
            res.add(flow);
        }
        return res;
    }
}
class MyFlow{
    private String type;
    private double flowMoney;
    private String flowTime;
    private String taskTitle;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getFlowMoney() {
        return flowMoney;
    }

    public void setFlowMoney(double flowMoney) {
        this.flowMoney = flowMoney;
    }

    public String getFlowTime() {
        return flowTime;
    }

    public void setFlowTime(String flowTime) {
        this.flowTime = flowTime;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
}

