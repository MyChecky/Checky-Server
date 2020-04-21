package com.whu.checky.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.User;
import com.whu.checky.domain.UserFriend;
import com.whu.checky.domain.UserHobby;
import com.whu.checky.service.MoneyService;
import com.whu.checky.service.TaskService;
import com.whu.checky.service.TaskSupervisorService;
import com.whu.checky.service.UserFriendService;
import com.whu.checky.service.UserHobbyService;
import com.whu.checky.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//匹配模块
@Component
public class Match {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private UserFriendService userFriendService;

    @Autowired
    private UserHobbyService userHobbyService;

    @Autowired
    private TaskSupervisorService taskSupervisorService;

    @Value("${jobs.match.maxNum}")
    @Deprecated
    private int matchMax;

    // @Deprecated
    // private HashMap<String, User> tempUserList;

    private static final double DEFAULT_AREA_THRESHOLD = 1.0;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(cron = "${jobs.match.cron}")
    public void match() {
        final int pageSize = 5;
        int curPage = 1;
        while (true) {
            Page<User> page = new Page<>(curPage, pageSize);
            List<User> users = userService.queryUsersWithPage(page);

            for (User u : users) {
                matchSupervisorForOneUser(u);
            }

            if (users.isEmpty())
                break;
            curPage++;
        }
    }

    public void matchSupervisorForOneUser(User user) {
        List<Task> tasks = taskService.getTasksAtNoMatchStateOwnedByUser(user);
        for (Task t : tasks) {
            matchSupervisorForOneTask(t);
        }
    }

    /**
     * @param task
     * @return true if the task is fully matched
     * 前端用户需要知道当前任务实际匹配到了多少人，故改变参数task的matchNum值（task是引用类型，相当于已经有matchNum的返回值了）--lu
     * 而若是把资金等相关的注释了，定时匹配任务时，会出问题的。。。--lu
     */
    public boolean matchSupervisorForOneTask(Task task) {
        int gap = task.getSupervisorNum();
        List<User> selectedSupervisors;
        Set<String> selectedSupervisorIds = new HashSet<>();

        User taskOwner = userService.queryUser(task.getUserId());

        MatchType matchType = resolveMatchType(task);

        if (matchType.isRand) {
            selectedSupervisors = userService.getUsersRandomly(gap, task.getUserId());
            gap -= selectedSupervisors.size();
        } else {
            selectedSupervisors = new ArrayList<>();
            
            final int pageSize = 5;
            int curPage = 1;
            endIterateUsers: while (true) {
                Page<User> page = new Page<>(curPage, pageSize);
                List<User> users = userService.queryUsersWithPage(page);

                for (User potentialSupervisor : users) {
                    if (potentialSupervisor.getUserId().equals(taskOwner.getUserId()))
                        continue;

                    if (matchType.isAcq == isAcq(taskOwner, potentialSupervisor)
                            && (matchType.isInSameArea == isInSameArea(taskOwner, potentialSupervisor))
                            && (matchType.hasSameHobby == hasSameHobby(taskOwner, potentialSupervisor))) {
                        if(!selectedSupervisorIds.contains(potentialSupervisor.getUserId())) {
                            selectedSupervisorIds.add(potentialSupervisor.getUserId());
                            selectedSupervisors.add(potentialSupervisor);
                            --gap;
                        }
                    }

                    if (gap == 0)
                        break endIterateUsers;
                }

                if (users.isEmpty())
                    break;
                curPage++;
            }
        }

//        return task.getSupervisorNum() - gap;
        task.setMatchNum(task.getSupervisorNum() - gap);  // 更新一下当前能匹配到的监督者人数，相当于返回了--lu

         if(gap == 0) {
             for (User supervisor : selectedSupervisors) {
                 TaskSupervisor newTaskSupervisor = new TaskSupervisor();
                 newTaskSupervisor.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                 newTaskSupervisor.setTaskId(task.getTaskId());
                 newTaskSupervisor.setSupervisorId(supervisor.getUserId());
                 taskSupervisorService.addTaskSupervisor(newTaskSupervisor);
    
                 supervisor.setSuperviseNum(supervisor.getSuperviseNum() + 1);
                 userService.updateUser(supervisor);
             }

//             task.setMatchNum(task.getSupervisorNum());
             task.setTaskState(MyConstants.TASK_STATE_DURING);
             taskService.updateTask(task);

             taskOwner.setTaskNum(taskOwner.getTaskNum() + 1);
             if (task.getIfTest() == MyConstants.IF_TEST_TRUE) {
                 taskOwner.setTestMoney(taskOwner.getTestMoney() - task.getTaskMoney());
                 userService.updateUser(taskOwner);
             } else if (task.getIfTest() == MyConstants.IF_TEST_FALSE) {
                 taskOwner.setUserMoney(taskOwner.getUserMoney() - task.getTaskMoney());
                 userService.updateUser(taskOwner);
             }

             MoneyFlow moneyFlow = new MoneyFlow();
             moneyFlow.setUserID(task.getUserId());
             moneyFlow.setIfTest(task.getIfTest());
             moneyFlow.setFlowIo("O");
             moneyFlow.setFlowType("pay");
             moneyFlow.setFlowMoney(task.getTaskMoney());
             moneyFlow.setTaskId(task.getTaskId());
             moneyFlow.setFlowTime(DATE_FORMAT.format(new Date()));
             moneyFlow.setFlowId(UUID.randomUUID().toString());
             moneyService.addTestMoneyRecord(moneyFlow);

             return true;
         }else{
             taskService.updateTask(task); // task仍是noMatch状态，但是要更新一下数据库，当前能匹配到的监督者人数--lu
         }

         return false;
    }

