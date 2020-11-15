package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Medal;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.UserMedal;
import com.whu.checky.mapper.MedalMapper;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.UserMedalMapper;
import com.whu.checky.service.MedalService;
import com.whu.checky.service.TaskService;
import com.whu.checky.util.MyConstants;
import com.whu.checky.util.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("/medalService")
public class MedalServiceImpl implements MedalService {
    @Autowired
    private MedalMapper medalMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserMedalMapper userMedalMapper;

    @Override
    public Map<String, Medal> getMedalsByUserId(String userId) {
        Map<String, Medal> ret = new HashMap<String, Medal>();
        List<Medal> medalList = medalMapper.selectList(new EntityWrapper<Medal>().eq("user_id", userId));
        for (int i = 0; i < medalList.size(); i++) {
            ret.put(medalList.get(i).getMedalType(), medalList.get(i));
        }
        return ret;
    }

    @Override
    public Medal getMedalByUserIdAndType(String userId, String medalType) {
        List<Medal> ret = medalMapper.selectList(new EntityWrapper<Medal>().eq("user_id", userId)
                .and()
                .eq("medal_type", medalType));
        if (ret != null) {
            return ret.get(0);
        }
        return null;
    }

    @Override
    public String updateTalentMedal(Medal medal) {
//        medal.setIfExpired("0");
//        medal.setExpireTime(MyStringUtil.nextMonth());
        if (medalMapper.updateById(medal) == 1) {
            return "勋章状态更新成功";
        } else {
            return "勋章状态更新失败";
        }
    }


