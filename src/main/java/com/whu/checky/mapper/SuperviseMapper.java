package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Supervise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "superviseMapper")
public interface SuperviseMapper extends BaseMapper<Supervise> {
    List<String> allNeedToCheck(@Param("userId") String userId,@Param("startDate") String startDate,@Param("endDate") String endDate);
    void updateState(String superviseId,String newState);
//    String getContent(String taskId);
    String getStateByIds(String supervisorId,String checkId);
    String haveSupervised(@Param("checkId")String checkId,@Param("userId")String userId);
}
