package com.whu.checky.controller;

import com.whu.checky.domain.Essay;
import com.whu.checky.domain.Topic;
import com.whu.checky.domain.TopicEssay;
import com.whu.checky.service.EssayService;
import com.whu.checky.service.TopicEssayService;
import com.whu.checky.service.TopicService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/topicRank")
public class TopicRankController {
    @Autowired
    TopicEssayService topicEssayService;
    @Autowired
    TopicService topicService;
    @Autowired
    EssayService essayService;

    //根据评论数排序
    @RequestMapping("/rank")
    public List<TopicAndNum> topicRank() {
        //返回值
        List<TopicAndNum> RankList = new ArrayList<>();
        //获取根据话题中动态数为依据的排列结果
        List<Topic> topicList = topicService.orderByTopicCount();
        //遍历话题list
        //把话题存入
        //同时把每个话题对应的所有动态检索出来 计算点赞和评论总和
        for(int i=0; i<topicList.size(); i++) {
            Topic topic = topicList.get(i);
            //存放当前topic对应的所有话题动态关系
            List<TopicEssay> topicEssays = topicEssayService.queryEssayIdByTopicId(topic.getTopicId());
            int commentSum = 0;
            int likeSum = 0;
            int recentTopicCount=0;
            for(int j=0; j<topicEssays.size(); j++) {
                TopicEssay topicEssay = topicEssays.get(j);
                Essay essay = essayService.queryEssayById(topicEssay.getEssayId());
                //判断 如果时间在一天之内 则可以被计算进去
                if(isYesterday(essay.getEssayTime())) {
                    commentSum += essay.getCommentNum();
                    likeSum += essay.getLikeNum();
                    recentTopicCount++;
                }
            }
            topic.setTopicCount(recentTopicCount);

            RankList.add(new TopicAndNum(topic,commentSum,likeSum));
        }

        Collections.sort(RankList);
        return RankList;
    }

    public static boolean isYesterday(String essayTime)
    {
        Date d = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);

        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");

        String yesterday = sp.format(d);// 获取昨天日期

        try {

            Date yesterdaydate = MyConstants.DATE_FORMAT.parse(yesterday);

            Date essaytimedate = MyConstants.DATE_FORMAT.parse(essayTime);
            return essaytimedate.after(yesterdaydate);
        }catch (Exception e)
        {
            return false;
        }
    }

}

class TopicAndNum implements Comparable<TopicAndNum>
{
    Topic topic;
    int likeSum;
    int commentSum;

    //构造函数
    public TopicAndNum(Topic topic,int likeSum,int commentSum)
    {
        this.topic=topic;
        this.likeSum=likeSum;
        this.commentSum=commentSum;
    }
    //综合排序
    //1.话题数目
    //2.likeSum+commentSum
    @Override
    public int compareTo(TopicAndNum o) {
        int i = this.getTopic().getTopicCount() - o.getTopic().getTopicCount();
        if (i == 0) {
            return -(this.getLikeSum()+this.getCommentSum() - o.getLikeSum()+o.getCommentSum());
        }
        return -i;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public int getLikeSum() {
        return likeSum;
    }

    public void setLikeSum(int likeSum) {
        this.likeSum = likeSum;
    }

    public int getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(int commentSum) {
        this.commentSum = commentSum;
    }


}

