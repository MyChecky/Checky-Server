package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.*;
import com.whu.checky.mapper.*;
import com.whu.checky.service.TagService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service("TagService")
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TypeTagMapper typeTagMapper;
    @Autowired
    private TaskTagMapper taskTagMapper;
    @Autowired
    private TaskTypeMapper taskTypeMapper;

    @Override
    public List<Tag> queryAllTag(Page<Tag> p) {
        return tagMapper.selectPage(p, new EntityWrapper<Tag>());
    }

    @Override
    public List<Tag> queryAll() {
        return tagMapper.selectList(new EntityWrapper<Tag>());
    }

    @Override
    public void deleteTagById(String id) {
        // 删除与task与tag的关联
        taskTagMapper.delete(new EntityWrapper<TaskTag>()
                .eq("tag_id", id));

        Tag tag = tagMapper.selectById(id);
        List<TypeTag> typeTags = typeTagMapper.selectList(new EntityWrapper<TypeTag>()
                .eq("tag_id", id));
        // 更新taskType表中的总数、通过数
        for (TypeTag typeTag : typeTags) {
            TaskType taskType = taskTypeMapper.selectById(typeTag.getTypeId());
            taskType.setTotalNum(taskType.getTotalNum() - tag.getTagCount());
            taskType.setPassNum(taskType.getPassNum() - tag.getPassCount());
            taskTypeMapper.updateById(taskType);
        }

        // 删除type 与 tag 的关联
        typeTagMapper.delete(new EntityWrapper<TypeTag>()
                .eq("tag_id", id));

        tagMapper.deleteById(id);
    }

    @Override
    public int addTag(Tag tag) {
        return tagMapper.insert(tag);
    }

    @Override
    public Tag queryTagById(String tagId) {
        return tagMapper.selectById(tagId);
    }

    @Override
    public List<Tag> rank() {
        Page<Tag> tagPage = new Page<>(0, MyConstants.HOT_NUMBER);
        List<Tag> tagList = tagMapper.selectPage(tagPage, new EntityWrapper<Tag>()
//                .gt("tag_count", 0)
                        .orderBy("tag_count", false)
        );

        for (Tag tag : tagList) {
            List<TypeTag> typeTagList = typeTagMapper.selectList(new EntityWrapper<TypeTag>()
                    .eq("tag_id", tag.getTagId()));
            for(TypeTag typeTag: typeTagList){
                TaskType taskType = taskTypeMapper.selectById(typeTag.getTypeId());
                if(tag.getTagBelongedTypes() == null){
                    tag.setTagBelongedTypes(taskType.getTypeContent()+" ");
                }else{
                    tag.setTagBelongedTypes(tag.getTagBelongedTypes()+taskType.getTypeContent()+" ");
                }
            }
        }
        return tagList;
    }

    @Override
    public void incTagCount(String tagId) {
        tagMapper.incTagCount(tagId);
    }

    @Override
    public void incPassNum(String tagId) {
        tagMapper.incPassNum(tagId);
    }

    @Override
    public List<Tag> getTagsByTypeId(String typeId) {
//        return tagMapper.getTagsByTypeId(typeId);
        List<TypeTag> typeTags = typeTagMapper.selectList(new EntityWrapper<TypeTag>().eq("type_id", typeId));
        List<Tag> tagsRes = new ArrayList<Tag>();
        for (TypeTag typeTag : typeTags) {
            tagsRes.add(tagMapper.selectById(typeTag.getTagId()));
        }
        return tagsRes;
    }

    @Override
    public int addTaskTag(String taskId, String tagId) {
        TaskTag taskTag = new TaskTag();
        taskTag.setTagId(tagId);
        taskTag.setTaskId(taskId);
        return taskTagMapper.insert(taskTag);
    }

    @Override
    public List<TaskTag> getTaskTagsByTaskId(String taskId) {
        return taskTagMapper.selectList(new EntityWrapper<TaskTag>()
                .eq("task_id", taskId));
    }

    @Override
    public String getTagNameById(String tagId) {
        return tagMapper.selectById(tagId).getTagContent();
    }

    @Override
    public void deleteTaskTagsByTaskId(String taskId) {
        taskTagMapper.delete(new EntityWrapper<TaskTag>()
                .eq("task_id", taskId));
    }

    @Override
    public List<Tag> queryByKeyword(String keyword) {
        return tagMapper.selectList(new EntityWrapper<Tag>()
                .like("tag_content", keyword)
                .orderBy("tag_count", false));
    }

    @Override
    public List<Tag> getTagByTagName(String tagName) {
        return tagMapper.selectList(new EntityWrapper<Tag>()
                .eq("tag_content", tagName));
    }
}
