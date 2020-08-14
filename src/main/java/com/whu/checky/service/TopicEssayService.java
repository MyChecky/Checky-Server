package com.whu.checky.service;

import com.whu.checky.domain.TopicEssay;

import java.util.List;

public interface TopicEssayService {
    //根据topicId筛选对应的所有关系
    List<TopicEssay> queryEssayIdByTopicId(String topicId);
}
