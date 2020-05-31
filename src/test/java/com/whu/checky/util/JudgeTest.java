package com.whu.checky.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

import java.security.KeyStore.Entry;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.*;
import com.whu.checky.mapper.*;
import com.whu.checky.service.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class JudgeTest {
    final Logger LOGGER = Logger.getLogger(JudgeTest.class.getName());
    final double EPS = 1e-6;

    @Autowired
    Judge cls;

    @Autowired
    CheckMapper checkMapper;

    @Autowired
    TaskSupervisorMapper taskSupervisorMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskService taskService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SuperviseMapper superviseMapper;

    @Autowired
    AppealMapper appealMapper;

    SimpleDateFormat sdf = new SimpleDateFormat(MyConstants.FORMAT_DATE);

    @Test
    public void checkinTest() throws Exception {
        Date checkTime = new Date(new Date().getTime() - 12 * 60 * 60 * 1000);
        String checkTimeStr = sdf.format(checkTime);

        List<Check> checks = checkMapper.selectList(new EntityWrapper<>());
        Check check = checks.get(0);

        check.setCheckTime(checkTimeStr);
        check.setCheckState(MyConstants.CHECK_STATE_UNKNOWN);

        checkMapper.updateById(check);

        String checkId = check.getCheckId();
        String taskId = check.getTaskId();

        Task task = taskMapper.selectById(taskId);

        LOGGER.info(String.format("Task: %s", task.getTaskTitle()));
        LOGGER.info(String.format("Check Date: %s", check.getCheckTime()));

        List<TaskSupervisor> supervisors = taskSupervisorMapper
                .selectList(new EntityWrapper<TaskSupervisor>().eq("task_id", taskId));

        int numPasses = check.getPassNum();
        LOGGER.info(String.format("numPasses: %d", numPasses));

        int numSupers = check.getSuperviseNum();
        LOGGER.info(String.format("numSupers: %d", numSupers));

        Map<TaskSupervisor, Integer> supervisorBadNumsToCheck = new HashMap<>();
        Map<Task, Integer> taskCheckPassCheck = new HashMap<>();

        for (TaskSupervisor supervisor : supervisors) {

            LOGGER.info(String.format(String.format("\tFor Supervisor: %s",
                    userMapper.selectById(supervisor.getSupervisorId()).getUserName())));

            String supervisorId = supervisor.getSupervisorId();

            boolean isPass = true;

            List<Supervise> supervises = superviseMapper.selectList(
                    new EntityWrapper<Supervise>().eq("check_id", checkId).eq("supervisor_id", supervisorId));

            String status = MyConstants.SUPERVISE_STATE_UNKNOWN;

            if (supervises != null && supervises.size() > 0) {
                Supervise supervise = supervises.get(0);

                status = supervise.getSuperviseState();
                LOGGER.info(String.format("Supervise State: %s", status));

                if (status.equals(MyConstants.SUPERVISE_STATE_PASS)) {
                    isPass = true;
                } else if (status.equals(MyConstants.SUPERVISE_STATE_DENY)) {
                    isPass = false;
                } else if (status.equals(MyConstants.SUPERVISE_STATE_UNKNOWN)) {
                    isPass = true;

                    TaskSupervisor badSupervisor = taskSupervisorMapper.selectList(
                            new EntityWrapper<TaskSupervisor>().eq("task_id", taskId).eq("supervisor_id", supervisorId))
                            .get(0);

                    int badNum = supervisor.getBadNum();
                    LOGGER.info(String.format("Supervisor Bad Num (before): %d", badNum));
                    badNum += 1;
                    LOGGER.info(String.format("Supervisor Bad Num (after): %d", badNum));

                    supervisorBadNumsToCheck.put(badSupervisor, badNum);
                }
            } else {
                if (supervises == null) {
                    LOGGER.info("supervises is null");
                } else {
                    LOGGER.info(String.format("supervises.size(): %d", supervises.size()));
                }
            }

            if (isPass) {
                LOGGER.info("Pass");
                numPasses += 1;
            } else {
                LOGGER.info("Not Pass");
            }

            numSupers += 1;

            LOGGER.info(String.format("numPasses: %d, numSupers: %d", numPasses, numSupers));
        }

        String checkType = task.getMinCheckType();
        LOGGER.info(String.format("checkType: %s", checkType));

        String resultState = null;

        if (checkType.equals(MyConstants.CHECK_TYPE_PROPORTION)) {
            double proportion = task.getMinCheck();
            LOGGER.info(String.format("proportion (expected): %f", proportion));

            double proportionCur = (double) numPasses / numSupers;
            LOGGER.info(String.format("proportion （current): %f", proportionCur));

            resultState = MyConstants.CHECK_STATE_PASS;
            if (proportionCur >= proportion) {
                resultState = MyConstants.CHECK_STATE_PASS;
            } else {
                resultState = MyConstants.CHECK_STATE_DENY;
            }
        } else if (checkType.equals(MyConstants.CHECK_TYPE_NUMBER)) {
            double minPasses = task.getMinCheck();
            LOGGER.info(String.format("minPasses (expected): %f", minPasses));

            resultState = MyConstants.CHECK_STATE_PASS;
            if (numPasses >= minPasses) {
                resultState = MyConstants.CHECK_STATE_PASS;
            } else {
                resultState = MyConstants.CHECK_STATE_DENY;
            }
        }
        LOGGER.info(String.format("resultState: %s", resultState));

        int checkPass = task.getCheckPass();
        LOGGER.info(String.format("checkPass (before): %d", checkPass));

        if (resultState.equals(MyConstants.CHECK_STATE_PASS)) {
            checkPass++;
        }

        LOGGER.info(String.format("checkPass (after): %d", checkPass));

        cls.checkin();

        for (Map.Entry<TaskSupervisor, Integer> entry : supervisorBadNumsToCheck.entrySet()) {
            TaskSupervisor supervisor = entry.getKey();
            supervisor = taskSupervisorMapper.selectList(new EntityWrapper<TaskSupervisor>()
                    .eq("task_id", supervisor.getTaskId()).and().eq("supervisor_id", supervisor.getSupervisorId()))
                    .get(0);

            Assert.assertEquals(entry.getValue().intValue(), supervisor.getBadNum());
        }

        task = taskMapper.selectById(task.getTaskId());
        Assert.assertEquals(checkPass, task.getCheckPass());

        check = checkMapper.selectById(check.getCheckId());

        Assert.assertEquals(numPasses, check.getPassNum().intValue());
        Assert.assertEquals(numSupers, check.getSuperviseNum().intValue());
        Assert.assertEquals(resultState, check.getCheckState());
    }

    @Test
    public void checkTaskSuccessTest() throws Exception {
        List<Task> tasks = taskMapper.selectList(new EntityWrapper<>());
        Task task = tasks.get(0);

        task.setTaskState(MyConstants.TASK_STATE_DURING);
        task.setTaskEndTime(sdf.format(new Date().getTime() - 24 * 60 * 60 * 1000));
        taskMapper.updateById(task);

        LOGGER.info(String.format("Task: %s", task.getTaskTitle()));

        String taskId = task.getTaskId();

        List<Appeal> appeals = appealMapper
                .selectList(new EntityWrapper<Appeal>().eq("task_id", taskId).and().isNull("process_time"));

        String finalStates = null;
        Double rate = null;
        if (appeals == null || appeals.size() == 0) {
            int numPasses = task.getCheckPass();
            int numShould = task.getCheckTimes();

            LOGGER.info(String.format("numPasses: %d, numShould: %d", numPasses, numShould));

            rate = (double) numPasses / numShould;

            LOGGER.info(String.format("rate: %f", rate));

            /**
             * If {@code numShould} = 0, which means no requirement is imposed on this task,
             * so it is implicitly a success.
             */
            if (Double.isNaN(rate)) {
                rate = 1.0;
            }
            LOGGER.info(String.format("rate (normalized): %f", rate));

            double minRate = task.getMinPass();
            LOGGER.info(String.format("minRate: %f", minRate));

            if (rate >= minRate) {
                finalStates = MyConstants.TASK_STATE_SUCCESS;
            } else {
                finalStates = MyConstants.TASK_STATE_FAIL;
            }
            LOGGER.info(String.format("finalStates: %s", finalStates));
        } else {
            LOGGER.info(String.format("appeals.size(): %d", appeals.size()));
        }

        cls.checkTaskSuccess();

        task = taskMapper.selectById(task.getTaskId());
        Assert.assertEquals(finalStates, task.getTaskState());
        Assert.assertEquals(rate.doubleValue(), task.getRealPass(), EPS);
    }

    @Test
    public void checkTaskSuccessTest2() throws Exception {
        List<Task> tasks = taskMapper.selectList(new EntityWrapper<>());
        Task task = tasks.get(0);

        task.setTaskState(MyConstants.TASK_STATE_DURING);
        task.setTaskEndTime(sdf.format(new Date().getTime() + 24 * 60 * 60 * 1000));
        taskMapper.updateById(task);

        LOGGER.info(String.format("Task: %s", task.getTaskTitle()));

        String taskId = task.getTaskId();

        List<Appeal> appeals = appealMapper
                .selectList(new EntityWrapper<Appeal>().eq("task_id", taskId).and().isNull("process_time"));

        String finalStates = MyConstants.TASK_STATE_DURING;
        double rate = task.getRealPass();

        cls.checkTaskSuccess();

        task = taskMapper.selectById(task.getTaskId());
        Assert.assertEquals(finalStates, task.getTaskState());
        Assert.assertEquals(rate, task.getRealPass(), EPS);
    }

}