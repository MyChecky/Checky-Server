package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Topic;
import com.whu.checky.domain.TopicEssay;
import com.whu.checky.mapper.TopicEssayMapper;
import com.whu.checky.mapper.TopicMapper;
import com.whu.checky.service.TopicEssayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("TopicEssayService")
public class TopicEssayServiceImpl implements TopicEssayService {

    @Autowired
    private TopicEssayMapper topicEssayMapper;

    @Override
    public List<TopicEssay> queryEssayIdByTopicId(String topicId) {
        return topicEssayMapper.selectList(new EntityWrapper<TopicEssay>()
        .eq("topic_id",topicId)
        );
    }
}
