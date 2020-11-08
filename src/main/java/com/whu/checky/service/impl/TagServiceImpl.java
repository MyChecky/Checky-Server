package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Tag;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskType;
import com.whu.checky.domain.TypeTag;
import com.whu.checky.mapper.TagMapper;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.TopicMapper;
import com.whu.checky.mapper.TypeTagMapper;
import com.whu.checky.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        //还要删除该标签下所有的任务
        taskMapper.delete(new EntityWrapper<Task>()
                .eq("tag1", id)
                .or()
                .eq("tag2", id)
                .or()
                .eq("tag3", id)
                .or()
                .eq("tag4", id)
                .or()
                .eq("tag5", id)
        );
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
        return tagMapper.selectList(new EntityWrapper<Tag>()
                .gt("tag_count", 0)
                .orderBy("tag_count", false)
        );
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
        for(TypeTag typeTag:typeTags){
            tagsRes.add(tagMapper.selectById(typeTag.getTagId()));
        }
        return tagsRes;
    }
}
