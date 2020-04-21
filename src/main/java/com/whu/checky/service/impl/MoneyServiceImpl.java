package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.config.WxPayConfig;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Pay;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.MoneyFlowMapper;
import com.whu.checky.mapper.PayMapper;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.UserMapper;
import com.whu.checky.service.MoneyService;
import com.whu.checky.util.MyConstants;
import com.whu.checky.util.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("/moneyService")
public class MoneyServiceImpl implements MoneyService {
    @Autowired
    private MoneyFlowMapper moneyFlowMapper;

    @Autowired
    private PayMapper payMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public int addTestMoneyRecord(MoneyFlow moneyFlow) {
        return moneyFlowMapper.insert(moneyFlow);
    }

    @Override
    public int addTrueMoneyRecord(Pay pay) {
        return payMapper.insert(pay);
    }

    @Override
    public MoneyFlow queryTestMoneyFlow(String flowId) {
        return moneyFlowMapper.selectById(flowId);
    }

    @Override
    public List<MoneyFlow> queryUserMoneyFlow(String userId) {
        return moneyFlowMapper.selectList(new EntityWrapper<MoneyFlow>()
                .eq("user_id", userId)
                .orderBy("flow_time", true));
    }

    @Override
    public List<Pay> queryUserTrueMoneyFlow(String payUserid) {
        return payMapper.selectList(new EntityWrapper<Pay>()
                .eq("pay_userid", payUserid)
                .orderBy("pay_time", true));
    }

//    @Override
//    public List<MoneyFlow> queryUserMoneyFlow(String userId, Page page) {
//        Wrapper<MoneyFlow> wrapper = new EntityWrapper<MoneyFlow>()
//                .eq("user_id", userId)
//                .orderBy("flow_time", true);
//        if (page == null)
//            return moneyFlowMapper.getMoneyFlowsWithName(wrapper);
//        else
//            return moneyFlowMapper.getMoneyFlowsWithName(wrapper, page);
//    }
//
//    @Override
//    public List<MoneyFlow> queryAllMoneyFlow() {
//        return moneyFlowMapper.selectList(new EntityWrapper<MoneyFlow>()
//                .orderBy("flow_time", true))
//                ;
//    }
//
//    @Override
//    public List<MoneyFlow> queryAllMoneyFlow(Page page) {
//        Wrapper<MoneyFlow> wrapper = new EntityWrapper<MoneyFlow>()
//                .orderBy("flow_time", true);
//
//        if (page != null) return moneyFlowMapper.getMoneyFlowsWithName(wrapper, page);
//        else return moneyFlowMapper.getMoneyFlowsWithName(wrapper);
//    }

    @Override
    public List<MoneyFlow> queryAllScopeMoneyFlow(String startDate, String endDate) {
        return moneyFlowMapper.queryAllScopeMoneyFlow(startDate, endDate);
    }

    @Override
    public List<Pay> rechargeList(Page<Pay> page, boolean isAsc) {
        List<Pay> pays = payMapper.selectPage(page, new EntityWrapper<Pay>()
                .orderBy("pay_time", isAsc));
        for(Pay pay: pays){
            pay.setPayUserName(userMapper.selectById(pay.getPayUserid()).getUserName());
        }
        return pays;
    }

    @Override
    public List<Pay> rechargeUser(String userId, Page<Pay> page, boolean isAsc) {
        List<Pay> pays = payMapper.selectPage(page, new EntityWrapper<Pay>()
                .eq("pay_userid", userId)
                .orderBy("pay_time", isAsc));
        for(Pay pay: pays){
            pay.setPayUserName(userMapper.selectById(pay.getPayUserid()).getUserName());
        }
        return pays;
    }

    @Override
    public List<MoneyFlow> queryUserMoneyFlowWithName(String userId, Page<MoneyFlow> page, boolean isAsc) {
        List<MoneyFlow> moneyFlows = moneyFlowMapper.selectPage(page, new EntityWrapper<MoneyFlow>()
                .eq("user_id", userId)
                .orderBy("flow_time", isAsc));
        for(MoneyFlow moneyFlow: moneyFlows){
            moneyFlow.setUserName(userMapper.selectById(moneyFlow.getUserID()).getUserName());
        }
        return moneyFlows;
    }

