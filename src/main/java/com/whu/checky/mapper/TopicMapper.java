package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "TopicMapper")
public interface TopicMapper extends BaseMapper<Topic> {

    @Select("SELECT topic_id AS topicId,topic_content AS topicContent,topic_count AS topicCount FROM topic")
    List<Topic> getAllTopic();
    void incTopicCount(String topicId);
}
