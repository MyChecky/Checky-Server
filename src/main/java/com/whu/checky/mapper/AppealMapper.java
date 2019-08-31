package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Appeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("AppealMapper")
public interface AppealMapper extends BaseMapper<Appeal> {
int updateState(@Param("appealId") String appealId,@Param("result") String result);
    List<Appeal> queryAppealByUserName(@Param("username")String username);
}
