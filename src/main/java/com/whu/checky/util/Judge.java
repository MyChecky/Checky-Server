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
    CheckMapper checkMapper;

    @Autowired
    SuperviseMapper superviseMapper;

    @Autowired
    TaskMapper taskMapper;

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

    SimpleDateFormat sdf = new SimpleDateFormat(MyConstants.FORMAT_DATE);

    // 业务逻辑
    // 取出所有未判断的每日打卡记录
    // 若监督的数量已足够则进行判定
    // 否则判断是否过期（过了一定的天数还没有足够的监督人数
    // 若过期则从监督人数通过是否过半进行判定，无人监督则算通过
    @Deprecated
    public void dailyCheckSupervisedJudge() {
        System.out.println("Task start!");
        List<Check> checkList = checkMapper
                .selectList(new EntityWrapper<Check>().eq("check_state", MyConstants.CHECK_STATE_UNKNOWN));

        for (Check c : checkList) {
            try {
                Task task = taskService.queryTask(c.getTaskId());
                int supervisorNum = task.getSupervisorNum();
                double timeDiff = (new Date().getTime() - sdf.parse(c.getCheckTime()).getTime())
                        / (1000 * 60 * 60 * 24);// 超过一定天数后监督人数不足自动判定
                if (c.getSuperviseNum() == supervisorNum || timeDiff >= timeoutDay) {
                    if (c.getPassNum() * 2 >= c.getSuperviseNum()) {
                        c.setCheckState(MyConstants.CHECK_STATE_PASS);
                        task.setCheckNum(task.getCheckNum() + 1);
                        taskService.updateTaskWithUpdateCheckTimes(task);
                    } else
                        c.setCheckState(MyConstants.CHECK_STATE_DENY);

                    checkMapper.updateById(c);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    // List<Supervise> superviseList = superviseMapper.selectList(new
    // EntityWrapper<Supervise>()
    // .eq("check_id",c.getCheckId())
    // );
    // int supervisorNum = taskMapper.selectById(c.getTaskId()).getSupervisorNum();
    // if(superviseList.size()==supervisorNum){
    // int count=0;
    // for(Supervise s:superviseList){
    // if(s.getSuperviseState().equals("pass")) count++;
    // }
    // if(count*2>c.getSuperviseNum()){
    // c.setCheckState("pass");
    // }else{
    // c.setCheckState("deny");
    // }
    // checkMapper.updateById(c);
    // }
    // try {
    // double timeDiff = (new Date().getTime() -
    // sdf.parse(c.getCheckTime()).getTime()) / (1000 * 60 * 60 *
    // 24);//超过一定天数后监督人数不足自动判定
    // if(timeDiff>=timeoutDay){
    // int count=0;
    // for(Supervise s:superviseList){
    // if(s.getSuperviseState().equals("pass")) count++;
    // }
    // if(count==0) c.setCheckState("pass");
    // else if(count*2>=superviseList.size()) c.setCheckState("pass");
    // else c.setCheckState("deny");
    // checkMapper.updateById(c);
    // }
    // } catch (ParseException e) {
    // e.printStackTrace();
    // }

    // @Scheduled(initialDelay = 0,fixedRate = 1000000000)
    @Deprecated
    public void taskFinishJudge() {
        Date today = new Date();
        String now = sdf.format(today);
        String before = sdf.format(new Date(today.getTime() - timeoutDay * 24 * 60 * 60 * 1000));
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
                .eq("task_state", MyConstants.TASK_STATE_DURING).and().between("task_end_time", before, now));

        for (Task t : taskList) {
            // int count = checkMapper.selectCount(new EntityWrapper<Check>()
            // .eq("check_state","pass")
            // .and()
            // .eq("task_id",t.getTaskId())
            // );
            int count = t.getCheckNum();
            if (count >= t.getCheckTimes()) {
                t.setTaskState(MyConstants.TASK_STATE_SUCCESS);
                // 成功全额退款
                MoneyFlow record = new MoneyFlow();
                record.setFlowId(UUID.randomUUID().toString());
                record.setFlowMoney(t.getTaskMoney());
                record.setFlowTime(sdf.format(new Date()));
                record.setUserID(t.getUserId());
                record.setIfTest(t.getIfTest());
                record.setFlowIo(MyConstants.MONEY_FLOW_IN);
                record.setFlowType(MyConstants.MONEY_FLOW_TYPE_REFUND);
                record.setTaskId(t.getTaskId());
                moneyFlowMapper.insert(record);
                // 根据是否试玩，更新账户余额
                User user = userMapper.selectById(t.getUserId());
                if (t.getIfTest() == MyConstants.IF_TEST_TRUE) {
                    user.setTestMoney(user.getTestMoney() + t.getTaskMoney());
                } else {
                    user.setUserMoney(user.getUserMoney() + t.getTaskMoney());
                }
                userMapper.updateById(user);
            } else if (count - timeoutDay <= t.getCheckTimes()) {
                t.setTaskState(MyConstants.TASK_STATE_FAIL);

                // 失败全部分成
                HashMap<String, Double> distribute = taskService.distribute(t);
                for (Map.Entry<String, Double> pair : distribute.entrySet()) {

                    MoneyFlow record = new MoneyFlow();
                    record.setFlowId(UUID.randomUUID().toString());
                    record.setFlowMoney(pair.getValue());
                    record.setFlowTime(sdf.format(new Date()));
                    record.setUserID(t.getUserId());
                    record.setIfTest(t.getIfTest());
                    record.setFlowIo(MyConstants.MONEY_FLOW_IN);
                    record.setFlowType(MyConstants.MONEY_FLOW_TYPE_BENEFIT);
                    record.setTaskId(t.getTaskId());
                    moneyFlowMapper.insert(record);

                    User user = userMapper.selectById(pair.getKey());
                    if (t.getIfTest() == MyConstants.IF_TEST_TRUE) {
                        user.setTestMoney(user.getTestMoney() + t.getTaskMoney());
                    } else {
                        user.setUserMoney(user.getUserMoney() + t.getTaskMoney());
                    }
                    userMapper.updateById(user);
                }
            } else
                continue;

            taskService.updateTaskWithUpdateCheckTimes(t);
        }
    }

    private boolean supervisorPass(String checkId, String supervisorId) {
        boolean isPass = true;

        // 找到当前打卡与监督者的监督清单
        List<Supervise> supervises = superviseMapper
                .selectList(new EntityWrapper<Supervise>().eq("check_id", checkId).eq("supervisor_id", supervisorId));

        String status = MyConstants.SUPERVISE_STATE_UNKNOWN;

        // 对监督记录进行判断
        // 如果有监督记录
        if (supervises != null && supervises.size() > 0) {
            Supervise supervise = supervises.get(0);

            status = supervise.getSuperviseState();

            // 如果已完成监督
            if (status.equals(MyConstants.SUPERVISE_STATE_PASS)) {
                isPass = true;
            } else if (status.equals(MyConstants.SUPERVISE_STATE_DENY)) {
                isPass = false;
            }
            // 没有完成监督，则累计其记录其不作为记录?
            else if (status.equals(MyConstants.SUPERVISE_STATE_UNKNOWN)) {
                isPass = true;

                Check check = checkMapper.selectById(checkId);
                String taskId = check.getTaskId();

                List<TaskSupervisor> supervisors = taskSupervisorMapper.selectList(
                        new EntityWrapper<TaskSupervisor>().eq("task_id", taskId).eq("supervisor_id", supervisorId));
                TaskSupervisor supervisor = supervisors.get(0);

                int badNum = supervisor.getBadNum();
                badNum += 1;
                supervisor.setBadNum(badNum);
                taskSupervisorMapper.updateById(supervisor);
            }
        }

        // 如果没有监督记录呢？
        // added by lhw on 2020.4.20

        // 目前默认通过

        return isPass;
    }

    @Scheduled(cron = "${jobs.judge.check.cron}")
    public void checkin() {
        Date today = new Date();
        String todayStr = sdf.format(today);

        Date yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000);
        String yesterdayStr = sdf.format(yesterday);

        List<Check> checks = checkMapper.selectList(new EntityWrapper<Check>()
                .between("check_time", yesterdayStr, todayStr).eq("check_state", MyConstants.CHECK_STATE_UNKNOWN));

        // 遍历打卡
        for (Check check : checks) {
            String checkId = check.getCheckId();

            // 找到监督者
            String taskId = check.getTaskId();
            List<TaskSupervisor> supervisors = taskSupervisorMapper
                    .selectList(new EntityWrapper<TaskSupervisor>().eq("task_id", taskId));

            int numPasses = check.getPassNum();
            int numSupers = check.getSuperviseNum();

            // 对打卡监督者的监督记录进行判断，并综合计算当前打卡是否通过
            for (TaskSupervisor supervisor : supervisors) {
                String supervisorId = supervisor.getSupervisorId();
                boolean isPass = supervisorPass(checkId, supervisorId);

                if (isPass) {
                    numPasses += 1;
                }

                numSupers += 1;
            }

            // 图上说 pass_num += 0/1，此处没有问题
            check.setPassNum(numPasses);
            check.setSuperviseNum(numSupers);

            Task task = taskService.queryTask(taskId);

            String checkType = task.getMinCheckType();
            String resultState = null;
            if (checkType.equals(MyConstants.CHECK_TYPE_PROPORTION)) {
                double proportion = task.getMinCheck();
                double proportionCur = (double) numPasses / numSupers;

                resultState = MyConstants.CHECK_STATE_PASS;
                if (proportionCur >= proportion) {
                    resultState = MyConstants.CHECK_STATE_PASS;
                } else {
                    resultState = MyConstants.CHECK_STATE_DENY;
                }
            } else if (checkType.equals(MyConstants.CHECK_TYPE_NUMBER)) {
                double minPasses = task.getMinCheck();

                resultState = null;
                if (numPasses >= minPasses) {
                    resultState = MyConstants.CHECK_STATE_PASS;
                } else {
                    resultState = MyConstants.CHECK_STATE_DENY;
                }
            }
            check.setCheckState(resultState);

            int checkPass = task.getCheckPass();

            if (resultState.equals(MyConstants.CHECK_STATE_PASS)) {
                checkPass++;
            }

            task.setCheckPass(checkPass);

            taskMapper.updateById(task);

            checkMapper.updateById(check);
        }
    }

    @Scheduled(cron = "${jobs.judge.task.cron}")
    public void checkTaskSuccess() {

        List<Task> tasks = taskMapper.selectList(new EntityWrapper<Task>()
                .eq("task_state", MyConstants.TASK_STATE_DURING).le("task_end_time", sdf.format(new Date())));

        for (Task task : tasks) {
            String taskId = task.getTaskId();

            List<Appeal> appeals = appealMapper
                    .selectList(new EntityWrapper<Appeal>().eq("task_id", taskId).and().isNull("process_time"));

            if (appeals == null || appeals.size() == 0) {
                int numPasses = task.getCheckPass();
                int numShould = task.getCheckTimes();

                double rate = (double) numPasses / numShould;

                /**
                 * If {@code numShould} = 0, which means no requirement is imposed on this task,
                 * so it is implicitly a success.
                 */
                if (Double.isNaN(rate)) {
                    rate = 1;
                }

                double minRate = task.getMinPass();

                String finalStates = MyConstants.TASK_STATE_SUCCESS;
                if (rate >= minRate) {
                    finalStates = MyConstants.TASK_STATE_SUCCESS;
                } else {
                    finalStates = MyConstants.TASK_STATE_FAIL;
                }
                task.setTaskState(finalStates);

                task.setRealPass(rate);

                taskService.updateTask(task);
            }
        }
    }
}
