package com.whu.checky.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

@Deprecated
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

    @Autowired
    Match match;

    // @Scheduled(cron = "${jobs.appeal.cron}")
    @Deprecated
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

        // List<Report> reports = reportService.getAllReports();
        // for (Report report : reports) {
        // if (report.getProcessResult().equals(MyConstants.PROCESS_STATE_PASS) &&
        // report.getProcessTime() == null) {
        // if (report.getReportType().equals(MyConstants.REPORT_TYPE_SUPERVISOR)) {
        // List<TaskSupervisor> taskSupervisors = taskSupervisorService
        // .getTasksSupByTaskId(report.getTaskId());
        // for (TaskSupervisor taskSupervisor : taskSupervisors) {
        // if (taskSupervisor.getSupervisorId().equals(report.getSupervisorId())) {
        // taskSupervisor.setRemoveTime(new SimpleDateFormat().format(new Date()));
        // taskSupervisorService.updateTaskSup(taskSupervisor);

        // // add a new supervisor
        // Set<String> supervisorIds =
        // taskSupervisorService.getTasksSupByTaskId(report.getTaskId())
        // .stream().map(su -> su.getSupervisorId()).collect(Collectors.toSet());
        // match.matchSupervisorForOneTask(taskService.queryTask(report.getTaskId()),
        // supervisorIds,
        // 1);

        // break;
        // }
        // }
        // }
        // else if (report.getReportType().equals(MyConstants.REPORT_TYPE_TASK)) {
        // Task task = taskService.queryTask(report.getTaskId());
        // task.setTaskState(MyConstants.TASK_STATE_FAIL);
        // taskService.updateTask(task);
        // } else if (report.getReportType().equals(MyConstants.REPORT_TYPE_ESSAY)) {
        // Essay essay = essayService.queryEssayById(report.getEssayId());
        // essay.setIfDelete(1);
        // essayService.updateEssay(essay);
        // } else if (report.getReportType().equals(MyConstants.REPORT_TYPE_CHECK)) {
        // Check check = checkService.queryCheckById(report.getCheckId());
        // check.setCheckState(MyConstants.CHECK_STATE_DENY);
        // checkService.updateCheck(check);
        // }
        // }
        // report.setProcessTime(MyConstants.DATETIME_FORMAT.format(new Date()));
    }
}