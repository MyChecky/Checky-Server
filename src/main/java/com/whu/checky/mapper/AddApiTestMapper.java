package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.AddApiTest;
import com.whu.checky.domain.AddApiTestAux;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "AddApiTestMapper")
public interface AddApiTestMapper extends BaseMapper<AddApiTest> {
    @Select("SELECT `add_api_id` AS `id`, `add_api_name` AS `name`, `add_api_time` AS `time` " +
            "FROM `add_api_test` WHERE `add_api_id` = #{addApiId};")
    AddApiTestAux selectSafelyById(@Param("addApiId") String addApiId);
}
