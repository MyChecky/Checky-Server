package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.TaskType;
import com.whu.checky.domain.Topic;
import com.whu.checky.mapper.TopicMapper;
import com.whu.checky.service.TopicService;
import net.sf.jsqlparser.statement.select.Top;
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
        return topicMapper.selectList(new EntityWrapper<Topic>());
    }

    @Override
    public List<Topic> queryAllTopicInPage(Page<TaskType> p) {
        return topicMapper.selectPage(p,new EntityWrapper<Topic>());
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
//                .and()
                .gt("topic_count",0)
                .orderBy("topic_count")
                .last("desc")
        );
    }

    @Override
    public Boolean isSameContent(String topicContent) {
        List<Topic> topics = topicMapper.selectList(new EntityWrapper<Topic>()
        .eq("topic_content",topicContent)
        );
        return topics.size()==0?false:true;
    }

    @Override
    public void incTopicCount(String topicId) {
        topicMapper.incTopicCount(topicId);
    }

}
