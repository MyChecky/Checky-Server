package com.whu.checky.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class MyConstants {

    //时间格式
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_TIME = "HH:mm:ss";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(MyConstants.FORMAT_DATE);
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(MyConstants.FORMAT_TIME);
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat(MyConstants.FORMAT_DATETIME);

    public static final long SECONDS_A_DAY = 86400000L;

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00%");

    // 主要用于管理端搜索，但不提供时间范围时
    public static final String START_TIME = "1970-01-01";
    public static final String END_TIME = "2099-01-01";

    // 举报申诉状态
    public static final String PROCESS_STATE_PASS = "pass";
    public static final String PROCESS_STATE_DENY = "deny";
    public static final String PROCESS_STATE_TO_PROCESS = "toProcess";
    //
    public static final int MENU_FLAG_ENABLE = 1;
    public static final int MENU_FLAG_DISABLE = 0;

    // 基本返回
    public static final String RESULT_OK = "ok";
    public static final String RESULT_FAIL = "fail";
    // 带有意义的特殊返回
    public static final String RESULT_NO_ENOUGH_MONEY = "noEnoughMoney";    // 资金不足
    public static final String RESULT_NO_ENOUGH_SUP = "noEnoughSupervisor"; // 监督者不够
    public static final String RESULT_ZERO_CHECK_TIMES = "zeroCheckTimes";  // 监督次数为0
    public static final String RESULT_INSERT_FAIL = "insertFail";
    public static final String RESULT_UPDATE_FAIL = "updateFail";
    public static final String RESULT_DELETE_FAIL = "deleteFail";

    //任务状态
    public static final String TASK_STATE_SAVE = "save";          //草稿/未发布状态
    public static final String TASK_STATE_NOMATCH = "nomatch";    //没有找到监督者
    public static final String TASK_STATE_DURING = "during";      //找到监督者，且正在执行中
    public static final String TASK_STATE_SUCCESS = "success";    //任务执行成功
    public static final String TASK_STATE_FAIL = "fail";           //任务执行失败
    public static final String TASK_STATE_COMPLETE = "complete"; //分账完成
    public static final String TASK_STATE_APPEAL = "appeal";    //申诉中

    //任务操作状态
    //任务添加成功/失败
    public static final String TASK_ADD_SUCCESS = "ok";
    public static final String TASK_ADD_FAIL = "fail";
    //任务添加失败原因

    public static final String TASK_ADD_FAIL_X = ""; //
    public static final String TASK_ADD_FAIL_Y = ""; //
    public static final String TASK_ADD_FAIL_Z = "";

    //任务匹配成功/失败
    public static final String TASK_MATCH_SUCCESS = "ok";
    public static final String TASK_MATCH_FAIL = "fail";
    //任务匹配失败原因


    //打卡认证方式（按比例或按数量来判断某一次打卡是否认证通过
    public static final String CHECK_TYPE_PROPORTION = "proportion";
    public static final String CHECK_TYPE_NUMBER = "number";

    //打卡认证状态
    public static final String CHECK_STATE_UNKNOWN = "unknown";
    public static final String CHECK_STATE_PASS = "pass";
    public static final String CHECK_STATE_DENY = "deny";

    //监督认证状态
    public static final String SUPERVISE_STATE_PASS = "pass";
    public static final String SUPERVISE_STATE_DENY = "deny";
    public static final String SUPERVISE_STATE_UNKNOWN = "unknown"; //?存在此状态吗？

    //打卡记录媒体类型
    public static final String RECORD_TYPE_TEXT = "text";
    public static final String RECORD_TYPE_IMAGE = "image";
    public static final String RECORD_TYPE_AUDIO = "audio";
    public static final String RECORD_TYPE_VIDEO = "video";

    // 默认下拉加载数量
    public static final int PAGE_LENGTH = 10;
    public static final int PAGE_LENGTH_MINI = 5;

    // 好友状态
    public static final int FRIEND_APPLY = 0;
    public static final int FRIEND_PASS = 1;
    public static final int FRIEND_DENY = 2;

    //建议状态
    public static final String SUGGESTION_STATE_WAITING = "waiting";
    public static final String SUGGESTION_STATE_PROCESSED = "processed";
    public static final String SUGGESTION_STATE_PASS = "pass";
    public static final String SUGGESTION_STATE_DENY = "deny";

    //举报类型
    public static final String REPORT_TYPE_ESSAY = "0";
    public static final String REPORT_TYPE_TASK = "1";   //监督者举报任务发布者/执行人的任务
    public static final String REPORT_TYPE_CHECK = "3";  //监督者举报任务发布者/执行人的打卡
    public static final String REPORT_TYPE_SUPERVISOR = "2"; //任务发布者举报 监督者


    //资金流向：进/出
    public static final String MONEY_FLOW_IO_ALL = "all"; //查询是，用到下列所有类型
    public static final String MONEY_FLOW_IN = "I";
    public static final String MONEY_FLOW_OUT = "O";

    //资金流动类型
    public static final String MONEY_FLOW_TYPE_ALL = "all";   //查询时，用到下列所有类型（除init?)
    public static final String MONEY_FLOW_TYPE_INIT = "init";         //玩家初始货币
    public static final String MONEY_FLOW_TYPE_PAY = "pay";           //支付押金
    public static final String MONEY_FLOW_TYPE_REFUND = "refund";    //返还押金
    public static final String MONEY_FLOW_TYPE_BENEFIT = "benefit";  //收益/分成

    //交易类型充值，提现
    public static final String PAY_TYPE_ALL = "all"; //查询时，用到下列所有类型
    public static final String PAY_TYPE_PAY = "pay"; //充值
    public static final String PAY_TYPE_WITHDRAW = "withdraw"; //提现

    //交易状态
    public static final String PAY_STATE_ALL = "all";//查询时，用到下列所有类型
    public static final String PAY_STATE_SUBMIT = "submit";
    public static final String PAY_STATE_SUCCESS = "success";
    public static final String PAY_STATE_FAIL = "fail";
    //...
    public static final int MONEY_TEST_ALL = 2;
    public static final int MONEY_TEST_TRUE = 1;
    public static final int MONEY_TEST_FALSE = 0;

    public static final int IF_TEST_TRUE = 1;
    public static final int IF_TEST_FALSE = 0;

    public static final int IF_DELETE_TRUE = 1;
    public static final int IF_DELETE_FALSE = 0;

    //勋章类型
    /*等级勋章分以下五类
     * 专注勋章一类
     * 达人勋章类型根据任务类型给定
     * */
    public static final String RANK1 = "青铜";
    public static final String RANK2 = "白银";
    public static final String RANK3 = "黄金";
    public static final String RANK4 = "黑金";
    public static final String RANK5 = "砖石";
    public static final String CONCENTRATE = "CONCENTRATE";

    // medal images
    public static final String MEDAL_SPECIAL_TYPE_URL = "type.png";
    public static final String MEDAL_CONCENTRATE_TYPE_URL = "concentrate.png";
    public static final String MEDAL_LEVEL_TYPE_1_URL = "l1.png";
    public static final String MEDAL_LEVEL_TYPE_2_URL = "l2.png";
    public static final String MEDAL_LEVEL_TYPE_3_URL = "l3.png";
    public static final String MEDAL_LEVEL_TYPE_4_URL = "l4.png";
    public static final String MEDAL_LEVEL_TYPE_5_URL = "l5.png";

    // Hot number
    public static final int HOT_NUMBER = 5;

    // Medal
    public static final String MEDAL_SPECIAL_TYPE_NAME = "达人勋章";
    public static final String MEDAL_CONCENTRATE_TYPE_NAME = "专注勋章";
    public static final String MEDAL_LEVEL_TYPE_NAME = "等级勋章";

    // Level Medal Judge Method -> replaced by >> in MedalUpdate.judgeUserLevel()
//    public static final int LEVEL_MEDAL_1 = 4;
//    public static final int LEVEL_MEDAL_2 = 16;
//    public static final int LEVEL_MEDAL_3 = 64;
//    public static final int LEVEL_MEDAL_4 = 256;
//    public static final int LEVEL_MEDAL_5 = 1024;

    public static final String LEVEL_MEDAL_Name_1 = "白银等级";
    public static final String LEVEL_MEDAL_Name_2 = "黄金等级";
    public static final String LEVEL_MEDAL_Name_3 = "铂金等级";
    public static final String LEVEL_MEDAL_Name_4 = "黑金等级";
    public static final String LEVEL_MEDAL_Name_5 = "砖石等级";

    public static final int CONCENTRATE_MEDAL_TIMES = 16;
    public static final int AREA_SPECIAL_MEDAL_TIMES = 128;

    public static final double CONCENTRATE_MEDAL_LOSE_RATE = 0.70;
    public static final double AREA_SPECIAL_MEDAL_LOSE_RATE = 0.70;

}
