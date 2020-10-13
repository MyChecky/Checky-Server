package com.whu.checky.service;

import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.User;

public interface TaskService {
    // 用户新建打卡
    Integer addTask(Task task);

    Integer delTask(String taskId);

    Integer updateTask(Task task);

    Integer updateTaskWithUpdateCheckTimes(Task task);

    String getTitleById(String taskId);

    List<Task> queryUserTasks(String userId, String date);

    List<Task> listTasks();

    Task queryTask(String taskId);

    HashMap<String, Double> distribute(Task task);

    // //打卡日历
    // void getTasks();
    // //打卡清单和历史记录
    // void getTaskList();
    List<Task> query(HashMap<String, String> params, Page<Task> page);

    List<Task> getTasksAtNoMatchStateOwnedByUser(User user);

    List<Task> queryTaskLikeNickname(Page<Task> p, String startTime, String endTime, String keyword);

    List<Task> queryTaskLikeContent(Page<Task> p, String startTime, String endTime, String keyword);

    List<Task> queryTaskLikeTitle(Page<Task> p, String startTime, String endTime, String keyword);

    List<Task> queryTaskAll(Page<Task> p, String startTime, String endTime);

    List<Task> queryTaskByUser(String userId);
    //筛选一周内是否有失败任务
    boolean checkFailTaskWeekly(String userId);
    //判断该用户所有任务的一周内打卡是否全都按照check_fre连续打卡
    boolean checkFreWeekly(String userId);
}
