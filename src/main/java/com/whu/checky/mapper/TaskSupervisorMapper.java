package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.TaskSupervisor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "taskSupervisorMapper")
public interface TaskSupervisorMapper extends BaseMapper<TaskSupervisor> {
    List<String> getTaskSupervisors(String taskId);
    TaskSupervisor getTaskSupervisor(@Param("taskId") String taskId,
                                     @Param("supervisorId")String supervisorId);
}
