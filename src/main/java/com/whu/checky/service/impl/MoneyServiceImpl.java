package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.mapper.MoneyFlowMapper;
import com.whu.checky.service.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("/moneyService")
public class MoneyServiceImpl implements MoneyService {
    @Autowired
    private MoneyFlowMapper moneyFlowMapper;

    @Override
    public int addMoneyRecord(MoneyFlow moneyFlow) {
        return moneyFlowMapper.insert(moneyFlow);
    }

    @Override
    public MoneyFlow queryMoneyFlow(String flowId) {
        return moneyFlowMapper.selectById(flowId);
    }

    @Override
    public List<MoneyFlow> queryUserMoneyFlow(String userId) {
        return moneyFlowMapper.selectList(new EntityWrapper<MoneyFlow>()
                .eq("to_user_id",userId)
                .or()
                .eq("from_user_id",userId)
                .orderBy("flow_time",true))
                ;
    }

    @Override
    public List<MoneyFlow> queryUserMoneyFlow(String userId, int page) {
        return moneyFlowMapper.selectPage(new Page<MoneyFlow>(page, 10), new EntityWrapper<MoneyFlow>()
                .eq("to_user_id", userId)
                .or()
                .eq("from_user_id", userId)
                .orderBy("flow_time", true))
                ;
    }

    @Override
    public List<MoneyFlow> queryAllMoneyFlow() {
        return moneyFlowMapper.selectList(new EntityWrapper<MoneyFlow>()
                .orderBy("flow_time",true))
                ;
    }

    @Override
    public List<MoneyFlow> queryAllMoneyFlow(int page) {
        return moneyFlowMapper.selectPage(new Page<MoneyFlow>(page, 10), new EntityWrapper<MoneyFlow>()
                .orderBy("flow_time", true))
                ;
    }

    @Override
    public List<MoneyFlow> queryAllScopeMoneyFlow(String startDate, String endDate) {
        return moneyFlowMapper.queryAllScopeMoneyFlow(startDate,endDate);
    }

    @Override
    public List<MoneyFlow> queryUserScopeMoneyFlow(String startDate, String endDate, String userId) {
        return  moneyFlowMapper.queryUserScopeMoneyFlow(startDate,endDate,userId);
    }

}
