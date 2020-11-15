package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Tag;
import com.whu.checky.domain.Topic;
import com.whu.checky.domain.TypeTag;
import com.whu.checky.service.TagService;
import com.whu.checky.service.TopicService;
import com.whu.checky.service.TypeTagService;
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
    @Autowired
    TypeTagService typeTagService;

    //返回所有标签
    @RequestMapping("/queryAll")
    public HashMap<String, Object> getAllTag() {
        HashMap<String, Object> ret = new HashMap<>();
        List<Tag> tagList = tagService.queryAll();
        ret.put("TagList", tagList);
        return ret;
    }

    //删除某个标签(同时删除对应的关系)
    @RequestMapping("/delete")
    public JSONObject get(@RequestBody String jsonstr) {
        String id = JSON.parseObject(jsonstr).getString("id");
        tagService.deleteTagById(id);
        List<TypeTag> typeTagList = typeTagService.getTypeTagsBytagId(id);
        typeTagService.delTypeTag(typeTagList.get(0));
        JSONObject object = new JSONObject();
        object.put("state", MyConstants.RESULT_OK);
        return object;
    }

    //发布标签
    @RequestMapping("/add")
    public JSONObject add(@RequestBody String jsonstr) {
        //获取传入的tag和type信息
        String tagContent = JSON.parseObject(jsonstr).getString("tagContent");
        String typeId = JSON.parseObject(jsonstr).getString("typeId");
        //生成tag对象
        Tag tag = new Tag();
        tag.setTagContent(tagContent);
        String tagId = UUID.randomUUID().toString();
        tag.setTagId(tagId);
        tag.setTagCount(1);
        int result = tagService.addTag(tag);
        String addResult = result == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
        String addTypeTag = MyConstants.RESULT_FAIL;
        //若传入了typeId，则新增关系
        if (typeId != null) {
            TypeTag typeTag = new TypeTag();
            typeTag.setTagId(tagId);
            typeTag.setTypeId(typeId);
            int result1 = typeTagService.addTypeTag(typeTag);
            addTypeTag = result1 == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
        }
        JSONObject object = new JSONObject();
        object.put("addTagState", addResult);
        object.put("addTypeTagState", addTypeTag);
        return object;
    }

    //返回标签排行榜
    @RequestMapping("/rank")
    public JSONObject rank() {
        List<Tag> tagList = tagService.rank();
        JSONObject object = new JSONObject();
        object.put("rankList", tagList);
        return object;
    }

    @RequestMapping("/incCount")
    public JSONObject incCount(@RequestBody String jsonstr) {
        String tagId = JSON.parseObject(jsonstr).getString("tagId");
        tagService.incPassNum(tagId);
        tagService.incTagCount(tagId);
        JSONObject object = new JSONObject();
        object.put("request", MyConstants.RESULT_OK);
        return object;
    }

    @RequestMapping("/queryByKeyword")
    public HashMap<String, Object> queryByKeyword(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String keyword = (String) object.get("keyword");
        List<Tag> tagList = tagService.queryByKeyword(keyword);
        ret.put("tagList", tagList);
        return ret;
    }
}