    @Override
    public List<MoneyFlow> queryAllMoneyFlows(Page<MoneyFlow> page, boolean isAsc) {
        List<MoneyFlow> moneyFlows = moneyFlowMapper.selectPage(page, new EntityWrapper<MoneyFlow>()
                .orderBy("flow_time", isAsc));
        for(MoneyFlow moneyFlow: moneyFlows){
            moneyFlow.setUserName(userMapper.selectById(moneyFlow.getUserID()).getUserName());
        }
        return moneyFlows;
    }

    @Override
    public int querySizeOfUserMoneyFlowWithName(String userId) {
        return moneyFlowMapper.selectCount(new EntityWrapper<MoneyFlow>()
                .eq("user_id", userId));
    }

    @Override
    public int querySizeOfAllMoneyFlow() {
        return moneyFlowMapper.selectCount(new EntityWrapper<>());
    }

    @Override
    public int rechargeUserSize(String userId) {
        return payMapper.selectCount(new EntityWrapper<Pay>()
                .eq("pay_userid", userId));
    }

    @Override
    public int rechargeListSize() {
        return payMapper.selectCount(new EntityWrapper<>());
    }

    @Override
    public double[] getUserTotalMoneys(String userId) {
        double[] userTotalMoneys = {0.0, 0.0, 0.0, 0.0}; // totalTrueOut, totalTrueIn, totalTestOut, totalTestIn
        String startDay = "1970-01-01";
        String endDay = sdf.format(new Date());
        List<MoneyFlow> moneyFlows = moneyFlowMapper.queryUserScopeMoneyFlow(startDay, endDay, userId);
        for(MoneyFlow moneyFlow: moneyFlows){
            if(moneyFlow.getIfTest() == 0){ // 真实余额 数字0
                if(moneyFlow.getFlowIo().equals("O")){ // 字母 O
                    userTotalMoneys[0] += moneyFlow.getFlowMoney();
                }else{
                    userTotalMoneys[1] += moneyFlow.getFlowMoney();
                }
            }else{ // 测试余额
                if(moneyFlow.getFlowIo().equals("O")){ // 字母 O
                    userTotalMoneys[2] += moneyFlow.getFlowMoney();
                }else{
                    userTotalMoneys[3] += moneyFlow.getFlowMoney();
                }
            }
        }
        return userTotalMoneys;
    }

    @Override
    public List<MoneyFlow> queryMoneyflowsForAdmin(Page<MoneyFlow> p, String startTime, String endTime,
                                                   String moneyType, String moneyIO, int moneyTest, String keyword) {
        List<String> moneyTypes = new ArrayList<>();
        List<String> moneyIOs = new ArrayList<>();
        List<Integer> moneyTests = new ArrayList<>();
        if(moneyType.equals(MyConstants.MONEY_FLOW_TYPE_ALL)){
            moneyTypes.add(MyConstants.MONEY_FLOW_TYPE_PAY);
            moneyTypes.add(MyConstants.MONEY_FLOW_TYPE_REFUND);
            moneyTypes.add(MyConstants.MONEY_FLOW_TYPE_BENEFIT);
        }else{
            moneyTypes.add(moneyType);
        }
        if(moneyIO.equals(MyConstants.MONEY_FLOW_IO_ALL)){
            moneyIOs.add(MyConstants.MONEY_FLOW_IN);
            moneyIOs.add(MyConstants.MONEY_FLOW_OUT);
        }else{
            moneyIOs.add(moneyIO);
        }
        if(moneyTest == MyConstants.MONEY_TEST_ALL){
            moneyTests.add(MyConstants.MONEY_TEST_TRUE);
            moneyTests.add(MyConstants.MONEY_TEST_FALSE);
        }else{
            moneyTests.add(moneyTest);
        }

        if(keyword == null || keyword.equals("")){
            List<MoneyFlow> moneyFlows = moneyFlowMapper.selectPage(p, new EntityWrapper<MoneyFlow>()
                    .ge("flow_time", startTime)
                    .le("flow_time", endTime)
                    .in("if_test", moneyTests)
                    .in("flow_io", moneyIOs)
                    .in("flow_type", moneyTypes));
            for(MoneyFlow moneyFlow: moneyFlows){
                moneyFlow.setUserName(userMapper.getUsernameById(moneyFlow.getUserID()));
            }
            return moneyFlows;
        }else{
            List<User> users = userMapper.selectList(new EntityWrapper<User>().like("user_name", keyword));
            List<String> userIds = new ArrayList<>();
            for (User user : users) {
                userIds.add(user.getUserId());
            }
            List<MoneyFlow> moneyFlows = moneyFlowMapper.selectPage(p, new EntityWrapper<MoneyFlow>()
                    .ge("flow_time", startTime)
                    .le("flow_time", endTime)
                    .in("user_id", userIds)
                    .in("if_test", moneyTests)
                    .in("flow_io", moneyIOs)
                    .in("flow_type", moneyTypes));
            for(MoneyFlow moneyFlow: moneyFlows){
                moneyFlow.setUserName(userMapper.getUsernameById(moneyFlow.getUserID()));
            }
            return moneyFlows;
        }

    }

