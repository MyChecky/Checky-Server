package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.config.WxPayConfig;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Pay;
import com.whu.checky.domain.User;
import com.whu.checky.service.MoneyService;
import com.whu.checky.service.TaskService;
import com.whu.checky.service.UserService;
import com.whu.checky.util.MyConstants;
import com.whu.checky.util.WXPayUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/money")
public class MoneyController {
    @Autowired
    private MoneyService moneyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @RequestMapping("/yearList")
    public HashMap<String, Object> yearList(@RequestBody String body) throws ParseException {
        HashMap<String, Object> ret = new HashMap<>();
        String userId = ((JSONObject) JSON.parse(body)).getString("userId");
        User user = userService.queryUser(userId);
        Calendar userCalendar = Calendar.getInstance();
        userCalendar.setTime(MyConstants.DATETIME_FORMAT.parse(user.getUserTime()));
        Integer yearBegin = userCalendar.get(Calendar.YEAR);

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());
        Integer yearEnd = nowCalendar.get(Calendar.YEAR);

        List<Integer> years = new ArrayList<>();
        for(int i = yearBegin; i<= yearEnd; i++){
            years.add(i);
        }
        ret.put("state", MyConstants.RESULT_OK);
        ret.put("years", years);
        return ret;
    }

    @RequestMapping("/pay")
    public void pay(@RequestBody String jsonstr) {
        //微信支付相关

        //成功之后在服务器数据库记录流水
        MoneyFlow moneyFlow = JSON.parseObject(jsonstr, new TypeReference<MoneyFlow>() {
        });
        int result = moneyService.addTestMoneyRecord(moneyFlow);
        if (result == 1) {
            //记录成功
        } else {
            //记录失败
        }
    }

    @RequestMapping("/payback")
    public void payback() {
        //进行分成算法
        //然后对每个监督者进行退款并且记录在数据库当中

    }

    @RequestMapping("/queryMoneyLeft")
    public HashMap<String, Object> queryMoneyLeft(@RequestBody String jsonstr) throws ParseException {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        HashMap<String, Object> ans = new HashMap<>();  // 返回值
        User user = userService.queryUser(userId);
        ans.put("state", MyConstants.RESULT_OK);
        ans.put("userMoney", user.getUserMoney());
        ans.put("testMoney", user.getTestMoney());
        return ans;
    }

    @RequestMapping("/queryMoneyRecord")
    public HashMap<String, Object> queryMoneyRecord(@RequestBody String jsonstr) throws ParseException {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String userId = (String) object.get("userId");
        Integer displayTypeIndex = object.getInteger("displayTypeIndex"); // 0: 列表，1：图表
        Integer moneyTypeIndex = (Integer) object.get("moneyTypeIndex"); // 1：充值，0：试玩，2：全部
        HashMap<String, Object> ans = new HashMap<>();  // 返回值

        if (displayTypeIndex == 0) {          // 列表,查询时间范围内
            String startTime = (String) object.get("startTime");
            String endTime = (String) object.get("endTime");
            getRecordList(userId, startTime, endTime, moneyTypeIndex, ans);
        } else if (displayTypeIndex == 1) {    // 图表，查询某一年
            Integer year = object.getInteger("year");
            ans.put("year", year);
            getRecordGraph(userId, year, moneyTypeIndex, ans);
        }
        ans.put("state", MyConstants.RESULT_OK);
        return ans;
    }

    // 查询余额记录(有时间范围)
    // 此函数目前没有对充值提现状态进行筛选，微信支付上线时要筛选pay_state为success
    private void getRecordList(String userId, String startTime, String endTime,
                               int recordType, HashMap<String, Object> ans) throws ParseException {
        List<MoneyFlow> MoneyRecords = moneyService.queryUserTestScopeMoneyFlow(startTime, endTime, userId); // 升序
        List<Pay> PayRecords = moneyService.queryUserTrueScopeMoneyPay(startTime, endTime, userId); // 升序
        int i = PayRecords.size() - 1;
        int j = MoneyRecords.size() - 1;
        List<MyFlow> displayMoneyList = new ArrayList<MyFlow>(); // 返回值
        while (i >= 0 && j >= 0) {
            MyFlow flow = new MyFlow();
            Date testDate = MyConstants.DATE_FORMAT.parse(MoneyRecords.get(j).getFlowTime());
            Date trueDate = MyConstants.DATE_FORMAT.parse(PayRecords.get(i).getPayTime().substring(0, 10));
            if (testDate.compareTo(trueDate) >= 0) { // test日期更近或同一天
                if (recordType == 2 || recordType != MoneyRecords.get(j).getIfTest()) {
                    flow.setFlowTime(MoneyRecords.get(j).getFlowTime());
                    flow.setFlowMoney(MoneyRecords.get(j).getFlowMoney());
                    flow.setTaskTitle(taskService.queryTask(MoneyRecords.get(j).getTaskId()).getTaskTitle());
                    if (MoneyRecords.get(j).getFlowIo().equals(MyConstants.MONEY_FLOW_OUT)) { //not num 0, but letter O
                        flow.setType("cost");
                    } else {
                        flow.setType("income");
                    }
                    displayMoneyList.add(flow);
                }
                j--;
            } else if (testDate.compareTo(trueDate) < 0) { // true日期更近
                if ((recordType == 2 || recordType == 1)) {
                    flow.setFlowMoney(PayRecords.get(i).getPayMoney());
                    flow.setFlowTime(PayRecords.get(i).getPayTime().substring(0, 10));
                    if (PayRecords.get(i).getPayType().equals("pay")) {
                        flow.setType("cost");
                        flow.setTaskTitle("微信充值");
                    } else {
                        flow.setType("income");
                        flow.setTaskTitle("微信取现");
                    }
                    displayMoneyList.add(flow);
                }
                i--;
            }
        }
        while (i >= 0) {
            if ((recordType == 2 || recordType == 1)) {
                MyFlow flow = new MyFlow();
                flow.setTaskTitle("微信充值");
                flow.setFlowMoney(PayRecords.get(i).getPayMoney());
                flow.setFlowTime(PayRecords.get(i).getPayTime().substring(0, 10));
                if (PayRecords.get(i).getPayType().equals(MyConstants.PAY_TYPE_PAY)) {
                    flow.setType("cost");
                } else {
                    flow.setType("income");
                }
                displayMoneyList.add(flow);
            }
            i--;
        }
        while (j >= 0) {
            if (recordType == 2 || recordType != MoneyRecords.get(j).getIfTest()) {
                MyFlow flow = new MyFlow();
                flow.setFlowTime(MoneyRecords.get(j).getFlowTime());
                flow.setFlowMoney(MoneyRecords.get(j).getFlowMoney());
                flow.setTaskTitle(taskService.queryTask(MoneyRecords.get(j).getTaskId()).getTaskTitle());
                if (MoneyRecords.get(j).getFlowIo().equals(MyConstants.MONEY_FLOW_OUT)) {
                    flow.setType("cost");
                } else {
                    flow.setType("income");
                }
                displayMoneyList.add(flow);
            }
            j--;
        }
        ans.put("displayMoneyList", displayMoneyList);
    }

    private void getRecordGraph(String userId, int year, int recordType, HashMap<String, Object> ans) {
        double totalRecharge = 0, totalWithdraw = 0, totalMoneyOut = 0, totalMoneyIn = 0;
        double[] displayMoneyOut = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        double[] displayMoneyIn = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        List<Pay> PayRecords = moneyService.queryUserTrueMoneyFlow(userId);
        List<MoneyFlow> MoneyRecords = moneyService.queryUserMoneyFlow(userId);
        for (MoneyFlow record : MoneyRecords) {
            String[] recordDates = record.getFlowTime().split("-");
            if (Integer.parseInt(recordDates[0]) == year) { // 在查询年份的范围内
                if (recordType == 2 || recordType != record.getIfTest()) {  // 查询全部/ 充值余额/试玩余额
                    if (record.getFlowIo().equals(MyConstants.MONEY_FLOW_OUT)) {
                        totalMoneyOut += record.getFlowMoney();
                        displayMoneyOut[Integer.parseInt(recordDates[1]) - 1] += record.getFlowMoney();
                    } else { // 入账
                        totalMoneyIn += record.getFlowMoney();
                        displayMoneyIn[Integer.parseInt(recordDates[1]) - 1] += record.getFlowMoney();
                    }
                }
            }
        }
        for (Pay payRecord : PayRecords) {
            String[] recordDates = payRecord.getPayTime().split("-");
            if (Integer.parseInt(recordDates[0]) == year) { // 在查询年份的范围内
                if (recordType == 2 || recordType == 1) {  // 查询全部/ 充值余额/试玩余额
                    if (payRecord.getPayType().equals(MyConstants.MONEY_FLOW_TYPE_PAY)) { // 充值
                        totalRecharge += payRecord.getPayMoney();
                    } else { // 取现
                        totalWithdraw += payRecord.getPayMoney();
                    }
                }
            }
        }
        ans.put("displayMoneyOut", displayMoneyOut);
        ans.put("displayMoneyIn", displayMoneyIn);
        ans.put("totalRecharge", totalRecharge);
        ans.put("totalWithdraw", totalWithdraw);
        ans.put("totalMoneyOut", totalMoneyOut);
        ans.put("totalMoneyIn", totalMoneyIn);
    }


    // submitWithdraw
    @RequestMapping("/submitWithdraw")
    public HashMap<String, Object> submitWithdraw(@RequestBody String jsonstr) {    // 创建订单
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String openId = (String) object.get("openId");  // 用户的openId
        String payTime = (String) object.get("payTime"); // 下单时间，前端为准
        String payId = UUID.randomUUID().toString();    // 充值表的主键
        Pay pay = new Pay();

        pay.setPayId(payId);
        pay.setPayUserid(openId);
        pay.setPayState(MyConstants.PAY_STATE_SUBMIT);  // 提交取现申请->(审核->)->转账->取现成功/失败
        pay.setPayType(MyConstants.PAY_TYPE_WITHDRAW);
        pay.setPayTime(payTime);
        pay.setPayOrderinfo("test-without-wechat-order-info"); // 这个应该微信返回的微信编号

        User user = userService.queryUser(openId);

        int total_fee = 0; // 注意单位：分
        try {
            double amount = (Double) object.get("amount");
            pay.setPayMoney(amount);
            user.setUserMoney(user.getUserMoney() - amount);
            total_fee = (int) (amount * 100);
        } catch (ClassCastException ex) {
            double amount = Double.parseDouble((String) object.get("amount"));
            pay.setPayMoney(amount);
            user.setUserMoney(user.getUserMoney() - amount);
            total_fee = (int) (amount * 100);
        }
        userService.updateUser(user);
        int recordResult = moneyService.addTrueMoneyRecord(pay);

        HashMap<String, Object> ans = new HashMap<>();  // 返回值
        ans.put("state", MyConstants.RESULT_OK);
        return ans;
    }

    @RequestMapping("/createOrder")
    public HashMap<String, Object> createOrder(@RequestBody String jsonstr) {    // 创建订单
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String openId = (String) object.get("openId");  // 用户的openId
        String payTime = (String) object.get("payTime"); // 下单时间，前端为准
        String payId = UUID.randomUUID().toString();    // 充值表的主键
        Pay pay = new Pay();

        pay.setPayId(payId);
        pay.setPayUserid(openId);
        pay.setPayState(MyConstants.PAY_STATE_SUBMIT);
        pay.setPayType(MyConstants.PAY_TYPE_PAY);
        pay.setPayTime(payTime);
        pay.setPayOrderinfo("test-without-wechat-order-info"); // 这个应该微信返回的微信编号

        User user = userService.queryUser(openId);

        int total_fee = 0; // 注意单位：分
        try {
            double amount = (Double) object.get("amount");
            pay.setPayMoney(amount);
            user.setUserMoney(user.getUserMoney() + amount);
            total_fee = (int) (amount * 100);
        } catch (ClassCastException ex) {
            double amount = Double.parseDouble((String) object.get("amount"));
            pay.setPayMoney(amount);
            user.setUserMoney(user.getUserMoney() + amount);
            total_fee = (int) (amount * 100);
        }
        userService.updateUser(user);
        int recordResult = moneyService.addTrueMoneyRecord(pay);

        HashMap<String, Object> ans = new HashMap<>();  // 返回值
//        String formData = moneyService.commitData(openId, payId, total_fee);
//        String httpResult = HttpUtils.httpXMLPost(WxPayConfig.createOrderUrl, formData);
//        try {
//            Map<String, String> resultMap = WXPayUtil.xmlToMap(httpResult);
//            ans.put("package", "prepay_id=" + resultMap.get("prepay_id"));
//            ans.put("prepay_id", resultMap.get("prepay_id")); // 这条数据/weixinPay要用
//            ans.put("nonceStr", resultMap.get("nonce_str")); // 这条数据/weixinPay要用
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String times = WXPayUtil.getCurrentTimestamp() + "";
//        Map<String, String> packageParams = new HashMap<String, String>();
//        packageParams.put("appId", WxPayConfig.appId);
//        packageParams.put("signType", WxPayConfig.signType);
//        packageParams.put("nonceStr", ans.get("nonceStr") + "");
//        packageParams.put("timeStamp", times);
//        packageParams.put("package", ans.get("package") + "");//商户订单号
//        String sign = "";
//        try {
//            sign = WXPayUtil.generateSignature(packageParams, WxPayConfig.sandBoxKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ans.put("payId", payId);
//
//        ans.put("timeStamp", times);
//        ans.put("paySign", sign);
        ans.put("state", MyConstants.RESULT_OK);
        return ans;
    }

    @RequestMapping("/weixinPay")
    public HashMap<String, Object> weixinPay(@RequestBody String jsonstr) {    // 支付
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
//        String openId = (String) object.get("openId");    // 检测用
//        String payId = (String) object.get("payId");      // 检测用
        String nonceStr = (String) object.get("nonceStr");
        String prepayId = (String) object.get("prepay_id");
        HashMap<String, Object> ans = new HashMap<>();  // 返回值
        Long timeStamp = System.currentTimeMillis() / 1000;
        ans.put("timeStamp", timeStamp);
        ans.put("nonceStr", nonceStr);
        ans.put("package", "prepay_id=" + prepayId);
        ans.put("signType", "MD5");
        //拼接签名需要的参数
        String stringSignTemp = "appId=" + WxPayConfig.appId +
                "&nonceStr=" + nonceStr +
                "&package=prepay_id=" + prepayId +
                "&signType=MD5&" +
                "timeStamp=" + timeStamp;
        //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
        stringSignTemp += "&key=" + WxPayConfig.sandBoxKey;
        String paySign = DigestUtils.md5Hex(WXPayUtil.getContentBytes(stringSignTemp, "utf-8")).toUpperCase();
        ans.put("paySign", paySign);
        ans.put("state", MyConstants.RESULT_OK);
        return ans;
    }

    // 回调通知
    @RequestMapping("/notify")
    @ResponseBody
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);
        Map map = WXPayUtil.xmlToMap(notityXml);
        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            if (WXPayUtil.verify(WXPayUtil.createLinkString(map), (String) map.get("sign"), WxPayConfig.sandBoxKey, "utf-8")) {
                /**此处添加自己的业务逻辑代码start**/
                /**亦即更新pay的微信订单编号和状态**/

                /**此处添加自己的业务逻辑代码end**/
                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    private String getSandboxSignKey() {
        // 根据mch_id和appId获得沙箱API密钥，没有mch_id
        return null;
    }
}

class MyFlow {
    private String type;
    private double flowMoney;
    private String flowTime;
    private String taskTitle;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getFlowMoney() {
        return flowMoney;
    }

    public void setFlowMoney(double flowMoney) {
        this.flowMoney = flowMoney;
    }

    public String getFlowTime() {
        return flowTime;
    }

    public void setFlowTime(String flowTime) {
        this.flowTime = flowTime;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
}

