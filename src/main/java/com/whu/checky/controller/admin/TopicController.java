package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.TaskType;
import com.whu.checky.domain.Topic;
import com.whu.checky.service.EssayService;
import com.whu.checky.service.TopicService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/topic")
@Component("AdminTopicController") // 防止组件名相同冲突
public class TopicController {
    @Autowired
    TopicService topicService;
    @Autowired
    EssayService essayService;

    //返回所有话题列表
    @RequestMapping("/queryAll")
    public HashMap<String, Object> getAllTopics(@RequestBody String jsonstr)
    {
        JSONObject json = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        Integer page = object.getInteger("page");
        Integer pageSize = object.getInteger("pageSize");
        Page<TaskType> p = new Page<>(page, pageSize);

        HashMap<String, Object> ret = new HashMap<>();
        List<Topic> topicList = topicService.queryAllTopicInPage(p);
        ret.put("TopicList", topicList);
        return ret;
    }

    //删除某个话题
    @RequestMapping("/delete")
    public JSONObject get(@RequestBody String jsonstr) {
        String topicId = JSON.parseObject(jsonstr).getString("topicId");

        // 这里不真的删除用户的动态
        essayService.deleteEssayByTopicId(topicId);
        topicService.deleteTopicById(topicId);
        JSONObject object = new JSONObject();
        object.put("state", MyConstants.RESULT_OK);
        return object;
    }

    //发布话题
    @RequestMapping("/add")
    public JSONObject add(@RequestBody String jsonstr)
    {
        JSONObject json = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        JSONObject ret = new JSONObject();
        String topicContent = object.getString("topicContent");
        if(topicService.isSameContent(topicContent)){
            ret.put("state","已存在的话题");
            ret.put("topic",topicContent);
        }
        else {
            Topic topic = new Topic();
            topic.setTopicId(UUID.randomUUID().toString());
            topic.setTopicContent(topicContent);
            topic.setLaunchTime(MyConstants.DATETIME_FORMAT.format(new Date()));
            topic.setTopicCount(0);
            topic.setUserId("admin");
            int result = topicService.addTopic(topic);
            String addResult = result == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
            ret.put("state", addResult);
        }
        return ret;
    }

    //话题排序（用于统计）
    @RequestMapping("/sort")
    public JSONObject sort(@RequestBody String body)
    {
        int page = JSON.parseObject(body).getInteger("page");
        Integer pageSize = JSON.parseObject(body).getInteger("pageSize");
        if (pageSize == null) {
            pageSize = 10;
        }
        Page<Topic> p = new Page<>(page, pageSize);
        List<Topic> topicList = topicService.getTopicsByPage(p);
        JSONObject object = new JSONObject();
        object.put("sortedTopicList", topicList);
        object.put("total", p.getTotal());
        return object;
    }
}
