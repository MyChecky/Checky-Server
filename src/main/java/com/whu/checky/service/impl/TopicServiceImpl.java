package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Topic;
import com.whu.checky.mapper.TopicMapper;
import com.whu.checky.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("TopicService")
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public List<Topic> queryAllTopics() {
        return topicMapper.getAllTopic();
    }

    @Override
    public void deleteTopicById(String id) {
        topicMapper.deleteById(id);
    }

    @Override
    public int addTopic(Topic topic) {
        return topicMapper.insert(topic);
    }

    @Override
    public List<Topic> orderByTopicCount() {
        return topicMapper.selectList(new EntityWrapper<Topic>()
//                .where("DATE_SUB(CURDATE(), INTERVAL 1 DAY) <= topic_time" )
                .and()
                .gt("topic_count",0)
                .orderBy("topic_count")
                .last("desc")
        );
    }

}
