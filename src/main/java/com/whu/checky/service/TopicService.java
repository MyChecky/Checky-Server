package com.whu.checky.service;

import com.whu.checky.domain.Topic;

import java.util.List;

public interface TopicService {
    //返回所有话题
    List<Topic> queryAllTopics();
    //根据id删除某个话题
    void deleteTopicById(String id);
    //添加一个话题
    int addTopic(Topic topic);
    //根据话题topicCount排序
    List<Topic> orderByTopicCount();
}
