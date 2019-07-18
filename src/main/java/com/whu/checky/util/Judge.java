package com.whu.checky.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.*;
import com.whu.checky.mapper.*;
import com.whu.checky.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//仲裁模块
@Component
public class Judge {

    @Value("${jobs.match.maxNum}")
    int matchMax;

    @Value("${jobs.judge.check.timeoutDay}")
    int timeoutDay;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    CheckMapper checkMapper;

    @Autowired
    SuperviseMapper superviseMapper;

    @Autowired
    TaskService taskService;

    @Autowired
    MoneyFlowMapper moneyFlowMapper;

    @Autowired
    UserMapper userMapper;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


//    业务逻辑
//    取出所有未判断的每日打卡记录
//    若监督的数量已足够则进行判定
//    否则判断是否过期（过了一定的天数还没有足够的监督人数
//    若过期则从监督人数通过是否过半进行判定，无人监督则算通过
    @Scheduled(cron = "${jobs.judge.check.cron}")
    public void dailyCheckSupervisedJudge(){
        System.out.println("Task start!");
        List<Check> checkList = checkMapper.selectList(new EntityWrapper<Check>()
                .eq("check_state","unknown")
        );

        for(Check c: checkList){
            try {
                Task task = taskMapper.selectById(c.getTaskId());
                int supervisorNum = task.getSupervisorNum();
                double timeDiff = (new Date().getTime() - sdf.parse(c.getCheckTime()).getTime()) / (1000 * 60 * 60 * 24);//超过一定天数后监督人数不足自动判定
                if(c.getSuperviseNum()==supervisorNum||timeDiff>=timeoutDay) {
                    if (c.getPassNum() * 2 >= c.getSuperviseNum()) {
                        c.setCheckState("pass");
                        task.setCheckNum(task.getCheckNum()+1);
                        taskMapper.updateById(task);
                    }
                    else c.setCheckState("deny");

                    checkMapper.updateById(c);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
//            List<Supervise> superviseList = superviseMapper.selectList(new EntityWrapper<Supervise>()
//                .eq("check_id",c.getCheckId())
//            );
//            int supervisorNum = taskMapper.selectById(c.getTaskId()).getSupervisorNum();
//            if(superviseList.size()==supervisorNum){
//                int count=0;
//                for(Supervise s:superviseList){
//                    if(s.getSuperviseState().equals("pass")) count++;
//                }
//                if(count*2>c.getSuperviseNum()){
//                    c.setCheckState("pass");
//                }else{
//                    c.setCheckState("deny");
//                }
//                checkMapper.updateById(c);
//            }
//            try {
//                double timeDiff = (new Date().getTime() - sdf.parse(c.getCheckTime()).getTime()) / (1000 * 60 * 60 * 24);//超过一定天数后监督人数不足自动判定
//                if(timeDiff>=timeoutDay){
//                    int count=0;
//                    for(Supervise s:superviseList){
//                        if(s.getSuperviseState().equals("pass")) count++;
//                    }
//                    if(count==0) c.setCheckState("pass");
//                    else if(count*2>=superviseList.size()) c.setCheckState("pass");
//                    else c.setCheckState("deny");
//                    checkMapper.updateById(c);
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


    @Scheduled(cron = "${jobs.judge.task.cron}")
//    @Scheduled(initialDelay = 0,fixedRate = 1000000000)
    public void taskFinishJudge(){
        Date today = new Date();
        String now = sdf.format(today);
        String before = sdf.format(new Date(today.getTime()-timeoutDay* 24 * 60 * 60 * 1000));
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
            .eq("task_state","during")
            .and()
            .between("task_end_time",before,now)
        );

        for(Task t:taskList){
//            int count = checkMapper.selectCount(new EntityWrapper<Check>()
//                .eq("check_state","pass")
//                .and()
//                .eq("task_id",t.getTaskId())
//            );
            int count = t.getCheckNum();
            if(count>=t.getCheckTimes()) {
                t.setTaskState("success");
                //成功全额退款
                MoneyFlow record = new MoneyFlow();
                record.setFlowId(UUID.randomUUID().toString());
                record.setFlowMoney(t.getTaskMoney());
                record.setFlowTime(sdf.format(new Date()));
                record.setFromUserId("system");
                record.setToUserId(t.getUserId());
                moneyFlowMapper.insert(record);
                //更新账户余额
                User user = userMapper.selectById(t.getUserId());
                user.setUserMoney(user.getUserMoney()+t.getTaskMoney());
                userMapper.updateById(user);
            }
            else if(count-timeoutDay<=t.getCheckTimes()) {
                t.setTaskState("fail");

                //失败全部分成
                HashMap<String,Double> distribute = taskService.distribute(t.getTaskId());
                for(Map.Entry<String,Double> pair:distribute.entrySet()){

                    MoneyFlow record = new MoneyFlow();
                    record.setFlowId(UUID.randomUUID().toString());
                    record.setFlowMoney(pair.getValue());
                    record.setFlowTime(sdf.format(new Date()));
                    record.setFromUserId("system");
                    record.setToUserId(pair.getKey());
                    moneyFlowMapper.insert(record);

                    User user = userMapper.selectById(pair.getKey());
                    user.setUserMoney(user.getUserMoney()+pair.getValue());
                    userMapper.updateById(user);
                }
            }
            else continue;



            taskMapper.updateById(t);
        }
    }



}
