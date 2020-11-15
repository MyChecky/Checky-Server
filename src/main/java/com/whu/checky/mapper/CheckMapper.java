package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Check;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "CheckMapper")
public interface CheckMapper extends BaseMapper<Check> {
    void insertNewCheck(Check check);
    void updatePassSuperviseCheck(@Param("checkId") String checkId);
    void updateDenySuperviseCheck(@Param("checkId") String checkId);

    @Select("SELECT COUNT(*) \n" +
            "FROM `CHECK` \n" +
            "WHERE (\n" +
            "`USER_ID` = \"#{userId}\" \n" +
            "AND `CHECK_STATE` = \"#{checkState}\"\n" +
            ")")
    int countStateNumberForSomeone(@Param("userId") String userId, @Param("checkState") String checkState);
}
