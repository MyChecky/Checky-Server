package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.config.WxPayConfig;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Pay;
import com.whu.checky.mapper.MoneyFlowMapper;
import com.whu.checky.mapper.PayMapper;
import com.whu.checky.service.MoneyService;
import com.whu.checky.util.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("/moneyService")
public class MoneyServiceImpl implements MoneyService {
    @Autowired
    private MoneyFlowMapper moneyFlowMapper;

    @Autowired
    private PayMapper payMapper;

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
    public List<MoneyFlow> queryUserTestMoneyFlow(String userId) {
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

    @Override
    public List<MoneyFlow> queryUserTestMoneyFlow(String userId, Page page) {
        Wrapper<MoneyFlow> wrapper = new EntityWrapper<MoneyFlow>()
                .eq("user_id", userId)
                .orderBy("flow_time", true);
        if (page == null)
            return moneyFlowMapper.getMoneyFlowsWithName(wrapper);
        else
            return moneyFlowMapper.getMoneyFlowsWithName(wrapper, page);
    }

    @Override
    public List<MoneyFlow> queryAllTestMoneyFlow() {
        return moneyFlowMapper.selectList(new EntityWrapper<MoneyFlow>()
                .orderBy("flow_time", true))
                ;
    }

    @Override
    public List<MoneyFlow> queryAllTestMoneyFlow(Page page) {
        Wrapper<MoneyFlow> wrapper = new EntityWrapper<MoneyFlow>()
                .orderBy("flow_time", true);

        if (page != null) return moneyFlowMapper.getMoneyFlowsWithName(wrapper, page);
        else return moneyFlowMapper.getMoneyFlowsWithName(wrapper);
    }

    @Override
    public List<MoneyFlow> queryAllTestScopeMoneyFlow(String startDate, String endDate) {
        return moneyFlowMapper.queryAllScopeMoneyFlow(startDate, endDate);
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
    public HashMap<String, Object> getTestGraphData(String year) {
        List<MoneyFlow> moneyFlowList = queryAllTestScopeMoneyFlow(year + "-01-01", year + "-12-31");
        List<Double> incomeList = new ArrayList<Double>();
        List<Double> refundList = new ArrayList<>();
        List<Double> benefitList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            incomeList.add(0.0);
            refundList.add(0.0);
            benefitList.add(0.0);
        }

        for (MoneyFlow m : moneyFlowList) {
            try {
                int month = sdf.parse(m.getFlowTime()).getMonth();
                if (m.getFlowIo().equals("O")) {// not num 0 here
                    incomeList.set(month - 1, m.getFlowMoney() + incomeList.get(month - 1));
                } else {
                    refundList.set(month - 1, m.getFlowMoney() + incomeList.get(month - 1));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 12; i++) {
            benefitList.set(i, incomeList.get(i) - refundList.get(i));
        }
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("income", incomeList);
        ret.put("refund", refundList);
        ret.put("benefit", benefitList);
        return ret;
    }

    @Override
    public List<MoneyFlow> queryTestMoneyFlowByUserName(String username) {
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
