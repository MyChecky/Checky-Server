package com.whu.checky.service;

import com.whu.checky.domain.Medal;

import java.util.List;
import java.util.Map;

public interface MedalService {
    //根据用户ID查询所有Medal
    Map<String,Medal> getMedalsByUserId(String userId);
    //根据用户ID和类型查询达人Medal
    Medal getMedalByUserIdAndType(String userId, String medalType);
    //进行更新medal操作
    String updateTalentMedal(Medal medal);
    //更新专注勋章
    String updateConcentrateMedal(Medal medal,String check_fre);
    //新增达人勋章
    String addOrUpdateTalenMedal(String userId, String typeId, String taskType,Medal ownTalentMedal) throws Exception;
    //检查是否有等级勋章
    Medal checkRankMedal(String userId);
    //新增等级勋章
    String addOrUpdateRankMedal(String userId,Medal ownRankMedal) throws Exception;
    //检查是否有专注勋章
    Medal checkConcentrateMedal(String userId);
    //新增专注勋章
    String addConcentrateMedal(String userId) throws Exception;
    //
    String addOrUpdateConcentrateMedal(String userId,Medal ownConcentrateMedal) throws Exception;

    List<Medal> getMedalListByUserId(String userId);
}
