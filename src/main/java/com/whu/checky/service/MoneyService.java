package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Pay;

import java.util.HashMap;
import java.util.List;

public interface MoneyService {
//    //用户支付给系统
//    void UserpayToSystem();
//    //打卡成功系统返还给用户
//    void SystemPayback();
//    //打卡失败系统分成给监督者
//    void Systemdistribute();
    //添加流水记录
    int addTestMoneyRecord(MoneyFlow moneyFlow);
    int addTrueMoneyRecord(Pay pay);
    //查询具体的某一条流水
    MoneyFlow queryTestMoneyFlow(String flowId);
    //查询某个用户的所有流水
    List<MoneyFlow> queryUserTestMoneyFlow(String userId);
    List<Pay> queryUserTrueMoneyFlow(String userId);
    List<MoneyFlow> queryUserTestMoneyFlow(String userId, Page page);
    //查询所有的流水
    List<MoneyFlow> queryAllTestMoneyFlow();
    List<MoneyFlow> queryAllTestMoneyFlow(Page page);
    //查看一段日期内系统的所有流水
    List<MoneyFlow> queryAllTestScopeMoneyFlow(String startDate, String endDate);
    //查看一段日期内某个用户的所有流水
    List<MoneyFlow> queryUserTestScopeMoneyFlow(String startDate, String endDate, String userId);
    List<Pay> queryUserTrueScopeMoneyPay(String startDate, String endDate, String userId);
/*    //查看一段日期内系统的所有流水
    List<MoneyFlow> querySystemScopeMoneyFlow(String startDate,String endDate);*/

    HashMap<String, Object> getTestGraphData(String year);

    List<MoneyFlow> queryTestMoneyFlowByUserName(String username);

    // 更改流水（目前用到的是支付状态与取现状态）
    int updateTrueMoneyPay(Pay pay);
    // 微信支付用
    String commitData(String openId, String payId, int total_fee);
}
