package com.whu.checky.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.TaskSupervisorMapper;
import com.whu.checky.mapper.UserMapper;
import com.whu.checky.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//分成模块
@Component
public class Distribute {

    @Autowired
    UserMapper userMapper;

    @Autowired
    TaskService taskService;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskSupervisorMapper taskSupervisorMapper;

    @Scheduled(cron = "${jobs.distribute.cron}")
    public void assignMoney() {
        List<Task> tasks = taskMapper.selectList(new EntityWrapper<Task>()
                .eq("task_state", MyConstants.TASK_STATE_SUCCESS).or().eq("task_state", MyConstants.TASK_STATE_FAIL));

        for (Task task : tasks) {
            assignMoney(task);
        }
    }

    void assignMoney(Task task) {
        assignMoney(task, task.getSystemBenifit(), 1);
    }

    void assignMoney(Task task, double systemRate, int numAppealDays) {
        String taskAnnounceTime = task.getTaskAnnounceTime();
        if (taskAnnounceTime != null) {
            Duration duration = Duration.between(
                    LocalDateTime.parse(taskAnnounceTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.now());
            if (duration.toDays() < numAppealDays) {
                /**
                 * Still waiting for possible appeals.
                 */
                return;
            }
        }

        double deposit = task.getTaskMoney();

        double remained = deposit;

        double moneySystem = (double) (int) (remained * systemRate * 100) / 100;
        // task.setSystemBenifit(moneySystem);

        remained -= moneySystem;

        String superviseeId = task.getUserId();
        User supervisee = userMapper.selectById(superviseeId);

        double rate = task.getRealPass();
        double moneySupervisee = (double) (int) (remained * rate * 100) / 100;
        task.setRefundMoney(moneySupervisee);

        double superviseeCurMoney = supervisee.getUserMoney();
        superviseeCurMoney += moneySupervisee;
        supervisee.setUserMoney(superviseeCurMoney);
        userMapper.updateById(supervisee);

        remained -= moneySupervisee;

        String taskId = task.getTaskId();
        List<TaskSupervisor> supervisors = taskSupervisorMapper
                .selectList(new EntityWrapper<TaskSupervisor>().eq("task_id", taskId));

        int numActualChecks = task.getCheckNum();

        List<Double> supervisorRates = new ArrayList<Double>();
        double sumRates = 0.0;
        for (TaskSupervisor supervisor : supervisors) {
            int numBads = supervisor.getBadNum();
            int numActualSupervise = numActualChecks - numBads;
            if (numActualSupervise < 0 || supervisor.getRemoveTime() != null) {
                /**
                 * If supervisor does not supervise, or has been removed:
                 */
                numActualSupervise = 0;
            }

            double supervisorRate = (double) numActualSupervise / numActualChecks;
            supervisorRates.add(supervisorRate);

            sumRates += supervisorRate;
        }

        for (int i = 0; i < supervisors.size(); i++) {
            TaskSupervisor supervisor = supervisors.get(i);
            double supervisorRate = supervisorRates.get(i);

            double moneyRate = supervisorRate / sumRates;

            double moneySupervisor = (double) (int) (remained * moneyRate * 100) / 100;
            supervisor.setBenefit(moneySupervisor);
            taskSupervisorMapper.updateById(supervisor);

            String supervisorId = supervisor.getSupervisorId();
            User supervisorUser = userMapper.selectById(supervisorId);

            double moneyCur = supervisorUser.getUserMoney();
            moneyCur += moneySupervisor;
            supervisorUser.setUserMoney(moneyCur);
            userMapper.updateById(supervisorUser);
        }

        task.setTaskState(MyConstants.TASK_STATE_COMPLETE);

        taskService.updateTaskWithUpdateCheckTimes(task);
    }
}
