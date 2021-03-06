package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Tag;
import com.whu.checky.domain.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "TagMapper")
public interface TagMapper extends BaseMapper<Tag> {

    @Select("SELECT tag_id AS tagId,tag_content AS tagContent,tag_count AS tagCount FROM tag")
    List<Tag> getAllTag();

    void incTagCount(String tagId);
    void incPassNum(String tagId);

    @Select("SELECT tag_id AS tagId,tag_content AS tagContent,tag_count AS tagCount\n" +
            "FROM tag\n" +
            "WHERE tag_id IN \n" +
            "(\n" +
            "SELECT tag_id \n" +
            "FROM type_tag\n" +
            "WHERE type_id = \"#{typeId}\")")
    List<Tag> getTagsByTypeId(@Param("typeId")String  typeId);
}
