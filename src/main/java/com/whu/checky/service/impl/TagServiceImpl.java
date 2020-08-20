package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Tag;
import com.whu.checky.mapper.TagMapper;
import com.whu.checky.mapper.TopicMapper;
import com.whu.checky.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("TagService")
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> queryAllTag() {
        return tagMapper.getAllTag();
    }

    @Override
    public void deleteTagById(String id) {
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
                .gt("tag_count",0)
                .orderBy("tag_count",false)
        );
    }
}
