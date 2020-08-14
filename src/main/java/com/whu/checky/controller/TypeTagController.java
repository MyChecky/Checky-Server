package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Tag;
import com.whu.checky.domain.TypeTag;
import com.whu.checky.service.TagService;
import com.whu.checky.service.TypeTagService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/typeTag")
public class TypeTagController {
    @Autowired
    private TypeTagService typeTagService;

    @Autowired
    private TagService tagService;
    //添加一个关系
    @RequestMapping("/add")
    public HashMap<String, Object> add(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        TypeTag typeTag = JSON.parseObject(jsonstr, new TypeReference<TypeTag>() {
        });
        int result = typeTagService.addTypeTag(typeTag);
        String addResult = result == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
        ret.put("state", addResult);
        return ret;
    }

//    删除一个关系
    @RequestMapping("/del")
    public HashMap<String, Object> del(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        TypeTag typeTag = JSON.parseObject(jsonstr, new TypeReference<TypeTag>() {
        });
        int result = typeTagService.delTypeTag(typeTag);
        String addResult = result == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
        ret.put("state", addResult);
        return ret;
    }

//    更新一个关系
    @RequestMapping("/update")
    public HashMap<String, Object> update(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        TypeTag typeTag = JSON.parseObject(jsonstr, new TypeReference<TypeTag>() {
        });
        typeTagService.updateTypeTag(typeTag.getTypeId(),typeTag.getTagId());
        ret.put("state", MyConstants.RESULT_OK);
        return ret;
    }

//    查询所有关系
    @RequestMapping("/all")
    public HashMap<String, Object> queryAll(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        List<TypeTag> typeTagList = typeTagService.getAllTypeTags();
//        String addResult = result == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
        ret.put("state", typeTagList);
        return ret;
    }

    // 根据tpyeId获取对应的所有tag
    @RequestMapping("/getByTypeId")
    public HashMap<String, Object> getByTypeId(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        String typeid = JSON.parseObject(jsonstr).getString("typeId");
        List<TypeTag> typeTagList = typeTagService.getTypeTagsBytypeId(typeid);
        List<Tag> tags = new ArrayList<Tag>();
        for(int i = 0;i<typeTagList.size();i++)
        {
            tags.add(tagService.queryTagById(typeTagList.get(i).getTagId()));
        }
        ret.put("state",MyConstants.RESULT_OK);
        ret.put("tagList", tags);
        return ret;
    }

    //    根据tagId获取关系
    @RequestMapping("/getByTagId")
    public HashMap<String, Object> getByTagId(@RequestBody String jsonstr) {
        HashMap<String, Object> ret = new HashMap<>(); // 返回值
        String tagid = JSON.parseObject(jsonstr).getString("tagId");
        List<TypeTag> typeTagList = typeTagService.getTypeTagsBytagId(tagid);
        ret.put("state",MyConstants.RESULT_OK);
        ret.put("typeTagList", typeTagList);
        return ret;
    }

}
