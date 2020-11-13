package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.TaskType;
import com.whu.checky.domain.Topic;
import com.whu.checky.domain.TopicCount;

import java.util.List;

public interface TopicService {
    //返回所有话题
    List<Topic> queryAllTopics();
    //返回所有话题分页
    List<Topic> queryAllTopicInPage(Page<TaskType> p);
    //根据id删除某个话题
    void deleteTopicById(String id);
    //添加一个话题
    int addTopic(Topic topic);
    //根据话题topicCount排序
    List<Topic> orderByTopicCount();
    //重复检查
    Boolean isSameContent(String topicContent);
    //话题下 动态数自增
    void incTopicCount(String topicId);

    List<Topic> queryByKeyword(String keyword);

    Topic getTopicById(String topicId);

    String getTopicNameById(String topicId);

    List<TopicCount> getHotFiveTopics();
}
