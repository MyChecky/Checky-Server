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

    @Autowired
    TaskSupervisorMapper taskSupervisorMapper;

    @Autowired
    AppealMapper appealMapper;

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
                record.setUserID(t.getUserId());
                record.setIfTest(t.getIfTest());
                record.setFlowIo("I");
                record.setFlowType("refund");
                record.setTaskId(t.getTaskId());
                moneyFlowMapper.insert(record);
                // 根据是否试玩，更新账户余额
                User user = userMapper.selectById(t.getUserId());
                if(t.getIfTest() == 1){
                    user.setTestMoney(user.getTestMoney()+t.getTaskMoney());
                }else{
                    user.setUserMoney(user.getUserMoney()+t.getTaskMoney());
                }
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
                    record.setUserID(t.getUserId());
                    record.setIfTest(t.getIfTest());
                    record.setFlowIo("I");
                    record.setFlowType("benefit");
                    record.setTaskId(t.getTaskId());
                    moneyFlowMapper.insert(record);

                    User user = userMapper.selectById(pair.getKey());
                    if(t.getIfTest() == 1){
                        user.setTestMoney(user.getTestMoney()+t.getTaskMoney());
                    }else{
                        user.setUserMoney(user.getUserMoney()+t.getTaskMoney());
                    }
                    userMapper.updateById(user);
                }
            }
            else continue;



            taskMapper.updateById(t);
        }
    }

    boolean supervisorPass(String checkId, String supervisorId) {
        boolean isPass = true;

        List<Supervise> supervises = superviseMapper.selectList(new EntityWrapper<Supervise>()
            .eq("check_id", checkId)
            .eq("supervisor_id", supervisorId)
        );

        String status = "unknown";

        if(supervises != null && supervises.size() > 0) {
            Supervise supervise = supervises.get(0);

            status = supervise.getSuperviseState();

            if(status == "pass") {
                isPass = true;
            } else if(status == "deny") {
                isPass = false;
            } else if(status == "unknown") {
                isPass = true;
    
                Check check = checkMapper.selectById(checkId);
                String taskId = check.getTaskId();

                List<TaskSupervisor> supervisors = taskSupervisorMapper.selectList(new EntityWrapper<TaskSupervisor>()
                    .eq("task_id", taskId)
                    .eq("supervisor_id", supervisorId)
                );
                TaskSupervisor supervisor = supervisors.get(0);

                int badNum = supervisor.getBadNum();
                badNum += 1;
                supervisor.setBadNum(badNum);
                taskSupervisorMapper.updateById(supervisor);
            }
        }        

        return isPass;
    }

    public void checkin() {
        Date today = new Date();
        String todayStr = sdf.format(today);

        Date yesterday = new Date(today.getTime()- 24 * 60 * 60 * 1000);
        String yesterdayStr = sdf.format(yesterday);

        List<Check> checks = checkMapper.selectList(new EntityWrapper<Check>()
            .between("check_time", yesterdayStr, todayStr)
            .eq("check_state", "unknown")
        );

        for(Check check : checks) {
            String checkId = check.getCheckId();

            String taskId = check.getTaskId();
            List<TaskSupervisor> supervisors = taskSupervisorMapper.selectList(new EntityWrapper<TaskSupervisor>()
                .eq("task_id", taskId)
            );

            int numPasses = check.getPassNum();
            int numSupers = check.getSuperviseNum();

            for(TaskSupervisor supervisor : supervisors) {
                String supervisorId = supervisor.getSupervisorId();
                boolean isPass = supervisorPass(checkId, supervisorId);

                if(isPass) {
                    numPasses += 1;
                }

                numSupers += 1;
            }

            check.setPassNum(numPasses);
            check.setSuperviseNum(numSupers);

            Task task = taskMapper.selectById(taskId);

            String checkType = task.getMinCheckType();
            if(checkType == "proportion") {
                double proportion = task.getMinCheck();
                double proportionCur = (double)numPasses / numSupers;
                
                String resultState = "pass";
                if(proportionCur >= proportion) {
                    resultState = "pass";
                } else {
                    resultState = "deny";
                }
                
                check.setCheckState(resultState);
            } else if(checkType == "number") {
                double minPasses = task.getMinCheck();

                String resultState = "pass";
                if(numPasses >= minPasses) {
                    resultState = "pass";
                } else {
                    resultState = "deny";
                }

                check.setCheckState(resultState);
            }

            checkMapper.updateById(check);
        }
    }

    public void checkTaskSuccess() {
        Date today = new Date();
        String todayStr = sdf.format(today);

        Date yesterday = new Date(today.getTime()- 24 * 60 * 60 * 1000);
        String yesterdayStr = sdf.format(yesterday);

        List<Task> tasks = taskMapper.selectList(new EntityWrapper<Task>()
            .between("task_end_time", yesterdayStr, todayStr)
            .eq("task_state", "during")
        );

        for(Task task : tasks) {
            String taskId = task.getTaskId();

            List<Appeal> appeals = appealMapper.selectList(new EntityWrapper<Appeal>()
                .eq("task_id", taskId)
                .isNull("process_time")
            );

            if(appeals == null && appeals.size() == 0) {
                int numPasses = task.getCheckPass();
                int numShould = task.getCheckTimes();

                double rate = (double)numPasses / numShould;
                double minRate = task.getMinPass();
                
                String finalStates = "success";
                if(rate >= minRate) {
                    finalStates = "success";
                } else {
                    finalStates = "fail";
                }
                task.setTaskState(finalStates);

                task.setRealPass(rate);

                taskMapper.updateById(task);
            }
        }
    }
}
