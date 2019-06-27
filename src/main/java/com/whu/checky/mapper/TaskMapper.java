package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "taskMapper")
public interface TaskMapper extends BaseMapper<Task> {
}