package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.TaskSupervisor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "TaskSupervisorMapper")
public interface TaskSupervisorMapper extends BaseMapper<TaskSupervisor> {
}