    /**
     * helper function to determine a Task's corresponding MatchType according to
     * specific fields in db
     * 
     * @param task
     * @return MatchType
     */
    MatchType resolveMatchType(Task task) {
        MatchType mt = new MatchType();

        int supervisorType = task.getSupervisorType();
        int ifArea = task.getIfArea();
        int ifHobby = task.getIfHobby();

        switch (supervisorType) {
            case 0: {
                mt.isRand = true;
                return mt;
            }
            case 1: {
                mt.isAcq = true;
                break;
            }
            case 2: {
                mt.isAcq = false;
                break;
            }
        }

        switch (ifArea) {
            case 1: {
                mt.isInSameArea = true;
                break;
            }
            case 2: {
                mt.isInSameArea = false;
                break;
            }
        }

        switch (ifHobby) {
            case 1: {
                mt.hasSameHobby = true;
                break;
            }
            case 2: {
                mt.hasSameHobby = false;
                break;
            }
        }

        return mt;
    }

    boolean isAcq(User taskOwner, User potentialSupervisor) {
        if (taskOwner.getUserId().equals(potentialSupervisor.getUserId()))
            throw new RuntimeException("Task owner can never self-supervise!");

        List<UserFriend> taskOwnersFriends = userFriendService.queryUserFriends(taskOwner.getUserId());
        if (taskOwnersFriends.stream().anyMatch(uf -> uf.getToUserId().equals(potentialSupervisor.getUserId())
                || uf.getFromUserId().equals(potentialSupervisor.getUserId())))
            return true;

        return false;
    }

    boolean isInSameArea(User taskOwner, User potentialSupervisor) {
        return isInSameArea(taskOwner, potentialSupervisor, DEFAULT_AREA_THRESHOLD);
    }

    boolean isInSameArea(User taskOwner, User potentialSupervisor, double threshold) {
        if (taskOwner.getUserId().equals(potentialSupervisor.getUserId()))
            throw new RuntimeException("Task owner can never self-supervise!");

        double distance = Math.sqrt(Math.pow(taskOwner.getLongtitude() - potentialSupervisor.getLongtitude(), 2)
                + Math.pow(taskOwner.getLatitude() - potentialSupervisor.getLatitude(), 2));
        if (distance < threshold)
            return true;
        else
            return false;
    }

    boolean hasSameHobby(User taskOwner, User potentialSupervisor) {
        List<UserHobby> taskOwnerHobbies = userHobbyService.getUserHobbies(taskOwner.getUserId());
        List<UserHobby> potentialSupervisorHobbies = userHobbyService.getUserHobbies(potentialSupervisor.getUserId());
        return taskOwnerHobbies.stream().anyMatch(
                h1 -> potentialSupervisorHobbies.stream().anyMatch(h2 -> h1.getHobbyId().equals(h2.getHobbyId())));
    }

    // 暂时先做简单的匹配，思路为从数据库取出未匹配任务
    // 取出用户记录并按监督次数排序
    // 暂时只选取前三个建立监督关系，不足则取所有
    // @Deprecated
    // public void matchSupervisor() {
    // List<Task> taskList = taskMapper.selectList(new
    // EntityWrapper<Task>().eq("task_state", MyConstants.TASK_STATE_NOMATCH));

    // // String [] judgements = {"supervise_num",""};
    // List<User> userList = userMapper.selectList(new
    // EntityWrapper<User>().orderBy("supervise_num", true)
    // .orderBy("supervise_num_min", false).last("limit 70"));

