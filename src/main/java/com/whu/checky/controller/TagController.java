package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Tag;
import com.whu.checky.domain.Topic;
import com.whu.checky.service.TagService;
import com.whu.checky.service.TopicService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    TagService tagService;

    //返回所有标签
    @RequestMapping("/queryAll")
    public HashMap<String, Object> getAllTag()
    {
        HashMap<String, Object> ret = new HashMap<>();
        List<Tag> tagList = tagService.queryAllTag();
        ret.put("TagList", tagList);
        return ret;
    }

    //删除某个标签
    @RequestMapping("/delete")
    public JSONObject get(@RequestBody String jsonstr) {
        String id = JSON.parseObject(jsonstr).getString("id");
        tagService.deleteTagById(id);
        JSONObject object = new JSONObject();
        object.put("state", MyConstants.RESULT_OK);
        return object;
    }

    //发布标签
    @RequestMapping("/add")
    public JSONObject add(@RequestBody String jsonstr)
    {
        Tag tag = JSON.parseObject(jsonstr, new TypeReference<Tag>() {
        });
        tag.setTagId(UUID.randomUUID().toString());
        tag.setTagCount(1);
        int result = tagService.addTag(tag);
        String addResult = result == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
        JSONObject object = new JSONObject();
        object.put("state", addResult);
        return object;
    }

    //返回标签排行榜
    @RequestMapping("/rank")
    public JSONObject rank(@RequestBody String jsonstr)
    {
        List<Tag> tagList = tagService.rank();
        JSONObject object = new JSONObject();
        object.put("rankList", tagList);
        return object;
    }
}
