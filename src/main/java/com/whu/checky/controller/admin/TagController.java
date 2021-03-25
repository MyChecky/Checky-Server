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
import com.whu.checky.service.TaskTypeService;
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
    @Autowired
    TaskTypeService taskTypeService;

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
        JSONObject res = new JSONObject();
        // 传入 tagContent 与 typeId
        // 全靠后台判断
        String tagName = JSON.parseObject(jsonstr).getString("tagContent");
        String typeId = JSON.parseObject(jsonstr).getString("typeId");

        // 检查是不是有这个标签了
        List<Tag> tagList = tagService.getTagByTagName(tagName);
        Tag tag;
        if(tagList.isEmpty()){
            // 创建新标签
            tag = new Tag();
            tag.setTagContent(tagName);
            tag.setTagId(UUID.randomUUID().toString());
            tag.setTagCount(0);
            tag.setPassCount(0);
            tagService.addTag(tag);
            // 添加新标签对任务类型的关联
            TypeTag typeTagToAdd = new TypeTag();
            typeTagToAdd.setTagId(tag.getTagId());
            typeTagToAdd.setTypeId(typeId);
            typeTagService.addTypeTag(typeTagToAdd);
            // return
            res.put("addState", "新建标签，并新建关联");
        }else{
            tag=tagList.get(0);

            // 检查是不是已经有对应的标签和任务类型了
            List<TypeTag> typeTagList = typeTagService.getTypeTagByTypeTag(tag.getTagId(), typeId);
            if(typeTagList.isEmpty()){
                // 新建关联前，增加任务类型的相应数据
                TaskType taskType = taskTypeService.queryTaskType(typeId);
                taskType.setTotalNum(taskType.getTotalNum() + tag.getTagCount());
                taskType.setPassNum(taskType.getPassNum() + tag.getPassCount());
                taskTypeService.updataTaskType(taskType);

                // 新建关联
                TypeTag typeTagToAdd = new TypeTag();
                typeTagToAdd.setTagId(tag.getTagId());
                typeTagToAdd.setTypeId(typeId);
                typeTagService.addTypeTag(typeTagToAdd);


                res.put("addState", "已有对应标签，并更新任务类型数据后新建关联");
            }else{
                res.put("addState", "已有对应标签与任务类型，且已经关联");
            }
        }


        return res;
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
