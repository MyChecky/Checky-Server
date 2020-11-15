package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.TaskType;
import com.whu.checky.domain.Topic;
import com.whu.checky.domain.TopicCount;
import com.whu.checky.mapper.TopicCountMapper;
import com.whu.checky.mapper.TopicMapper;
import com.whu.checky.service.TopicService;
import com.whu.checky.util.MyConstants;
import net.sf.jsqlparser.statement.select.Top;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service("TopicService")
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private TopicCountMapper topicCountMapper;

    @Override
    public List<Topic> queryAllTopics() {
        return topicMapper.selectList(new EntityWrapper<Topic>()
                .orderBy("topic_count", false));
    }

    @Override
    public List<Topic> queryAllTopicInPage(Page<TaskType> p) {
        return topicMapper.selectPage(p, new EntityWrapper<Topic>());
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
                        .gt("topic_count", 0)
                        .orderBy("topic_count")
                        .last("desc")
        );
    }

    @Override
    public Boolean isSameContent(String topicContent) {
        List<Topic> topics = topicMapper.selectList(new EntityWrapper<Topic>()
                .eq("topic_content", topicContent)
        );
        return topics.size() != 0;
    }

    @Override
    public void incTopicCount(String topicId) {
        String countDate = MyConstants.DATE_FORMAT.format(new Date());

        List<TopicCount> topicCounts = topicCountMapper.selectList(new EntityWrapper<TopicCount>()
                .eq("topic_id", topicId)
                .eq("count_date", countDate));

        if (topicCounts.isEmpty()) {
            TopicCount topicCount = new TopicCount();
            topicCount.setTopicId(topicId);
            topicCount.setCountDate(countDate);
            topicCount.setCountNumber(1);
            topicCountMapper.insert(topicCount);
        } else {
            TopicCount topicCount = topicCounts.get(0);
            topicCount.setCountNumber(topicCount.getCountNumber() + 1);
            topicCountMapper.update(topicCount, new EntityWrapper<TopicCount>()
                    .eq("topic_id", topicId)
                    .eq("count_date", countDate));
        }
        topicMapper.incTopicCount(topicId);
    }

    @Override
    public List<Topic> queryByKeyword(String keyword) {
        return topicMapper.selectList(new EntityWrapper<Topic>()
                .like("topic_content", keyword)
                .orderBy("topic_count", false));
    }

    @Override
    public Topic getTopicById(String topicId) {
        return topicMapper.selectById(topicId);
    }

    @Override
    public String getTopicNameById(String topicId) {
        return topicMapper.selectById(topicId).getTopicContent();
    }

    @Override
    public List<TopicCount> getHotFiveTopics() {
        Date dayNow = new Date();
        String dateNow = MyConstants.DATE_FORMAT.format(dayNow);

        Page<TopicCount> topicCountPage = new Page<>(0, MyConstants.HOT_NUMBER);
        List<TopicCount> topicCounts = topicCountMapper.selectPage(topicCountPage, new EntityWrapper<TopicCount>()
                .eq("count_date", dateNow)
                .orderBy("count_number", false));

        // 今日个数小于五个,尽量在过去一周找当日热度，找不到就散了
        for (int i = 0; i < 7 && topicCounts.size() < MyConstants.HOT_NUMBER; ++i) {
            dayNow = new Date(dayNow.getTime() - MyConstants.SECONDS_A_DAY);
            dateNow = MyConstants.DATE_FORMAT.format(dayNow);
            List<TopicCount> topicCountsTmp = topicCountMapper.selectPage(topicCountPage, new EntityWrapper<TopicCount>()
                    .eq("count_date", dateNow)
                    .orderBy("count_number", false));
            for (TopicCount topicCountOld : topicCountsTmp) {
                boolean isRepeat = false;
                for (TopicCount topicCountRes : topicCounts) {
                    if (topicCountRes.getTopicId().equals(topicCountOld.getTopicId())) {
                        isRepeat = true;
                        break;
                    }
                }
                if (!isRepeat) {
                    topicCounts.add(topicCountOld);
                    if (topicCounts.size() == MyConstants.HOT_NUMBER) break;
                }
            }
        }

        for (TopicCount topicCount : topicCounts)
            topicCount.setTopic(topicMapper.selectById(topicCount.getTopicId()));
        return topicCounts;
    }
}