    @Override
    public List<Pay> queryPaysForAdmin(Page<MoneyFlow> p, String startTime, String endTime, String payType, String keyword) {
        List<String> payTypes = new ArrayList<>();
        if(payType.equals(MyConstants.PAY_TYPE_ALL)){
            payTypes.add(MyConstants.PAY_TYPE_WITHDRAW);
            payTypes.add(MyConstants.PAY_TYPE_PAY);
        }else{
            payTypes.add(payType);
        }
        if(keyword == null || keyword.equals("")){
            List<Pay> pays = payMapper.selectPage(p, new EntityWrapper<Pay>()
                    .ge("pay_time", startTime)
                    .le("pay_time", endTime)
                    .in("pay_type", payTypes));
            for(Pay pay: pays){
                pay.setPayUserName(userMapper.getUsernameById(pay.getPayUserid()));
            }
            return pays;
        }else{
            List<User> users = userMapper.selectList(new EntityWrapper<User>().like("user_name", keyword));
            List<String> userIds = new ArrayList<>();
            for (User user : users) {
                userIds.add(user.getUserId());
            }
            List<Pay> pays = payMapper.selectPage(p, new EntityWrapper<Pay>()
                    .ge("pay_time", startTime)
                    .le("pay_time", endTime)
                    .in("pay_userid", userIds)
                    .in("pay_type", payTypes));
            for(Pay pay: pays){
                pay.setPayUserName(userMapper.getUsernameById(pay.getPayUserid()));
            }
            return pays;
        }
    }

    @Override
    public List<MoneyFlow> queryUserTestScopeMoneyFlow(String startDate, String endDate, String userId) {
        return moneyFlowMapper.queryUserScopeMoneyFlow(startDate, endDate, userId);
    }

    @Override
    public List<Pay> queryUserTrueScopeMoneyPay(String startDate, String endDate, String userId) {
        return payMapper.queryUserScopePay(startDate, endDate, userId);
    }

    @Override
    public HashMap<String, Object> getUserGraphData(String year, String userId) {
        List<MoneyFlow> moneyFlows = moneyFlowMapper.selectList(new EntityWrapper<MoneyFlow>()
                .ge("flow_time", year + "-01-01")
                .le("flow_time", year + "-12-31")
                .eq("user_id", userId));
        List<Double> testIncomeList = new ArrayList<Double>();
        List<Double> testRefundList = new ArrayList<>();
        List<Double> testBenefitList = new ArrayList<>();
        List<Double> trueIncomeList = new ArrayList<Double>();
        List<Double> trueRefundList = new ArrayList<>();
        List<Double> trueBenefitList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            testIncomeList.add(0.0);
            testRefundList.add(0.0);
            testBenefitList.add(0.0);
            trueIncomeList.add(0.0);
            trueRefundList.add(0.0);
            trueBenefitList.add(0.0);
        }

