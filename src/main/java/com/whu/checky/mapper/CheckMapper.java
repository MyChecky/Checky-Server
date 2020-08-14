package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Check;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "CheckMapper")
public interface CheckMapper extends BaseMapper<Check> {
    void insertNewCheck(Check check);
    void updatePassSuperviseCheck(@Param("checkId") String checkId);
    void updateDenySuperviseCheck(@Param("checkId") String checkId);
}
