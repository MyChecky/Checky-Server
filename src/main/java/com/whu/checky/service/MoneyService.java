package com.whu.checky.service;

import com.whu.checky.domain.MoneyFlow;

import java.util.List;

public interface MoneyService {
//    //用户支付给系统
//    void UserpayToSystem();
//    //打卡成功系统返还给用户
//    void SystemPayback();
//    //打卡失败系统分成给监督者
//    void Systemdistribute();
    //添加流水记录
    int addMoneyRecord(MoneyFlow moneyFlow);
    //查询具体的某一条流水
    MoneyFlow queryMoneyFlow(String flowId);
    //查询某个用户的所有流水
    List<MoneyFlow> queryUserMoneyFlow(String userId);
    //查询所有的流水
    List<MoneyFlow> queryAllMoneyFlow();
    //查看一段日期内系统的所有流水
    List<MoneyFlow> queryAllScopeMoneyFlow(String startDate,String endDate);
    //查看一段日期内某个用户的所有流水
    List<MoneyFlow> queryUserScopeMoneyFlow(String startDate,String endDate,String userId);
/*    //查看一段日期内系统的所有流水
    List<MoneyFlow> querySystemScopeMoneyFlow(String startDate,String endDate);*/


}
