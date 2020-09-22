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

    // 这是wechat用的
    //添加流水记录
    int addTestMoneyRecord(MoneyFlow moneyFlow);
    int addTrueMoneyRecord(Pay pay);
    //查询具体的某一条流水
    MoneyFlow queryTestMoneyFlow(String flowId);
    //查询某个用户的所有流水
    List<MoneyFlow> queryUserMoneyFlow(String userId);
    List<Pay> queryUserTrueMoneyFlow(String userId);
    //查看一段日期内某个用户的所有流水
    List<MoneyFlow> queryUserTestScopeMoneyFlow(String startDate, String endDate, String userId);
    List<Pay> queryUserTrueScopeMoneyPay(String startDate, String endDate, String userId);
/*    //查看一段日期内系统的所有流水
    List<MoneyFlow> querySystemScopeMoneyFlow(String startDate,String endDate);*/
    // 更改流水（目前用到的是支付状态与取现状态）
    int updateTrueMoneyPay(Pay pay);
    // 微信支付用
    String commitData(String openId, String payId, int total_fee);
    //根据PAY_ORDERINFO更新
    int updateMoneyPayByPayOrderInfo(String payOrderinfo);
    // 这是admin用的
//    List<MoneyFlow> queryAllMoneyFlow();
//    List<MoneyFlow> queryAllMoneyFlow(Page page);
//    List<MoneyFlow> queryUserMoneyFlow(String userId, Page page);
    // 查询年度流水数据，供图表显示
    HashMap<String, Object> getAllGraphData(String year);
    HashMap<String, Object> getUserGraphData(String year, String userId);
    // 根据用户名模糊查询流水记录
    List<MoneyFlow> queryMoneyFlowByUserName(String username);
    //查看一段日期内系统的所有流水
    List<MoneyFlow> queryAllScopeMoneyFlow(String startDate, String endDate);
    // 是以page查全部人的冲值变化
    List<Pay> rechargeList(Page<Pay> page, boolean isAsc);
    // 是以page和userId查某人的充值记录
    List<Pay> rechargeUser(String userId, Page<Pay> page, boolean isAsc);
    // 是以page和userId查某人的资金流动
    List<MoneyFlow> queryUserMoneyFlowWithName(String userId, Page<MoneyFlow> page, boolean isAsc);
    // 以page查全部人的资金流动
    List<MoneyFlow> queryAllMoneyFlows(Page<MoneyFlow> page, boolean isAsc);

    int querySizeOfUserMoneyFlowWithName(String userId);

    int querySizeOfAllMoneyFlow();

    int rechargeUserSize(String userId);

    int rechargeListSize();

    double[] getUserTotalMoneys(String userId);

    List<MoneyFlow> queryMoneyflowsForAdmin(Page<MoneyFlow> p, String startTime, String endTime, String moneyType, String moneyIO, int moneyTest, String keyword);

    List<Pay> queryPaysForAdmin(Page<MoneyFlow> p, String startTime, String endTime, String payType, String keyword);
}