    @Override
    public String addOrUpdateTalenMedal(String userId, String typeId, String taskType, Medal ownTalentMedal) throws Exception {
        /**
         * 任务筛选条件：1.用户2.类型3.非失败4.开始时间在一年以内
         * */
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
                .eq("user_id", userId)
                .eq("type_id", typeId)
                .ne("task_state", MyConstants.TASK_STATE_FAIL)
        );
        int sum = 0;
        for (int i = 0; i < taskList.size(); i++) {
            if (MyStringUtil.checkIsInOneYear(taskList.get(i).getTaskStartTime())) {
                sum += taskList.get(i).getCheckNum();
            }
        }
        //如果目前不存在则考虑新增
        if (ownTalentMedal == null) {
            //如果符合标准，则添加任务类型对应的达人勋章
            if (sum >= MyConstants.TALENT_STANDART) {
                Medal newTalentMedal = new Medal();
                newTalentMedal.setMedalId(MyStringUtil.getUUID());
                newTalentMedal.setMedalType(taskType);
                newTalentMedal.setMedalUrl(MyConstants.TALENT_URL);
//                newTalentMedal.setExpireTime(MyStringUtil.nextMonth());
//                newTalentMedal.setIfExpired("0");
                if (medalMapper.insert(newTalentMedal) == 1) {
                    return "符合" + taskType + "达人勋章标准并以授予";
                } else {
                    return "符合" + taskType + "达人勋章 ！但是授予失败！";
                }
            }
            //不符合标准
            else {
                return "不符合标准，不予授予达人勋章";
            }
        }
        //如果已经存在，考虑更新或者删除
        else {
            //如果不符合了，就删除
            if (sum < MyConstants.TALENT_STANDART) {
                medalMapper.deleteById(ownTalentMedal.getMedalId());
                return "不符合达人勋章标准，已删除";
            }
            //如果符合标准就更新
            /**
             * 更新就是将过期时间延后一个月
             */
            else {
//                ownTalentMedal.setIfExpired("0");
//                ownTalentMedal.setExpireTime(MyStringUtil.nextMonth());
                if (medalMapper.updateById(ownTalentMedal) == 1) {
                    return "勋章状态更新成功";
                } else {
                    return "勋章状态更新失败";
                }
            }
        }
    }

    @Override
    public Medal checkRankMedal(String userId) {
        List<Medal> rankMedalList = medalMapper.selectList(new EntityWrapper<Medal>()
                .eq("user_id", userId)
                .andNew()
                .eq("medal_type", MyConstants.RANK1)
                .or()
                .eq("medal_type", MyConstants.RANK2)
                .or()
                .eq("medal_type", MyConstants.RANK3)
                .or()
                .eq("medal_type", MyConstants.RANK4)
                .or()
                .eq("medal_type", MyConstants.RANK5)
        );
        if (rankMedalList == null) {
            return null;
        } else {
            return rankMedalList.get(0);
        }
    }

    @Override
    public String addOrUpdateRankMedal(String userId, Medal ownRankMedal) throws Exception {
        /**
         * 查找一年内该用户所有的打卡数
         */
        List<Task> taskList = taskMapper.selectList(new EntityWrapper<Task>()
                .eq("user_id", userId));
        int sum = 0;
        for (int i = 0; i < taskList.size(); i++) {
            if (MyStringUtil.checkIsInOneYear(taskList.get(i).getTaskStartTime())) {
                sum += taskList.get(i).getCheckNum();
            }
        }
        //如果不存在就考虑新增
        if (ownRankMedal == null) {
            Medal newRankMedal = new Medal();
//                newRankMedal.setUserId(userId);
            newRankMedal.setMedalId(MyStringUtil.getUUID());
//                newRankMedal.setExpireTime(MyStringUtil.nextMonth());
//                newRankMedal.setIfExpired("0");
            if (sum >= 100) {
                newRankMedal.setMedalType(MyConstants.RANK5);
                newRankMedal.setMedalUrl(MyConstants.RANK5_URL);
                medalMapper.insert(newRankMedal);
                return "获得砖石等级勋章";
            } else if (sum >= 80) {
                newRankMedal.setMedalType(MyConstants.RANK4);
                newRankMedal.setMedalUrl(MyConstants.RANK4_URL);
                medalMapper.insert(newRankMedal);
                return "获得黑金等级勋章";
            } else if (sum >= 60) {
                newRankMedal.setMedalType(MyConstants.RANK3);
                newRankMedal.setMedalUrl(MyConstants.RANK3_URL);
                medalMapper.insert(newRankMedal);
                return "获得黄金等级勋章";
            } else if (sum >= 40) {
                newRankMedal.setMedalType(MyConstants.RANK2);
                newRankMedal.setMedalUrl(MyConstants.RANK2_URL);
                medalMapper.insert(newRankMedal);
                return "获得白银等级勋章";
            } else if (sum >= 20) {
                newRankMedal.setMedalType(MyConstants.RANK1);
                newRankMedal.setMedalUrl(MyConstants.RANK1_URL);
                medalMapper.insert(newRankMedal);
                return "获得黄铜等级勋章";
            } else {
                return "不符合标准，暂无法授予等级勋章";
            }

        }
        //如果存在则进行更新或删除
        else {
//            ownRankMedal.setIfExpired("0");
//            ownRankMedal.setExpireTime(MyStringUtil.nextMonth());
            if (sum >= 100) {
                ownRankMedal.setMedalType(MyConstants.RANK5);
                ownRankMedal.setMedalUrl(MyConstants.RANK5_URL);
                medalMapper.updateById(ownRankMedal);
                return "等级勋章更新为黄铜勋章";
            } else if (sum >= 80) {
                ownRankMedal.setMedalType(MyConstants.RANK4);
                ownRankMedal.setMedalUrl(MyConstants.RANK4_URL);
                medalMapper.updateById(ownRankMedal);
                return "等级勋章更新为砖石勋章";
            } else if (sum >= 60) {
                ownRankMedal.setMedalType(MyConstants.RANK3);
                ownRankMedal.setMedalUrl(MyConstants.RANK3_URL);
                medalMapper.updateById(ownRankMedal);
                return "等级勋章更新为砖石勋章";
            } else if (sum >= 40) {
                ownRankMedal.setMedalType(MyConstants.RANK2);
                ownRankMedal.setMedalUrl(MyConstants.RANK2_URL);
                medalMapper.updateById(ownRankMedal);
                return "等级勋章更新为砖石勋章";
            } else if (sum >= 20) {
                ownRankMedal.setMedalType(MyConstants.RANK1);
                ownRankMedal.setMedalUrl(MyConstants.RANK1_URL);
                medalMapper.updateById(ownRankMedal);
                return "等级勋章更新为砖石勋章";
            } else {
                medalMapper.deleteById(ownRankMedal.getMedalId());
                return "不符合获得等级勋章的要求";
            }
        }
    }

    @Override
    public Medal checkConcentrateMedal(String userId) {
        List<Medal> ret = medalMapper.selectList(new EntityWrapper<Medal>().eq("user_id", userId)
                .and()
                .eq("medal_type", MyConstants.CONCENTRATE));
        if (ret != null) {
            return ret.get(0);
        }
        return null;
    }

    @Override
    public String addConcentrateMedal(String userId) throws Exception {
        Medal newConcentrateMedal = new Medal();
        newConcentrateMedal.setMedalId(MyStringUtil.getUUID());
//        newConcentrateMedal.setUserId(userId);
        newConcentrateMedal.setMedalType(MyConstants.CONCENTRATE);
        newConcentrateMedal.setMedalUrl(MyConstants.TALENT_URL);
//        newConcentrateMedal.setExpireTime(MyStringUtil.nextMonth());
//        newConcentrateMedal.setIfExpired("0");
        medalMapper.insert(newConcentrateMedal);
        return "新增专注勋章";
    }

    @Override
    public String updateConcentrateMedal(Medal medal, String check_fre) {
        /// todo
        //要求一周内没有失败任务&&本周内打卡次数符合要求
//        if (!taskService.checkFailTaskWeekly(medal.getUserId())&&taskService.checkFreWeekly(medal.getUserId()))
//        {
//            medal.setIfExpired("0");
//            medal.setExpireTime(MyStringUtil.nextMonth());
//            if (medalMapper.updateById(medal) == 1) {
//                return "勋章状态更新成功";
//            } else {
//                return "勋章状态更新失败";
//            }
//        }
        //否则删除勋章
//        else
//        {
//            medalMapper.deleteById(medal.getMedalId());
//            return "条件不满足，已删除专注勋章";
//        }
        return null;
    }

    @Override
    public String addOrUpdateConcentrateMedal(String userId, Medal ownConcentrateMedal) throws Exception {
        if (ownConcentrateMedal == null) {
            if (taskService.checkFailTaskWeekly(userId)) {
                return "近一周有失败任务，无法获取专注勋章";
            } else {
                //如果原本没有专注勋章且近一周没有失败任务，则进行新增
                if (!taskService.checkFreWeekly(userId)) {
                    return "本周内有任务没有连续打卡，不予授予";
                } else {
                    Medal newConcentrateMedal = new Medal();
                    newConcentrateMedal.setMedalId(MyStringUtil.getUUID());
//                        newConcentrateMedal.setUserId(userId);
//                        newConcentrateMedal.setMedalType(MyConstants.CONCENTRATE);
//                        newConcentrateMedal.setMedalUrl(MyConstants.TALENT_URL);
//                        newConcentrateMedal.setExpireTime(MyStringUtil.nextMonth());
//                        newConcentrateMedal.setIfExpired("0");
                    medalMapper.insert(newConcentrateMedal);
                    return "新增专注勋章";
                }
            }
        } else {
            //要求一周内没有失败任务&&本周内打卡次数符合要求
//            if (!taskService.checkFailTaskWeekly(ownConcentrateMedal.getUserId())&&taskService.checkFreWeekly(ownConcentrateMedal.getUserId()))
//            {
//                ownConcentrateMedal.setIfExpired("0");
//                ownConcentrateMedal.setExpireTime(MyStringUtil.nextMonth());
//                medalMapper.updateById(ownConcentrateMedal);
//                    return "勋章状态更新成功";
//            }
//            //否则删除勋章
//            else
//            {
//                medalMapper.deleteById(ownConcentrateMedal.getMedalId());
//                return "条件不满足，已删除专注勋章";
//            }
        }
        return null;
    }

    @Override
    public List<Medal> getMedalListByUserId(String userId) {
        List<Medal> medalListRes = new ArrayList<>();

        List<UserMedal> userMedalList = userMedalMapper.selectList(new EntityWrapper<UserMedal>()
                .eq("user_id", userId));
        if (userMedalList.isEmpty()) return medalListRes;

        List<String> medalIdList = new ArrayList<>();
        for (UserMedal userMedal : userMedalList)
            medalIdList.add(userMedal.getMedalId());
        return medalMapper.selectList(new EntityWrapper<Medal>()
                .in("medal_id", medalIdList));
    }
}
