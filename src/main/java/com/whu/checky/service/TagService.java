package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Tag;
import com.whu.checky.domain.TaskTag;
import com.whu.checky.domain.TypeTag;

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

    // get tags by typeId
    List<Tag> getTagsByTypeId(String typeId);
    // add record for task_tag
    int addTaskTag(String taskId, String tagId);

    List<TaskTag> getTaskTagsByTaskId(String taskId);

    String getTagNameById(String tagId);

    void deleteTaskTagsByTaskId(String taskId);

    List<Tag> queryByKeyword(String keyword);

    List<Tag> getTagByTagName(String tagName);

    List<Tag> getTagsByPage(Page<Tag> p);

    List<Tag> getOneTypeTagsByPage(Page<TypeTag> p, String typeId);
}
