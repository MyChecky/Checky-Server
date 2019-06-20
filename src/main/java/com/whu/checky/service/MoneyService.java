package com.whu.checky.service;

public interface MoneyService {
    //用户支付给系统
    void UserpayToSystem();
    //打卡成功系统返还给用户
    void SystemPayback();
    //打卡失败系统分成给监督者
    void Systemdistribute();
}
