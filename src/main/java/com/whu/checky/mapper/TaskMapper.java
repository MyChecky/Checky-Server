package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.whu.checky.domain.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "taskMapper")
public interface TaskMapper extends BaseMapper<Task> {
    //这里还不知道改的对不对（试过能查）
    String taskWithNameSql = "SELECT task_id AS taskId,user_id AS userId,type_id AS typeId,task_title AS taskTitle,task_content\n" +
            " AS taskContent,task_start_time AS taskStartTime,task_end_time AS taskEndTime,task_state AS\n" +
            " taskState,task_money AS taskMoney,supervisor_num AS supervisorNum,refund_money AS refundMoney,check_times\n" +
            "AS checkTimes,check_frec AS checkFrec, user_name as userName, if_test as ifTest, system_benifit as systemBenifit,\n" +
            "check_pass as checkPass, min_pass as minPass, real_pass as realPass, min_check as minCheck, min_check_type as \n" +
            "minCheckType, supervisor_type as supervisorType, if_area as  ifArea, if_hobby as ifHobby, add_time as addTime\n" +
            "FROM task NATURAL JOIN user WHERE 1=1";

    List<Task> queryUserTasks(@Param("userId") String userId, @Param("date") String date);
    String getTitleById(@Param("taskId")String taskId);

    @Select(taskWithNameSql + " ${ew.sqlSegment}")
    List<Task> getTasksWithName(@Param("ew") Wrapper wrapper);

    @Select(taskWithNameSql + " ${ew.sqlSegment}")
    List<Task> getTasksWithNameAndPage(@Param("ew") Wrapper wrapper, Pagination page);

    List<Task> queryTaskByUserName(@Param("username") String username);
}