    // for (Task t : taskList) {
    // String taskId = t.getTaskId();
    // for (int i = 0; i < 3 && i < userList.size(); i++) {
    // TaskSupervisor temp = new TaskSupervisor();
    // User user = userList.get(i);
    // temp.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
    // Date()));
    // temp.setTaskId(taskId);
    // temp.setSupervisorId(user.getUserId());
    // taskSupervisorMapper.insert(temp);
    // user.setSuperviseNum(user.getSuperviseNum() + 1);
    // userMapper.updateById(user);
    // // t.setSupervisorNum(t.getSupervisorNum()+1);
    // }
    // userList.sort(new Comparator<User>() {
    // @Override
    // public int compare(User o1, User o2) {
    // return o1.getSuperviseNum() - o2.getSuperviseNum();
    // }
    // });
    // // taskMapper.updateById(t);
    // }
    // }

    // @Deprecated
    // public void matchSupervisorBySimilarity() {
    // System.out.println("Task start!");

    // // get all tasks needing supervisors
    // List<Task> taskList = taskMapper.selectList(new
    // EntityWrapper<Task>().eq("task_state", MyConstants.TASK_STATE_NOMATCH).or()
    // .eq("task_state", MyConstants.TASK_STATE_DURING).and().lt("match_num", matchMax)
    // // .or()
    // // .lt("supervisor_num",matchMax)
    // );

    // // map: raiserId => task raiser
    // tempUserList = new HashMap<>();
    // for (Task t : taskList) {
    // tempUserList.put(t.getUserId(), userMapper.selectById(t.getUserId()));
    // }

    // for (Task t : taskList) {
    // // if(t.getTaskState().equals(MyConstants.TASK_STATE_DURING)) continue;
    // PriorityQueue<Pair<Integer, Double>> queue = new PriorityQueue<>(new
    // Comparator<Pair<Integer, Double>>() {
    // @Override
    // public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
    // return (int) (o1.getValue() - o2.getValue());
    // }
    // });
    // for (int i = 0; i < taskList.size(); i++) {
    // Task another = taskList.get(i);
    // if (t == another)
    // continue;
    // double value = computeSimiV1(t, another);
    // queue.add(new Pair<>(i, value));
    // }

    // HashMap<String, Task> add = new HashMap<>();

    // int i;
    // for (i = t.getSupervisorNum(); i < matchMax && !queue.isEmpty();) {
    // // while(!queue.isEmpty()){
    // int idx = queue.poll().getKey();
    // Task temp = taskList.get(idx);
    // if (temp.getMatchNum() == matchMax || add.containsKey(temp.getUserId())
    // || temp.getUserId().equals(t.getUserId()))
    // continue;
    // add.put(temp.getUserId(), temp);
    // // }
    // i++;
    // }

    // if (i == matchMax) {
    // for (Map.Entry<String, Task> entry : add.entrySet()) {
    // Task temp = entry.getValue();
    // TaskSupervisor taskSupervisor = new TaskSupervisor();
    // taskSupervisor.setTaskId(t.getTaskId());
    // taskSupervisor.setSupervisorId(temp.getUserId());
    // taskSupervisor.setAddTime(new SimpleDateFormat("yyyy-MM-dd
    // HH:mm:ss").format(new Date()));
    // taskSupervisorMapper.insert(taskSupervisor);
    // t.setSupervisorNum(t.getSupervisorNum() + 1);
    // t.setTaskState(MyConstants.TASK_STATE_DURING);
    // temp.setMatchNum(temp.getMatchNum() + 1);
    // }
    // }
    // }

    // // update db
    // for (Task t : taskList)
    // taskMapper.updateById(t);
    // }

    // typeDiff = type1==type2?0:1
    // weight = startTimeDiff * 0.3 + endTimeDiff * 0.3 + typeDiff + goldDiff * 0.02
    // + creditDiff * 0.2 + leftMatch;
    // @Deprecated
    // private double computeSimiV1(Task t1, Task t2) {
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // double ans;
    // try {
    // int trans = (1000 * 60 * 60 * 24);
    // double startTimeDiff = Math.abs(
    // sdf.parse(t1.getTaskStartTime()).getTime() -
    // sdf.parse(t2.getTaskStartTime()).getTime()) / trans;
    // double endTimeDiff = Math
    // .abs(sdf.parse(t1.getTaskEndTime()).getTime() -
    // sdf.parse(t2.getTaskEndTime()).getTime()) / trans;
    // double typeDiff = t1.getTaskId().equals(t2.getTypeId()) ? 0 : 1;
    // double goldDiff = t1.getTaskMoney() - t2.getTaskMoney();
    // double creditDiff = (tempUserList.get(t1.getUserId()).getUserCredit()
    // - tempUserList.get(t2.getUserId()).getUserCredit());
    // int leftMatch = matchMax - t2.getMatchNum();
    // ans = startTimeDiff * 0.3 + endTimeDiff * 0.3 + typeDiff * 10 + goldDiff *
    // 0.02 + creditDiff * 0.2
    // + leftMatch;
    // } catch (Exception e) {
    // return 1000.0;
    // }
    // return ans;
    // }
}