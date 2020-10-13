package com.whu.checky.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.*;
import com.whu.checky.service.TaskService;

import com.whu.checky.util.MyConstants;
import com.whu.checky.util.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("taskService")
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CheckMapper checkMapper;
    @Autowired
    private TaskSupervisorMapper taskSupervisorMapper;
    @Autowired
    private TaskTypeMapper taskTypeMapper;

    private Calendar cal = Calendar.getInstance();
    private int[] weekDays = {7, 1, 2, 3, 4, 5, 6};

    private void updateCheckTimes(Task task) {
        int weekNum = 0;
        int res = 0;
        // try {
        // Date dateStart = format.parse(task.getTaskStartTime());
        // Date dateEnd = format.parse(task.getTaskEndTime());
        // weekNum=(int)(dateEnd.getTime() - dateStart.getTime())/86400000/7;
        // }catch (Exception e){
        // e.printStackTrace();
        //
        // }
        String temp = task.getCheckFrec();
        try {
            Date dateStart = MyConstants.DATE_FORMAT.parse(task.getTaskStartTime());
            Date dateEnd = MyConstants.DATE_FORMAT.parse(task.getTaskEndTime());
            weekNum = (int) ((dateEnd.getTime() - dateStart.getTime()) / 86400000 / 7);
            Date startDate = MyConstants.DATE_FORMAT.parse(task.getTaskStartTime());
            cal.setTime(startDate);
            int w1 = cal.get(Calendar.DAY_OF_WEEK) - 1; // 开始日期是周几
            Date endDate = MyConstants.DATE_FORMAT.parse(task.getTaskEndTime());
            cal.setTime(endDate);
            int w2 = cal.get(Calendar.DAY_OF_WEEK) - 1; // 结束日期是周几
            if (w1 != w2) {
                while (w1 != w2) {
                    if (temp.charAt(w1) == '1')
                        res++;
                    w1 = (w1 + 1) % 7;
                }
            }
            if (temp.charAt(w2) == '1')
                res++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        int Frec = 0;
        for (int i = 0; i < 7; i++) {
            if (temp.charAt(i) == '1')
                Frec++;
        }

        int checkTimes = weekNum * Frec;
        res += checkTimes;
        task.setCheckTimes(res);
    }

    @Override
    public Integer addTask(Task task) {
        updateCheckTimes(task);
        task.setTaskState(MyConstants.TASK_STATE_SAVE);
        return taskMapper.insert(task);
    }

    @Override
    public Integer delTask(String taskId) {
        return taskMapper.deleteById(taskId);
    }

    @Override
    public Integer updateTask(Task task) {
        return taskMapper.updateById(task);
    }

    @Override
    public Integer updateTaskWithUpdateCheckTimes(Task task) {
        updateCheckTimes(task);
        return taskMapper.updateById(task);
    }

    @Override
    public List<Task> queryUserTasks(String userId, String date) {
        List<Task> res;
        if (date == null) {
            res = taskMapper.selectList(new EntityWrapper<Task>()
                    .eq("user_id", userId)
                    .orderBy("add_time", false));
            for (Task task : res) {
                task.setTypeContent(taskTypeMapper.selectById(task.getTypeId()).getTypeContent());
            }
        } else {
            res = taskMapper.queryUserTasks(userId, date);
            try {
                Date tmpDate = MyConstants.DATE_FORMAT.parse(date);
                Calendar cal = Calendar.getInstance();
                // int[] weekDays = {7,1,2,3,4,5,6};
                cal.setTime(tmpDate);
                int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
                List<Task> removeList = new LinkedList<Task>();
                for (Task task : res) {
                    if (task.getCheckFrec().charAt(w) != '1') {
                        removeList.add(task);
                    }
                }
                for (Task task : removeList) {
                    res.remove(task);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return res;
        // taskMapper.selectList(new
        // EntityWrapper<Task>().eq("user_id",userid).le("task_start_time",date)
        // .ge("task_end_time",date));
    }

    @Override
    public List<Task> listTasks() {
        return taskMapper.selectList(new EntityWrapper<Task>().orderBy("task_start_time"));
    }

    @Override
    public Task queryTask(String taskId) {
        return taskMapper.selectById(taskId);
    }

    @Override
    public HashMap<String, Double> distribute(Task task) {
        HashMap<String, Double> result = new HashMap<>();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        List<TaskSupervisor> supervisors = taskSupervisorMapper
                .selectList(new EntityWrapper<TaskSupervisor>().eq("task_id", task.getTaskId()));
        supervisors.sort(Comparator.comparingInt(TaskSupervisor::getSuperviseNum));

        double money = task.getTaskMoney() * 0.8;
        int superviseNum = 0;

        for (TaskSupervisor supvisor : supervisors) {
            superviseNum += supvisor.getSuperviseNum();
        }

        for (TaskSupervisor taskSupervisor : supervisors) {
            taskSupervisor.setBenefit(money * taskSupervisor.getSuperviseNum() / superviseNum);
            taskSupervisor.setRemoveTime(dateFormat.format(now));
            taskSupervisorMapper.update(taskSupervisor,
                    new EntityWrapper<TaskSupervisor>().eq("task_id", taskSupervisor.getTaskId()).and()
                            .eq("supervisor_id", taskSupervisor.getSupervisorId()));
            result.put(taskSupervisor.getSupervisorId(), money);
        }

        return result;
    }

    @Override
    public List<Task> query(HashMap<String, String> params, Page<Task> page) {
        Wrapper<Task> wrapper = new EntityWrapper<>();
        for (String key : params.keySet()) {
            wrapper = wrapper.eq(key, params.get(key));
        }

        if (page == null) {
            // return taskMapper.selectList(wrapper);
            return taskMapper.getTasksWithName(wrapper);
        } else {
            // return taskMapper.selectPage(new Page<Task>(page, 10), wrapper);
            return taskMapper.getTasksWithNameAndPage(wrapper, page);
        }
    }

    @Override
    public String getTitleById(String taskId) {
        return taskMapper.getTitleById(taskId);
    }

    @Override
    public List<Task> getTasksAtNoMatchStateOwnedByUser(User user) {
        return taskMapper.selectList(new EntityWrapper<Task>().eq("user_id", user.getUserId()).eq("task_state", MyConstants.TASK_STATE_NOMATCH));
    }

    @Override
    public List<Task> queryTaskLikeNickname(Page<Task> p, String startTime, String endTime, String keyword) {
        List<User> users = userMapper.selectList(new EntityWrapper<User>().like("user_name", keyword));
        List<String> userIds = new ArrayList<>();
        for (User user : users) {
            userIds.add(user.getUserId());
        }
        List<Task> tasks = taskMapper.selectPage(p, new EntityWrapper<Task>()
                .in("user_id", userIds)
                .andNew()
                .ge("task_start_time", startTime)
                .or()
                .le("task_end_time", endTime)
                .orderBy("add_time", false));
        for (Task task : tasks) {
            task.setUserName(userMapper.getUsernameById(task.getUserId()));
        }
        return tasks;
    }

    @Override
    public List<Task> queryTaskLikeContent(Page<Task> p, String startTime, String endTime, String keyword) {
        List<Task> tasks = taskMapper.selectPage(p, new EntityWrapper<Task>()
                .like("task_content", keyword)
                .andNew()
                .ge("task_start_time", startTime)
                .or()
                .le("task_end_time", endTime)
                .orderBy("add_time", false));
        for (Task task : tasks) {
            task.setUserName(userMapper.getUsernameById(task.getUserId()));
        }
        return tasks;
    }

    @Override
    public List<Task> queryTaskLikeTitle(Page<Task> p, String startTime, String endTime, String keyword) {
        List<Task> tasks = taskMapper.selectPage(p, new EntityWrapper<Task>()
                .like("task_title", keyword)
                .andNew()
                .ge("task_start_time", startTime)
                .or()
                .le("task_end_time", endTime)
                .orderBy("add_time", false));
        for (Task task : tasks) {
            task.setUserName(userMapper.getUsernameById(task.getUserId()));
        }
        return tasks;
    }

    @Override
    public List<Task> queryTaskAll(Page<Task> p, String startTime, String endTime) {
        List<Task> tasks = taskMapper.selectPage(p, new EntityWrapper<Task>()
                .ge("task_start_time", startTime)
                .or()
                .le("task_end_time", endTime)
                .orderBy("add_time", false));
        for (Task task : tasks) {
            task.setUserName(userMapper.getUsernameById(task.getUserId()));
        }
        return tasks;
    }

    @Override
    public List<Task> queryTaskByUser(String userId) {
        return taskMapper.selectList(new EntityWrapper<Task>()
                .eq("user_id", userId));
    }

    @Override
    public boolean checkFailTaskWeekly(String userId) {
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
                .eq("task_state", "fail")
                .and()
                .eq("user_id", userId)
        );
        for(int i = 0;i<taskList.size();i++)
        {
            if(!MyStringUtil.checkIsBeforeWeek(taskList.get(i).getTaskEndTime()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkFreWeekly(String userId) {
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
        .eq("user_id",userId)
                        .andNew()
                .eq("task_state","during")
        );
        //遍历正在进行的每一个任务
        for(int i =0 ;i<taskList.size();i++)
        {
            int checkNumSupposed = MyStringUtil.transferCheckFre(taskList.get(i).getCheckFrec());
            //查找该任务的每一次打卡
            List<Check> checkList = checkMapper.selectList(new EntityWrapper<Check>()
            .eq("task_id",taskList.get(i).getTaskId())
            );
            //判断本周内打卡次数是否符合check_fre
            int checkNum = 0;
            for(int j = 0;j<checkList.size();j++)
            {
                if(MyStringUtil.checkIsBeforeWeek(checkList.get(j).getCheckTime()))
                {
                    checkNum++;
                }
            }
            //如果某个任务的打卡次数小于应该的次数，返回false
            if(checkNum<checkNumSupposed)
            {
                return false;
            }
        }
        return true;
    }
}