        for (MoneyFlow m : moneyFlows) {
            try {
                int month = Integer.parseInt(m.getFlowTime().substring(5, 7)) - 1;
//                int month = sdf.parse(m.getFlowTime()).getMonth();
                if (m.getFlowIo().equals(MyConstants.MONEY_FLOW_OUT)) {// flowIO为O,仅支出,尚未结算，需等结算
                    if (m.getIfTest() == MyConstants.MONEY_TEST_FALSE) {// t num 0 here
                        if(taskMapper.selectById(m.getTaskId()).getTaskState().equals(MyConstants.TASK_STATE_COMPLETE)){
                            trueIncomeList.set(month, m.getFlowMoney() + trueIncomeList.get(month));
                        }
                    }
                    else{
                        if(taskMapper.selectById(m.getTaskId()).getTaskState().equals(MyConstants.TASK_STATE_COMPLETE))
                            testIncomeList.set(month, m.getFlowMoney() + testIncomeList.get(month));
                    }
                } else { // 入账，说明已结算，无需判别任务状态
                    if (m.getIfTest() == MyConstants.MONEY_TEST_FALSE)// t num 0 here
                        trueRefundList.set(month, m.getFlowMoney() + trueRefundList.get(month));
                    else
                        testRefundList.set(month, m.getFlowMoney() + testRefundList.get(month));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 12; i++) {
            testBenefitList.set(i, testIncomeList.get(i) - testRefundList.get(i));
            trueBenefitList.set(i, trueIncomeList.get(i) - trueRefundList.get(i));
        }
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("testIncomeList", testIncomeList);
        ret.put("testRefundList", testRefundList);
        ret.put("testBenefitList", testBenefitList);
        ret.put("trueIncomeList", trueIncomeList);
        ret.put("trueRefundList", trueRefundList);
        ret.put("trueBenefitList", trueBenefitList);
        return ret;
    }

    @Override
    public HashMap<String, Object> getAllGraphData(String year) {
        List<MoneyFlow> moneyFlowList = queryAllScopeMoneyFlow(year + "-01-01", year + "-12-31");
        List<Double> testIncomeList = new ArrayList<Double>();
        List<Double> testRefundList = new ArrayList<>();
        List<Double> testBenefitList = new ArrayList<>();
        List<Double> trueIncomeList = new ArrayList<Double>();
        List<Double> trueRefundList = new ArrayList<>();
        List<Double> trueBenefitList = new ArrayList<>();

        Double total_1_O = moneyFlowMapper.selectSum(MyConstants.MONEY_TEST_TRUE, MyConstants.MONEY_FLOW_OUT);
        Double total_1_I = moneyFlowMapper.selectSum(MyConstants.MONEY_TEST_TRUE, MyConstants.MONEY_FLOW_IN);
        Double total_0_O = moneyFlowMapper.selectSum(MyConstants.MONEY_TEST_FALSE, MyConstants.MONEY_FLOW_OUT);
        Double total_0_I = moneyFlowMapper.selectSum(MyConstants.MONEY_TEST_FALSE, MyConstants.MONEY_FLOW_IN);
        // 前期没数据会有null的
        if(total_1_O==null)
            total_1_O = 0.0;
        if(total_1_I==null)
            total_1_I = 0.0;
        if(total_0_O==null)
            total_0_O = 0.0;
        if(total_0_I==null)
            total_0_I = 0.0;

        Double payPay = payMapper.selectSum(MyConstants.PAY_TYPE_PAY);
        Double payWithdraw = payMapper.selectSum(MyConstants.PAY_TYPE_WITHDRAW);
        if(payPay == null)
            payPay = 0.0;
        if(payWithdraw == null)
            payWithdraw = 0.0;

        for (int i = 0; i < 12; i++) {
            testIncomeList.add(0.0);
            testRefundList.add(0.0);
            testBenefitList.add(0.0);
            trueIncomeList.add(0.0);
            trueRefundList.add(0.0);
            trueBenefitList.add(0.0);
        }

        for (MoneyFlow m : moneyFlowList) {
            try {
                int month = Integer.parseInt(m.getFlowTime().substring(5, 7)) - 1;
//                int month = sdf.parse(m.getFlowTime()).getMonth();
                if (m.getFlowIo().equals("O")) {// flowIO为O,仅支出,尚未结算，需等结算
                    if (m.getIfTest() == 0) {// t num 0 here
                        if(taskMapper.selectById(m.getTaskId()).getTaskState().equals(MyConstants.TASK_STATE_COMPLETE)){
                            trueIncomeList.set(month, m.getFlowMoney() + trueIncomeList.get(month));
                        }
                    }
                    else{
                        if(taskMapper.selectById(m.getTaskId()).getTaskState().equals(MyConstants.TASK_STATE_COMPLETE))
                            testIncomeList.set(month, m.getFlowMoney() + testIncomeList.get(month));
                    }
                } else { // 入账，说明已结算，无需判别任务状态
                    if (m.getIfTest() == 0)// t num 0 here
                        trueRefundList.set(month, m.getFlowMoney() + trueRefundList.get(month));
                    else
                        testRefundList.set(month, m.getFlowMoney() + testRefundList.get(month));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 12; i++) {
            testBenefitList.set(i, testIncomeList.get(i) - testRefundList.get(i));
            trueBenefitList.set(i, trueIncomeList.get(i) - trueRefundList.get(i));
        }
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("testIncomeList", testIncomeList);
        ret.put("testRefundList", testRefundList);
        ret.put("testBenefitList", testBenefitList);
        ret.put("trueIncomeList", trueIncomeList);
        ret.put("trueRefundList", trueRefundList);
        ret.put("trueBenefitList", trueBenefitList);
        ret.put("totalSystemTestGet", total_1_O - total_1_I);
        ret.put("totalSystemTrueGet", total_0_O - total_0_I);
        ret.put("totalPayGet", payPay - payWithdraw);
        return ret;
    }

    @Override
    public List<MoneyFlow> queryMoneyFlowByUserName(String username) {
        return moneyFlowMapper.queryMoneyFolwByUserName(username);
    }

    @Override
    public int updateTrueMoneyPay(Pay pay) {
        return payMapper.updateById(pay);
    }

    @Override
    public String commitData(String openId, String payId, int total_fee) {
        String nonceStr = WXPayUtil.generateUUID();
        String body = "JSAPI支付测试";
        Map<String, String> packageParams = new HashMap<String, String>();
        packageParams.put("appid", WxPayConfig.appId);
        packageParams.put("body", body);
        packageParams.put("mch_id", WxPayConfig.mchId);
        packageParams.put("nonce_str", nonceStr);
        packageParams.put("notify_url", WxPayConfig.notifyUrl);//支付成功后的回调地址
        packageParams.put("openid", openId + "");//支付方式
        packageParams.put("out_trade_no", payId);//商户订单号
        packageParams.put("sign_type", WxPayConfig.signType);
        packageParams.put("spbill_create_ip", "127.0.0.1");
        packageParams.put("total_fee", Integer.toString(total_fee));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        packageParams.put("trade_type", WxPayConfig.tradeType);//支付方式
        String sign = "";
        try {
            sign = WXPayUtil.generateSignature(packageParams, WxPayConfig.sandBoxKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String formData = "<xml>";
        formData += "<appid>" + WxPayConfig.appId + "</appid>"; //appid
        formData += "<body>" + body + "</body>";
        formData += "<mch_id>" + WxPayConfig.mchId + "</mch_id>"; //商户号
        formData += "<nonce_str>" + nonceStr + "</nonce_str>";
        formData += "<notify_url>" + WxPayConfig.notifyUrl + "</notify_url>";
        formData += "<openid>" + openId + "</openid>";
        formData += "<out_trade_no>" + payId + "</out_trade_no>";
        formData += "<sign_type>" + WxPayConfig.signType + "</sign_type>";
        formData += "<spbill_create_ip>" + "127.0.0.1" + "</spbill_create_ip>";
        formData += "<total_fee>" + Integer.toString(total_fee) + "</total_fee>";
        formData += "<trade_type>" + WxPayConfig.tradeType + "</trade_type>";
        formData += "<sign>" + sign + "</sign>";
        formData += "</xml>";
        return formData;
    }

}
