package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.Topic;
import com.whu.checky.service.TopicService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    TopicService topicService;

    //返回所有话题列表
    @RequestMapping("/queryAll")
    public HashMap<String, Object> getAllTopics(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>();
        List<Topic> topicList = topicService.queryAllTopics();
        ret.put("TopicList", topicList);
        return ret;
    }

    // 不给普通用户这些权限
//    //删除某个话题
//    @RequestMapping("/delete")
//    public JSONObject get(@RequestBody String jsonstr) {
//        String id = JSON.parseObject(jsonstr).getString("id");
//        topicService.deleteTopicById(id);
//        JSONObject object = new JSONObject();
//        object.put("state", MyConstants.RESULT_OK);
//        return object;
//    }
//
//    //发布话题
//    @RequestMapping("/add")
//    public JSONObject add(@RequestBody String jsonstr)
//    {
//        JSONObject res = new JSONObject();
//        JSONObject jsonObject = (JSONObject) JSON.parse(jsonstr);
//        String topicContent = jsonObject.getString("topicContent");
//        //重复性检查
//        if(topicService.isSameContent(topicContent)){
//            res.put("state","该话题已存在");
//            res.put("topic",topicContent);
//            return res;
//        }
//        else {
//            Topic topic = new Topic();
//            topic.setUserId(jsonObject.getString("userId"));
//            topic.setLaunchTime(MyConstants.DATETIME_FORMAT.format(new Date()));
//            topic.setTopicContent(topicContent);
//            topic.setTopicId(UUID.randomUUID().toString());
//            topic.setTopicCount(0);
//            int result = topicService.addTopic(topic);
//            String addResult = result == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
//            res.put("state", addResult);
//            return res;
//        }
//    }

    //话题排序（用于统计）
    @RequestMapping("/sort")
    public JSONObject sort(@RequestBody String jsonstr) {
        List<Topic> topicList = topicService.orderByTopicCount();
        JSONObject object = new JSONObject();
        object.put("sortedTopicList", topicList);
        return object;
    }

    /**
     * 热度前五话题
     *
     * @return
     */
    @RequestMapping("/getHotFiveTopics")
    public HashMap<String, Object> getHotFiveTopics(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>();
        // TODO
        return ret;
    }

    /**
     * 按关键词查询
     *
     * @return
     */
    @RequestMapping("/queryByKeyword")
    public HashMap<String, Object> queryByKeyword(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String keyword = (String) object.get("keyword");

        List<Topic> topicList = topicService.queryByKeyword(keyword);
        ret.put("TopicList", topicList);
        return ret;
    }
}
