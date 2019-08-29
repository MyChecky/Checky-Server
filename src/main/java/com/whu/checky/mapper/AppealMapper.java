package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Appeal;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("AppealMapper")
public interface AppealMapper extends BaseMapper<Appeal> {
int updateState(String appealId,String result);
}
