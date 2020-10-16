package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Tag;
import com.whu.checky.domain.TaskType;
import com.whu.checky.domain.Topic;
import com.whu.checky.domain.TypeTag;
import com.whu.checky.service.TagService;
import com.whu.checky.service.TaskService;
import com.whu.checky.service.TypeTagService;
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
    @Autowired
    TypeTagService typeTagService;
    //返回所有标签
    @RequestMapping("/queryAll")
    public HashMap<String, Object> getAllTag(@RequestBody String jsonstr)
    {
        JSONObject json = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        Integer page = object.getInteger("page");
        Integer pageSize = object.getInteger("pageSize");
        Page<Tag> p = new Page<>(page, pageSize);
        HashMap<String, Object> ret = new HashMap<>();
        List<Tag> tagList = tagService.queryAllTag(p);
        ret.put("page",page);
        ret.put("data", tagList);
        return ret;
    }

    //删除某个标签
    @RequestMapping("/delete")
    public JSONObject get(@RequestBody String jsonstr) {
        String id = JSON.parseObject(jsonstr).getString("tagId");
        tagService.deleteTagById(id);
        JSONObject object = new JSONObject();
        object.put("state", MyConstants.RESULT_OK);
        return object;
    }

    //发布标签
    @RequestMapping("/add")
    public JSONObject add(@RequestBody String jsonstr)
    {
        //获取传入的tag信息
        String tagContent = JSON.parseObject(jsonstr).getString("tagContent");
        String typeId = JSON.parseObject(jsonstr).getString("typeId");
        //生成对象
        Tag tag = new Tag();
        tag.setTagContent(tagContent);
        String tagId = UUID.randomUUID().toString();
        tag.setTagId(tagId);
        tag.setTagCount(0);
        tag.setTypeId(typeId);
        int result = tagService.addTag(tag);
        String addResult = result == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
//        String addTypeTag = MyConstants.RESULT_FAIL;
//        if(typeId!=null)
//        {
//            TypeTag typeTag = new TypeTag();
//            typeTag.setTagId(tagId);
//            typeTag.setTypeId(typeId);
//            int result1 = typeTagService.addTypeTag(typeTag);
//            addTypeTag = result1 == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;
//        }
        JSONObject object = new JSONObject();
        object.put("addTagState", addResult);
//        object.put("addTypeTagState", addTypeTag);
        return object;
    }

    //标签排序（用于统计）
    @RequestMapping("/sort")
    public JSONObject sort()
    {
        List<Tag> tagList = tagService.rank();
        JSONObject object = new JSONObject();
        object.put("sortedTagList", tagList);
        return object;
    }
}
