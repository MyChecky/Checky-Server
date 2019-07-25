package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.MoneyFlow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("moneyFlowMapper")
public interface MoneyFlowMapper extends BaseMapper<MoneyFlow> {
    List<MoneyFlow> queryAllScopeMoneyFlow(String startDate, String endDate);
    List<MoneyFlow> queryUserScopeMoneyFlow(String startDate, String endDate, String userId);
}
