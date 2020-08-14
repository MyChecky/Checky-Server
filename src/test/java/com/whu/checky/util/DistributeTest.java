package com.whu.checky.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.TaskSupervisorMapper;
import com.whu.checky.mapper.UserMapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DistributeTest {
    final double EPS = 1e-6;
    final Logger LOGGER = Logger.getLogger(DistributeTest.class.getName());

    @Autowired
    Distribute cls;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskSupervisorMapper taskSupervisorMapper;

    @Test
    public void assignMoneyTest() throws Exception {
        List<Task> tasks = taskMapper.selectList(new EntityWrapper<>());
        Task task = tasks.get(0);

        LOGGER.info(String.format("Task: %s", task.getTaskTitle()));

        task.setTaskState(MyConstants.TASK_STATE_SUCCESS);
        task.setTaskAnnounceTime(
                LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        LOGGER.info(String.format("Task Announce Time: %s", task.getTaskAnnounceTime()));

        taskMapper.updateById(task);

        double deposit = task.getTaskMoney();

        LOGGER.info(String.format("Deposit: %f", deposit));

        double remained = deposit;

        double moneySystem = (double) (int) (remained * task.getSystemBenifit() * 100) / 100;

        LOGGER.info(String.format("System Benifit: %f", task.getSystemBenifit()));

        LOGGER.info(String.format("System Money Added: %f", moneySystem));

        remained -= moneySystem;

        String superviseeId = task.getUserId();
        User supervisee = userMapper.selectById(superviseeId);

        LOGGER.info(String.format("Supervisee: %s", supervisee.getUserName()));

        double rate = task.getRealPass();

        LOGGER.info(String.format("Rate: %f", rate));

        double moneySupervisee = (double) (int) (remained * rate * 100) / 100;

        LOGGER.info(String.format("Supervisee Money Added: %f", moneySupervisee));

        double superviseeCurMoney = supervisee.getUserMoney();

        LOGGER.info(String.format("Supervisee Money (before): %f", superviseeCurMoney));

        superviseeCurMoney += moneySupervisee;

        LOGGER.info(String.format("Supervisee Money (after): %f", superviseeCurMoney));

        remained -= moneySupervisee;

        String taskId = task.getTaskId();
        List<TaskSupervisor> supervisors = taskSupervisorMapper
                .selectList(new EntityWrapper<TaskSupervisor>().eq("task_id", taskId));

        LOGGER.info(String.format("Num Supervisors: %d", supervisors.size()));

        int numActualChecks = task.getCheckNum();

        LOGGER.info(String.format("Num Actual Checks: %d", numActualChecks));

        List<Double> supervisorRates = new ArrayList<Double>();
        double sumRates = 0.0;
        for (TaskSupervisor supervisor : supervisors) {

            LOGGER.info(String.format("\tFor Supervisor: %s",
                    userMapper.selectById(supervisor.getSupervisorId()).getUserName()));

            int numBads = supervisor.getBadNum();

            LOGGER.info(String.format("\tNum Bads: %d", numBads));

            int numActualSupervise = numActualChecks - numBads;

            LOGGER.info(String.format("\tNum Actual Supervise: %d", numActualSupervise));

            Assert.assertTrue(numActualSupervise >= 0);

            if (supervisor.getRemoveTime() != null) {
                /**
                 * If supervisor does not supervise, or has been removed:
                 */
                numActualSupervise = 0;

                LOGGER.info(String.format("\t\tSupervisor removed at %s for reason %s. Set Num Actual Supervise to %d.",
                        supervisor.getRemoveTime(), supervisor.getRemoveReason(), numActualSupervise));
            }

            double supervisorRate = (double) numActualSupervise / numActualChecks;

            LOGGER.info(String.format("\tSupervisor Rate: %f", supervisorRate));

            supervisorRates.add(supervisorRate);

            sumRates += supervisorRate;
        }

        List<Double> supervisorBenefits = new ArrayList<Double>();
        List<Double> supervisorMoneys = new ArrayList<Double>();
        List<User> supervisorUsers = new ArrayList<>();
        for (int i = 0; i < supervisors.size(); i++) {
            TaskSupervisor supervisor = supervisors.get(i);
            double supervisorRate = supervisorRates.get(i);

            LOGGER.info(String.format("\tFor Supervisor: %s",
                    userMapper.selectById(supervisor.getSupervisorId()).getUserName()));

            double moneyRate = supervisorRate / sumRates;

            LOGGER.info(String.format("\tSupervisor Rate (normalized): %f", moneyRate));

            double moneySupervisor = (double) (int) (remained * moneyRate * 100) / 100;

            LOGGER.info(String.format("\tSupervisor Money Added: %f", moneySupervisor));

            supervisorBenefits.add(moneySupervisor);

            String supervisorId = supervisor.getSupervisorId();
            User supervisorUser = userMapper.selectById(supervisorId);
            supervisorUsers.add(supervisorUser);

            double moneyCur = supervisorUser.getUserMoney();

            LOGGER.info(String.format("\tSupervisor Money (before): %f", moneyCur));

            moneyCur += moneySupervisor;

            LOGGER.info(String.format("\tSupervisor Money (after): %f", moneyCur));

            supervisorMoneys.add(moneyCur);
        }

        cls.assignMoney();

        task = taskMapper.selectById(task.getTaskId());
        Assert.assertEquals(moneySupervisee, task.getRefundMoney().doubleValue(), EPS);

        supervisee = userMapper.selectById(supervisee.getUserId());
        Assert.assertEquals(superviseeCurMoney, supervisee.getUserMoney().doubleValue(), EPS);

        for (int i = 0; i < supervisors.size(); i++) {
            double supervisorBenefit = supervisorBenefits.get(i);

            TaskSupervisor supervisor = supervisors.get(i);
            supervisor = taskSupervisorMapper.selectList(new EntityWrapper<TaskSupervisor>()
                    .eq("task_id", supervisor.getTaskId()).and().eq("supervisor_id", supervisor.getSupervisorId()))
                    .get(0);
            Assert.assertEquals(supervisorBenefit, supervisor.getBenefit(), EPS);

            double supervisorMoney = supervisorMoneys.get(i);

            User supervisorUser = supervisorUsers.get(i);
            supervisorUser = userMapper.selectById(supervisorUser.getUserId());
            Assert.assertEquals(supervisorMoney, supervisorUser.getUserMoney(), EPS);
        }

        Assert.assertEquals(task.getTaskState(), MyConstants.TASK_STATE_COMPLETE);
    }
}