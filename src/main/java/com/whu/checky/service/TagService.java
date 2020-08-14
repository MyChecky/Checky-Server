package com.whu.checky.service;

import com.whu.checky.domain.Tag;

import java.util.List;

public interface TagService {
    //返回所有标签
    List<Tag> queryAllTag();
    //根据id删除某个标签
    void deleteTagById(String id);
    //添加一个标签
    int addTag(Tag tag);
    //根据tagId返回tag
    Tag queryTagById(String tagId);
    //根据tagCount排序
    List<Tag> rank();
}
