package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Tag;
import com.whu.checky.service.TagService;
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
@RequestMapping("/admin/tag")
@Component("AdminTagController") // 防止组件名相同冲突
public class TagController {
    @Autowired
    TagService tagService;

    //返回所有标签
    @RequestMapping("/queryAll")
    public HashMap<String, Object> getAllTag()
    {
        HashMap<String, Object> ret = new HashMap<>();
        List<Tag> tagList = tagService.queryAllTag();
        ret.put("TopicList", tagList);
        return ret;
    }

    //删除某个话题
    @RequestMapping("/delete")
    public JSONObject get(@RequestBody String jsonstr) {
        String id = JSON.parseObject(jsonstr).getString("id");
        tagService.deleteTagById(id);
        JSONObject object = new JSONObject();
        object.put("state", MyConstants.RESULT_OK);
        return object;
    }

    //发布话题
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
}
