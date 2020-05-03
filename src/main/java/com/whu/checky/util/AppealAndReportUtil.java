package com.whu.checky.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Essay;
import com.whu.checky.domain.Report;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.User;
import com.whu.checky.service.AppealService;
import com.whu.checky.service.CheckService;
import com.whu.checky.service.EssayService;
import com.whu.checky.service.ReportService;
import com.whu.checky.service.TaskService;
import com.whu.checky.service.TaskSupervisorService;
import com.whu.checky.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class AppealAndReportUtil {
    @Autowired
    TaskService taskService;

    @Autowired
    AppealService appealService;

    @Autowired
    ReportService reportService;

    @Autowired
    EssayService essayService;

    @Autowired
    CheckService checkService;

    @Autowired
    TaskSupervisorService taskSupervisorService;

    @Scheduled(cron = "${jobs.appeal.cron}")
    public void checkAppealAndReport() {
        /**
         * Handled elsewhere.
         */
        // List<Appeal> appeals = appealService.getAllAppeals();
        // for (Appeal appeal : appeals) {
        // if (appeal.getProcessResult() == MyConstants.PROCESS_STATE_PASS) {
        // /**
        // * The appeal has been processed.
        // */
        // Task task = taskService.queryTask(appeal.getTaskId());
        // task.setTaskState(MyConstants.TASK_STATE_DURING);
        // taskService.updateTask(task);
        // }
        // }

        List<Report> reports = reportService.getAllReports();
        for (Report report : reports) {
            if (report.getReportType().equals(MyConstants.REPORT_TYPE_SUPERVISOR)
                    && report.getProcessResult().equals(MyConstants.PROCESS_STATE_PASS)) {
                List<TaskSupervisor> taskSupervisors = taskSupervisorService.getTasksSupByTaskId(report.getTaskId());
                for (TaskSupervisor taskSupervisor : taskSupervisors) {
                    if (taskSupervisor.getSupervisorId().equals(report.getSupervisorId())) {
                        taskSupervisor.setRemoveTime(new SimpleDateFormat().format(new Date()));
                        taskSupervisorService.updateTaskSup(taskSupervisor);
                        break;
                    }
                }
            } else if (report.getReportType().equals(MyConstants.REPORT_TYPE_TASK)
                    && report.getProcessResult().equals(MyConstants.PROCESS_STATE_PASS)) {
                Task task = taskService.queryTask(report.getTaskId());
                task.setTaskState(MyConstants.TASK_STATE_FAIL);
                taskService.updateTask(task);
            } else if (report.getReportType().equals(MyConstants.REPORT_TYPE_ESSAY)
                    && report.getProcessResult().equals(MyConstants.PROCESS_STATE_PASS)) {
                Essay essay = essayService.queryEssayById(report.getEssayId());
                essay.setIfDelete(1);
                essayService.updateEssay(essay);
            } else if (report.getReportType().equals(MyConstants.REPORT_TYPE_CHECK)
                    && report.getProcessResult().equals(MyConstants.PROCESS_STATE_PASS)) {
                Check check = checkService.queryCheckById(report.getCheckId());
                check.setCheckState(MyConstants.CHECK_STATE_DENY);
                checkService.updateCheck(check);
            }
        }
    }

}