package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Pay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "PayMapper")
public interface PayMapper extends BaseMapper<Pay> {
    //还没写都要查什么
    String getPayById(@Param("payId") String payId);
}
