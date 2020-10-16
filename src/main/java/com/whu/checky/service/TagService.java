package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Tag;
import com.whu.checky.domain.TaskType;

import java.util.List;

public interface TagService {
    //返回所有标签(管理端使用)
    List<Tag> queryAllTag(Page<Tag> p);
    //返回所有标签(小程序使用)
    List<Tag> queryAll();
    //根据id删除某个标签
    void deleteTagById(String id);
    //添加一个标签
    int addTag(Tag tag);
    //根据tagId返回tag
    Tag queryTagById(String tagId);
    //根据tagCount排序
    List<Tag> rank();
    //自增TagCount
    void incTagCount(String tagId);
    //自增passCount
    void incPassNum(String tagId);
}
