package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Record;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MoneyFlowMapper extends BaseMapper<MoneyFlow> {
}
