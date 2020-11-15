package com.whu.checky.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Medal;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.UserMedal;
import com.whu.checky.mapper.CheckMapper;
import com.whu.checky.mapper.MedalMapper;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.UserMedalMapper;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MedalUpdate {
    @Value("${jobs.judge.check.timeoutDay}")
    int timeoutDay;

    @Autowired
    UserService userService;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    CheckMapper checkMapper;

    @Autowired
    MedalMapper medalMapper;

    @Autowired
    UserMedalMapper userMedalMapper;

    private static List<String> levelMedalIdList = new ArrayList<>();

    private static List<String> areaSpecialMedalIdList = new ArrayList<>();

    private static String concentrateMedalId;

    // name -> id 方便映射更新
    private static Map<String, String> levelMedalIdAndLevel = new HashMap<>();

    private static final Page<Check> checkPageConcentrate = new Page<>(0, MyConstants.CONCENTRATE_MEDAL_TIMES);

    // private static final Page<Check> checkPageAreaSpecial = new Page<>(0, MyConstants.AREA_SPECIAL_MEDAL_TIMES);

    @Scheduled(cron = "${jobs.medal.cron}")
    public void updateMedal() {
        // 初始化 medal id 相关
        initSomeVariables();

        // 获得接下来需要更新的用户，从用户的新进活动迹象获得
        // 活动迹象判据：打卡时间
        // 打卡时间日期范围判据：judge 的 timeoutDay
        Set<String> userIdHashSet = new HashSet<String>();
        Date dayNow = new Date();
        String dateNow = MyConstants.DATE_FORMAT.format(dayNow);
        Date dayPre = new Date(dayNow.getTime() - timeoutDay * MyConstants.SECONDS_A_DAY);
        String datePre = MyConstants.DATE_FORMAT.format(dayPre);

        List<Check> checkList = checkMapper.selectList(new EntityWrapper<Check>()
                .between("check_time", dateNow, datePre));
        for (Check check : checkList) {
            userIdHashSet.add(check.getUserId());
        }

        // 遍历用户更新勋章
        for (String userId : userIdHashSet) {
            // 更新等级勋章->累积/更迭
            // 获得判据：初始L0无勋章，累积打卡成功次数超过 2^2,2^4,2^6,2^8,2^10，分别升至L1,L2,L3,L4,L5
            updateUserLevelMedal(userId);
            // 更新专注勋章->新进
            // 获得判据：连续 MyConstants.CONCENTRATE_MEDAL_TIMES 次打卡通过
            // 丧失判据：新进 MyConstants.CONCENTRATE_MEDAL_TIMES 次打卡中，成功率低于 MyConstants.CONCENTRATE_MEDAL_LOSE_RATE
            updateUserConcentrateMedal(userId);
            // 更新达人勋章->累积
            // 获得判据：累积在某 TASK_TYPE 下达到 MyConstant.AREA_SPECIAL_MEDAL_TIMES 次打卡通过
            //         且在相应 TASK_TYPE 下打卡通过率低于 MyConstant.AREA_SPECIAL_MEDAL_LOSE_RATE
            // 反之则丧失
            updateUserAreaSpecialMedal(userId);
        }
    }

    // 初始化部分全部变量
    private void initSomeVariables() {
        // init levelMedal id and levelMedal name
        if (levelMedalIdList.isEmpty()) {
            List<Medal> medalList = medalMapper.selectList(new EntityWrapper<Medal>()
                    .eq("medal_type", MyConstants.MEDAL_LEVEL_TYPE_NAME));
            for (Medal medal : medalList) {
                levelMedalIdList.add(medal.getMedalId());
                levelMedalIdAndLevel.put(medal.getMedalName(), medal.getMedalId());
            }
        }
        // init concentrateMedal id
        if (concentrateMedalId == null || concentrateMedalId.equals("")) {
            List<Medal> medalList = medalMapper.selectList(new EntityWrapper<Medal>()
                    .eq("medal_type", MyConstants.MEDAL_CONCENTRATE_TYPE_NAME));
            if (medalList.size() > 0) concentrateMedalId = medalList.get(0).getMedalId();
        }
        // init areaSpecialMedal id
        if (areaSpecialMedalIdList.isEmpty()) {
            List<Medal> medalList = medalMapper.selectList(new EntityWrapper<Medal>()
                    .eq("medal_type", "达人勋章"));
            for (Medal medal : medalList)
                areaSpecialMedalIdList.add(medal.getMedalId());
        }
    }

    // 更新用户等级
    private void updateUserLevelMedal(String userId) {
        String levelNow = judgeUserLevel(userId);
        List<UserMedal> userMedalList = userMedalMapper.selectList(new EntityWrapper<UserMedal>()
                .in("medal_id", levelMedalIdList));
        // 新发勋章
        if (userMedalList.isEmpty() && !levelNow.equals("暂无")) {
            UserMedal userMedal = new UserMedal();
            userMedal.setUserId(userId);
            userMedal.setMedalId(levelMedalIdAndLevel.get(levelNow));
            userMedal.setTime(MyConstants.DATETIME_FORMAT.format(new Date()));
            return;
        }
        // 升级勋章
        if (!userMedalList.isEmpty()
                && !levelMedalIdAndLevel.get(levelNow).equals(userMedalList.get(0).getMedalId())) {
            UserMedal userMedal = userMedalList.get(0);
            String oldLevelId = userMedal.getMedalId();
            userMedal.setMedalId(levelMedalIdAndLevel.get(levelNow));
            userMedalMapper.update(userMedal, new EntityWrapper<UserMedal>()
                    .eq("user_id", userMedal.getUserId())
                    .eq("medal_id", oldLevelId));
        }
    }

    // 获得名字形式的用户当前应达到的等级
    private String judgeUserLevel(String userId) {
        int passNumberTotal = checkMapper.countStateNumberForSomeone(userId, MyConstants.CHECK_STATE_PASS);
        if (passNumberTotal >> 10 > 0) return MyConstants.LEVEL_MEDAL_Name_5;
        else if (passNumberTotal >> 8 > 0) return MyConstants.LEVEL_MEDAL_Name_4;
        else if (passNumberTotal >> 6 > 0) return MyConstants.LEVEL_MEDAL_Name_3;
        else if (passNumberTotal >> 4 > 0) return MyConstants.LEVEL_MEDAL_Name_2;
        else if (passNumberTotal >> 2 > 0) return MyConstants.LEVEL_MEDAL_Name_1;
        else return "暂无";
    }

    // 更新专注勋章
    private void updateUserConcentrateMedal(String userId) {
        List<UserMedal> userMedalList = userMedalMapper.selectList(new EntityWrapper<UserMedal>()
                .eq("user_id", userId)
                .eq("medal_id", concentrateMedalId));
        // 无 但新获得
        if (userMedalList.isEmpty() && ifGetConcentrateMedal(userId)) {
            UserMedal userMedal = new UserMedal();
            userMedal.setUserId(userId);
            userMedal.setMedalId(concentrateMedalId);
            userMedal.setTime(MyConstants.DATETIME_FORMAT.format(new Date()));
            userMedalMapper.insert(userMedal);
        }
        // 有 但要失去
        if (!userMedalList.isEmpty() && ifLoseConcentrateMedal(userId)) {
            userMedalMapper.delete(new EntityWrapper<UserMedal>()
                    .eq("user_id", userId)
                    .eq("medal_id", concentrateMedalId));
        }
    }

    // 是否新获得专注勋章
    private boolean ifGetConcentrateMedal(String userId) {
        List<Check> checkList = checkMapper.selectPage(checkPageConcentrate, new EntityWrapper<Check>()
                .ne("check_state", MyConstants.CHECK_STATE_UNKNOWN)
                .orderBy("check_time", false));
        // 次数不够
        if (checkList.size() != MyConstants.CONCENTRATE_MEDAL_TIMES) return false;

        for (Check check : checkList) {
            if (check.getCheckState().equals(MyConstants.CHECK_STATE_DENY))
                return false;
        }
        return true;
    }

    // 是否失去已有专注勋章
    private boolean ifLoseConcentrateMedal(String userId) {
        List<Check> checkList = checkMapper.selectPage(checkPageConcentrate, new EntityWrapper<Check>()
                .ne("check_state", MyConstants.CHECK_STATE_UNKNOWN)
                .orderBy("check_time", false));
        // 次数不够
        if (checkList.size() != MyConstants.CONCENTRATE_MEDAL_TIMES) return true;
        int passNum = 0;
        for (Check check : checkList) {
            if (check.getCheckState().equals(MyConstants.CHECK_STATE_PASS))
                ++passNum;
        }
        // return true when passNum is not enough and then lose the medal
        return passNum <= MyConstants.CONCENTRATE_MEDAL_LOSE_RATE * MyConstants.CONCENTRATE_MEDAL_TIMES;
    }

    // 更新达人勋章
    private void updateUserAreaSpecialMedal(String userId) {
        for (String oneAreaSpecialMedalId : areaSpecialMedalIdList) {
            updateUserAreaSpecialMedalAux(userId, oneAreaSpecialMedalId);
        }
    }

    // 更新达人勋章辅助函数,更新一个type的达人勋章
    private void updateUserAreaSpecialMedalAux(String userId, String medalId) {
        List<UserMedal> userMedalList = userMedalMapper.selectList(new EntityWrapper<UserMedal>()
                .eq("user_id", userId)
                .eq("medal_id", medalId));

        boolean shouldGet = ifGetAreaSpecialMedal(userId, medalId);
        // 无 但新获得
        if (userMedalList.isEmpty() && shouldGet) {
            UserMedal userMedal = new UserMedal();
            userMedal.setUserId(userId);
            userMedal.setMedalId(medalId);
            userMedal.setTime(MyConstants.DATETIME_FORMAT.format(new Date()));
            userMedalMapper.insert(userMedal);
        }
        // 有 但要失去
        if (!userMedalList.isEmpty() && !shouldGet) {
            userMedalMapper.delete(new EntityWrapper<UserMedal>()
                    .eq("user_id", userId)
                    .eq("medal_id", medalId));
        }
    }

    // 是否够资格获得达人勋章
    // 累积在某 TASK_TYPE 下达到 MyConstant.AREA_SPECIAL_MEDAL_TIMES 次打卡通过
    //
    private boolean ifGetAreaSpecialMedal(String userId, String typeId) {
        // 获得用户该 taskType 下所有 task 之 taskId
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
                .eq("user_id", userId)
                .eq("type_id", typeId));
        if (taskList.isEmpty()) return false;
        List<String> taskIdList = new ArrayList<>();
        for (Task task : taskList) {
            taskIdList.add(task.getTaskId());
        }

        List<Check> checkListPass = checkMapper.selectList(new EntityWrapper<Check>()
                .eq("check_state", MyConstants.CHECK_STATE_PASS)
                .in("task_id", taskIdList));

        List<Check> checkListDeny = checkMapper.selectList(new EntityWrapper<Check>()
                .eq("check_state", MyConstants.CHECK_STATE_DENY)
                .in("task_id", taskIdList));

        // 不能简单比较次数够不够，可能次数够了，但是通过率很差劲
        return checkListPass.size() >= MyConstants.AREA_SPECIAL_MEDAL_TIMES &&
                (checkListPass.size() * MyConstants.AREA_SPECIAL_MEDAL_LOSE_RATE) > checkListDeny.size();
    }
}
