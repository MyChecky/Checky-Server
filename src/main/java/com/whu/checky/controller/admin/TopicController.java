package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Topic;
import com.whu.checky.service.TopicService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/topic")
@Component("AdminTopicController") // 防止组件名相同冲突
public class TopicController {
    @Autowired
    TopicService topicService;

    //返回所有话题列表
    @RequestMapping("/queryAll")
    public HashMap<String, Object> getAllTopics()
    {
        HashMap<String, Object> ret = new HashMap<>();
        List<Topic> topicList = topicService.queryAllTopics();
        ret.put("TopicList", topicList);
        return ret;
    }

    //删除某个话题
    @RequestMapping("/delete")
    public JSONObject get(@RequestBody String jsonstr) {
        String id = JSON.parseObject(jsonstr).getString("id");
        topicService.deleteTopicById(id);
        JSONObject object = new JSONObject();
        object.put("state", MyConstants.RESULT_OK);
        return object;
    }

    //发布话题
    @RequestMapping("/add")
    public JSONObject add(@RequestBody String jsonstr)
    {
        Topic topic = JSON.parseObject(jsonstr, new TypeReference<Topic>() {
        });
        topic.setTopicId(UUID.randomUUID().toString());
        topic.setTopicCount(1);
        int result = topicService.addTopic(topic);
        String addResult = result == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
        JSONObject object = new JSONObject();
        object.put("state", addResult);
        return object;
    }
}
