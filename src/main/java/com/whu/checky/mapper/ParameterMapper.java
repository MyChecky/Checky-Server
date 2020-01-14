package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Parameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "ParameterMapper")
public interface ParameterMapper extends BaseMapper<Parameter> {
    //还没写都要查什么，应该有这个
    String getValueByParam(@Param("paramName")String paramName);
}
