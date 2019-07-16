package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "taskMapper")
public interface TaskMapper extends BaseMapper<Task> {
    List<Task> queryUserTasks(@Param("userId") String userId, @Param("date") String date);
}
