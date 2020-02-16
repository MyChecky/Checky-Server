package com.whu.checky.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.User;
import com.whu.checky.domain.UserFriend;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.TaskSupervisorMapper;
import com.whu.checky.mapper.UserFriendMapper;
import com.whu.checky.mapper.UserMapper;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;


//匹配模块
@Component
public class Match {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskSupervisorMapper taskSupervisorMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserFriendMapper userFriendMapper;

    @Value("${jobs.match.maxNum}")
    private int matchMax;

    private HashMap<String, User> tempUserList;

    private int defaultRandCandNum = 10;

    private double defaultAreaThreshold = 1.0;

    private Random rand = new Random();


    // 暂时先做简单的匹配，思路为从数据库取出未匹配任务
    // 取出用户记录并按监督次数排序
    // 暂时只选取前三个建立监督关系，不足则取所有

    @Deprecated
    public void matchSupervisor() {
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
                .eq("task_state", "nomatch")
        );

        // String [] judgements = {"supervise_num",""};
        List<User> userList = userMapper.selectList(new EntityWrapper<User>()
                .orderBy("supervise_num", true)
                .orderBy("supervise_num_min", false)
                .last("limit 70")
        );


        for (Task t : taskList) {
            String taskId = t.getTaskId();
            for (int i = 0; i < 3 && i < userList.size(); i++) {
                TaskSupervisor temp = new TaskSupervisor();
                User user = userList.get(i);
                temp.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                temp.setTaskId(taskId);
                temp.setSupervisorId(user.getUserId());
                taskSupervisorMapper.insert(temp);
                user.setSuperviseNum(user.getSuperviseNum() + 1);
                userMapper.updateById(user);
            //    t.setSupervisorNum(t.getSupervisorNum()+1);
            }
            userList.sort(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getSuperviseNum() - o2.getSuperviseNum();
                }
            });
        //    taskMapper.updateById(t);
        }
    }

    // 似乎不能static
    public boolean matchSupervisorForOneTask(Task task){
        List<User> userList = userMapper.selectList(new EntityWrapper<User>()
                .orderBy("supervise_num", true)
                .orderBy("supervise_num_min", false)
                .last("limit 70")
        );

        String taskId = task.getTaskId();
        for (int i = 0; i < task.getSupervisorNum() && i < userList.size(); i++) {
            // 这里做hobby, area, supervisorType的判别
            // 三个的值都是，0:随机,1:相似爱好/相近地域/熟人，2:不同爱好/不同地域/陌生人
            TaskSupervisor temp = new TaskSupervisor();
            User user = userList.get(i);
            temp.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            temp.setTaskId(taskId);
            temp.setSupervisorId(user.getUserId());
            taskSupervisorMapper.insert(temp);
            user.setSuperviseNum(user.getSuperviseNum() + 1);
            userMapper.updateById(user);
            // 该任务对应的已匹配的监督者的数目加一
            // （matchNum:实际已匹配的数目，supervisorNum:任务发布时要求的监督者数目）
            task.setMatchNum(task.getMatchNum() + 1);
        }
        userList.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getSuperviseNum() - o2.getSuperviseNum();
            }
        });

        return task.getSupervisorNum() == task.getMatchNum();
    }


    @Scheduled(cron = "${jobs.match.cron}")
    public void matchSupervisorBySimilarity() {
        System.out.println("Task start!");

        // get all tasks needing supervisors
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
                        .eq("task_state", "nomatch")
                        .or()
                        .eq("task_state", "during")
                        .and()
                        .lt("match_num", matchMax)
            //    .or()
            //    .lt("supervisor_num",matchMax)
        );

        // map: raiserId => task raiser
        tempUserList = new HashMap<>();
        for (Task t : taskList) {
            tempUserList.put(t.getUserId(), userMapper.selectById(t.getUserId()));
        }

        for (Task t : taskList) {
        //    if(t.getTaskState().equals("during")) continue;
            PriorityQueue<Pair<Integer, Double>> queue = new PriorityQueue<>(
                    new Comparator<Pair<Integer, Double>>() {
                        @Override
                        public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                            return (int) (o1.getValue() - o2.getValue());
                        }
                    });
            for (int i = 0; i < taskList.size(); i++) {
                Task another = taskList.get(i);
                if (t == another) continue;
                double value = computeSimiV1(t, another);
                queue.add(new Pair<>(i, value));
            }

            HashMap<String, Task> add = new HashMap<>();

            int i;
            for (i = t.getSupervisorNum(); i < matchMax && !queue.isEmpty(); ) {
                // while(!queue.isEmpty()){
                int idx = queue.poll().getKey();
                Task temp = taskList.get(idx);
                if (temp.getMatchNum() == matchMax || add.containsKey(temp.getUserId()) || temp.getUserId().equals(t.getUserId()))
                    continue;
                add.put(temp.getUserId(), temp);
                //  }
                i++;
            }

            if (i == matchMax) {
                for (Map.Entry<String, Task> entry : add.entrySet()) {
                    Task temp = entry.getValue();
                    TaskSupervisor taskSupervisor = new TaskSupervisor();
                    taskSupervisor.setTaskId(t.getTaskId());
                    taskSupervisor.setSupervisorId(temp.getUserId());
                    taskSupervisor.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    taskSupervisorMapper.insert(taskSupervisor);
                    t.setSupervisorNum(t.getSupervisorNum() + 1);
                    t.setTaskState("during");
                    temp.setMatchNum(temp.getMatchNum() + 1);
                }
            }
        }

        // update db
        for (Task t : taskList) taskMapper.updateById(t);
    }

    //    typeDiff = type1==type2?0:1
    //    weight = startTimeDiff * 0.3 + endTimeDiff * 0.3 + typeDiff + goldDiff * 0.02 + creditDiff * 0.2 + leftMatch;
    private double computeSimiV1(Task t1, Task t2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        double ans;
        try {
            int trans = (1000 * 60 * 60 * 24);
            double startTimeDiff = Math.abs(sdf.parse(t1.getTaskStartTime()).getTime() - sdf.parse(t2.getTaskStartTime()).getTime()) / trans;
            double endTimeDiff = Math.abs(sdf.parse(t1.getTaskEndTime()).getTime() - sdf.parse(t2.getTaskEndTime()).getTime()) / trans;
            double typeDiff = t1.getTaskId().equals(t2.getTypeId()) ? 0 : 1;
            double goldDiff = t1.getTaskMoney() - t2.getTaskMoney();
            double creditDiff = (tempUserList.get(t1.getUserId()).getUserCredit() - tempUserList.get(t2.getUserId()).getUserCredit());
            int leftMatch = matchMax - t2.getMatchNum();
            ans = startTimeDiff * 0.3 + endTimeDiff * 0.3 + typeDiff * 10 + goldDiff * 0.02 + creditDiff * 0.2 + leftMatch;
        } catch (Exception e) {
            return 1000.0;
        }
        return ans;
    }

    HashSet<String> acqCandidate(String userId) {
        HashSet<String> candidates = new HashSet<>();

        List<UserFriend> friendsFrom = userFriendMapper.selectList(new EntityWrapper<UserFriend>()
            .eq("to_user_id", userId)
        );

        for(UserFriend uf : friendsFrom) {
            candidates.add(uf.getFromUserId());
        }

        List<UserFriend> friendsTo = userFriendMapper.selectList(new EntityWrapper<UserFriend>()
            .eq("from_user_id", userId)
        );

        for(UserFriend uf : friendsTo) {
            candidates.add(uf.getToUserId());
        }

        List<Task> raiserTasks = taskMapper.selectList(new EntityWrapper<Task>()
            .eq("user_id", userId)
            .eq("task_state", "complete")
        );

        for(Task t : raiserTasks) {
            List<TaskSupervisor> supervisors = taskSupervisorMapper.selectList(new EntityWrapper<TaskSupervisor>()
                .eq("task_id", t.getTaskId())
            );

            for(TaskSupervisor ts : supervisors){
                candidates.add(ts.getSupervisorId());
            }
        }

        return candidates;
    }

    HashSet<String> strgCandidate(String userId) {
        HashSet<String> candidates = new HashSet<>();

        HashSet<String> invCandidates = acqCandidate(userId);

        List<User> candidateUsers = userMapper.selectList(new EntityWrapper<User>());

        for(User u : candidateUsers) {
            String uid = u.getUserId();
            if(!invCandidates.contains(uid)) {
                candidates.add(uid);
            }
        }

        return candidates;
    }

    HashSet<String> randCandidate(String userId) {
        return randCandidate(userId, defaultRandCandNum);
    }

    HashSet<String> randCandidate(String userId, int num) {
        HashSet<String> candidates = new HashSet<>();

        int cnt = userMapper.selectCount(new EntityWrapper<User>());

        if(cnt <= num) {
            List<User> candidateUsers = userMapper.selectList(new EntityWrapper<User>());

            for(User u : candidateUsers) {
                candidates.add(u.getUserId());
            }
        } else {
            HashSet<Integer> randIds = new HashSet<>();

            while(randIds.size() < num) {
                int newRandId = rand.nextInt(num);
                randIds.add(newRandId);
            }

            List<User> candidateUsers = userMapper.selectList(new EntityWrapper<User>());
            
            for(int rid : randIds) {
                candidates.add(candidateUsers.get(rid).getUserId());
            }
        }

        return candidates;
    }

    HashSet<String> areaCandidate(String userId) {
        return areaCandidate(userId, defaultAreaThreshold);
    }

    HashSet<String> areaCandidate(String userId, double threshold) {
        HashSet<String> candidates = new HashSet<>();

        List<User> users = userMapper.selectList(new EntityWrapper<User>()
            .eq("user_id", userId)
        );
        User user = users.get(0);

        double longtitude = user.getLongtitude();
        double latitude = user.getLatitude();

        List<User> users2 = userMapper.selectList(new EntityWrapper<User>());

        for(User user2 : users2) {
            double longtitude2 = user2.getLongtitude();
            double latitude2 = user2.getLatitude();

            double distance = Math.sqrt(
                Math.pow(longtitude2 - longtitude, 2) + 
                Math.pow(latitude2 - latitude, 2)
            );

            if(distance < threshold) {
                candidates.add(user2.getUserId());
            }

            candidates.add(user2.getUserId());
        }

        return candidates;
    }

    HashSet<String> hobbyCandidate(String userId) {
        HashSet<String> candidates = new HashSet<>();

        List<User> users = userMapper.selectList(new EntityWrapper<User>()
            .eq("user_id", userId)
        );
        User user = users.get(0);

        String hobbyStr = user.getHobby();

        if(hobbyStr != null) {
            String[] hobbies = hobbyStr.split(",");

            HashSet<String> hobbySet = new HashSet<String>();
            for(String h : hobbies) {
                hobbySet.add(h);
            }
    
            List<User> users2 = userMapper.selectList(new EntityWrapper<User>());
    
            for(User user2 : users2) {
                String hobbyStr2 = user2.getHobby();

                if(hobbyStr2 != null) {
                    String[] hobbies2 = hobbyStr2.split(",");
    
                    for(String h2 : hobbies2) {
                        if(hobbySet.contains(h2)) {
                            candidates.add(user2.getUserId());
        
                            break;
                        }
                    }
                }
            }
        }

        return candidates;
    }

    // helper function to determine a Task's corresponding MatchType
    // according to specific fields in db
    MatchType resolveMatchType(Task task) {
        MatchType mt = new MatchType();

        int supervisorType = task.getSupervisorType();
        int ifArea = task.getIfArea();
        int ifHobby = task.getIfHobby();

        switch(supervisorType) {
            case 0: {
                mt.isRand = true;
                break;
            }
            case 1: {
                mt.isAcq = true;
                break;
            }
            case 2: {
                mt.isStrg = true;
                break;
            }
        }

        if(ifArea == 1) {
            mt.isArea = true;
        }

        if(ifHobby == 1) {
            mt.isHobby = true;
        }

        return mt;
    }

    // @return's length might < num
    // caller should check it
    HashSet<String> match(String userId, MatchType type, int num) {
        HashSet<String> candidates = new HashSet<>();

        if(type.isAcq) {
            HashSet<String> added = acqCandidate(userId);
            candidates.addAll(added);
        }

        if(type.isStrg) {
            HashSet<String> added = strgCandidate(userId);
            candidates.addAll(added);
        }

        if(type.isRand) {
            HashSet<String> added = randCandidate(userId);
            candidates.addAll(added);
        }

        if(type.isArea) {
            HashSet<String> added = areaCandidate(userId);
            candidates.addAll(added);
        }

        if(type.isHobby) {
            HashSet<String> added = hobbyCandidate(userId);
            candidates.addAll(added);
        }

        ArrayList<String> candidatesList = new ArrayList<String>();
        candidatesList.addAll(candidates);
        Collections.shuffle(candidatesList);

        HashSet<String> supervisors = new HashSet<String>();

        int actualNum = Math.min(num, candidatesList.size());
        for(int i = 0; i < actualNum; i++) {
            supervisors.add(candidatesList.get(i));
        }

        return supervisors;
    }
}