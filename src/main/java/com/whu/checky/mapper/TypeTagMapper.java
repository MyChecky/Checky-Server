package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.AddApiTestAux;
import com.whu.checky.domain.TypeTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "TypeTagMapper")
public interface TypeTagMapper extends BaseMapper<TypeTag> {
    @Select("SELECT `type_id` AS `typeId`, `tag_id` AS `tagId` FROM `type_tag`")
    List<TypeTag> queryAll();

    Integer updateTypeTag(@Param("typeId") String typeId, @Param("tagId") String tagId);
}
