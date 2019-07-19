package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Supervise;
import com.whu.checky.domain.SupervisorState;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.mapper.TaskSupervisorMapper;
import com.whu.checky.service.CheckService;
import com.whu.checky.service.SuperviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/supervise")
public class SuperviseController {

    @Autowired
    private SuperviseService superviseService;
    @Autowired
    private TaskSupervisorMapper taskSuspervisorMapper;
    @Autowired
    private CheckService checkService;

    //监督者对一个Check进行验证
    @RequestMapping("/addSupervise")
    public void addSupervise(@RequestBody String jsonstr){
        Supervise supervise= JSON.parseObject(jsonstr,new TypeReference<Supervise>(){});
        supervise.setSuperviseId(UUID.randomUUID().toString());
        superviseService.addSupervise(supervise);
        String checkId=supervise.getCheckId();
        if(supervise.getSuperviseState().equals("pass")){
            //如果通过,对应check的supervise_num+1
            //同时pass_num也+1
            checkService.updatePassSuperviseCheck(checkId);
        }else {
            //如果没有通过，那么只有check的supervise_num+1
            checkService.updateDenySuperviseCheck(checkId);
        }


    }


    //还需等待认证的（需要设置一个有效期之类的东西）
    @RequestMapping("/needToSupervise")
    public List<Check> needToSupervise(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String supervisorId= (String) object.get("userId");
        String startDate= (String) object.get("startDate");
        String endDate= (String) object.get("endDate");
        return superviseService.userNeedToSupervise(supervisorId,startDate,endDate);

    }

    //查询属于某个用户的所有Supervise
    @RequestMapping("/queryUserAllSupervise")
    public List<Supervise> queryUserAllSupervise(@RequestBody String jsonstr){
        String userid= (String) JSON.parse(jsonstr);
        return superviseService.queryUserSupervise(userid,null);
    }

    //查询属于某个用户的关于某个check的所有Supervise
    @RequestMapping("/queryUserCheckSupervise")
    public List<Supervise> queryUserCheckSupervise(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String userId= (String) object.get("userId");
        String checkId= (String) object.get("checkId");
        return superviseService.queryUserSupervise(userId,checkId);
    }

    //查询具体的某一个Supervise
    @RequestMapping("/querySupervise")
    public Supervise querySupervise(@RequestBody String jsonstr){
        String superviseId= (String) JSON.parse(jsonstr);
        return superviseService.querySupervise(superviseId);
    }


    //提供给管理员修改状态的
    @RequestMapping("/modifySuperviseToSuccess")
    public void modifySuperviseToSuccess(@RequestBody String jsonstr){
        String superviseId= (String) JSON.parse(jsonstr);
        superviseService.updateSupervise(superviseId,"Success");
    }

    //提供给管理员修改状态的
    @RequestMapping("/modifySuperviseToFail")
    public void modifySuperviseToFail(@RequestBody String jsonstr){
        String superviseId= (String) JSON.parse(jsonstr);
        superviseService.updateSupervise(superviseId,"Fail");
    }



    @RequestMapping("/querySupervisorState")
    public List<SupervisorState> querySuperviseState(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String taskId= (String) object.get("taskId");
        String checkId= (String) object.get("checkId");
        return superviseService.querySuperviseState(taskId,checkId);
    }

}
