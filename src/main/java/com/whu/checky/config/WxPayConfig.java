package com.whu.checky.config;

/**
 * Created by qiang on 2018/1/1.
 */
public class WxPayConfig {
    //小程序appid
    public static final String appId = "wx5f1aa0197013dad6";
    //微信支付的商户id
    public static final String mchId = "";
    //微信支付的商户密钥
    public static final String key = "";
    //沙箱测试的商户密钥
    public static String sandBoxKey = "";
    //小程序密钥
    public static final String secret = "";
    //支付成功后的服务器回调url
    public static final String notifyUrl = "https:127.0.0.1:8080/money/notify";
    //交易类型，小程序支付的固定值为JSAPI
    public static final String tradeType = "JSAPI";
    //签名方式，固定值
    public static final String signType = "MD5";
    //统一订单接口url地址
    public static final String createOrderUrl = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
    //沙箱测试key
    public static final String getSandBoxSignKeyUrl = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";
}
