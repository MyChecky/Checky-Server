package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.*;
import com.whu.checky.mapper.*;
import com.whu.checky.service.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("taskTypeService")
public class TaskTypeServiceImpl implements TaskTypeService {
    @Autowired
    private TaskTypeMapper taskTypeMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private TypeTagMapper typeTagMapper;
    @Autowired
    private TaskTagMapper taskTagMapper;

    @Override
    public Integer addTaskType(TaskType taskType) {
        return taskTypeMapper.insert(taskType);
    }

    @Override
    public Integer updataTaskType(TaskType taskType) {
        return taskTypeMapper.updateById(taskType);
    }


    @Override
    public List<TaskType> ListAllTaskType(Page<TaskType> p) {
        List<TaskType> taskTypeList = taskTypeMapper.selectPage(p, new EntityWrapper<TaskType>());

        for (TaskType taskType : taskTypeList) {
            taskType.setSubTagsNum(typeTagMapper.selectList(new EntityWrapper<TypeTag>()
                    .eq("type_id", taskType.getTypeId()))
                    .size());
        }

        return taskTypeList;
    }

    @Override
    public List<TaskType> AllTaskType() {
        return taskTypeMapper.selectList(new EntityWrapper<TaskType>());
    }

    @Override
    public TaskType queryTaskType(String typeId) {
        return taskTypeMapper.selectById(typeId);
    }

    @Override
    public Integer DeleteTaskType(String typeId) {
        //首先要删除该任务类型下的所有任务和标签
//        taskMapper.delete(new EntityWrapper<Task>()
//        .eq("type_id",typeId)
//        );
//        tagMapper.delete(new EntityWrapper<Tag>()
//        .eq("type_id",typeId));

        // 首先获得所有tag
        List<TypeTag> typeTags = typeTagMapper.selectList(new EntityWrapper<TypeTag>()
                .eq("type_id", typeId));
        // 若有task 为相应tag，则删除
        // 若tag不对应其他type,则删除type_tag与tag;若tag对应其他type,则删除type_tag
        for (TypeTag typeTag : typeTags) {
            taskTagMapper.delete(new EntityWrapper<TaskTag>()
                    .eq("tag_id", typeTag.getTagId()));

            List<TypeTag> typeTagsForDelete = typeTagMapper.selectList(new EntityWrapper<TypeTag>()
                    .eq("tag_id", typeTag.getTagId()));
            // 因为一个tag 可能对应多个type；因此，若type_tag 中对应tag 只有一个，则删除
            typeTagMapper.delete(new EntityWrapper<TypeTag>()
                    .eq("type_id", typeTag.getTypeId())
                    .eq("tag_id", typeTag.getTagId()));
            if (typeTagsForDelete.size() == 1) {
                tagMapper.deleteById(typeTag.getTagId());
            }
        }
        return taskTypeMapper.deleteById(typeId);
    }

    @Override
    public void incTotalNum(String typeId) {
        taskTypeMapper.incTotalNum(typeId);
    }

    @Override
    public void incPassNum(String typeId) {
        taskTypeMapper.incPassNum(typeId);
    }

    @Override
    public String getTypeContentByTypeId(String typeId) {
        return taskTypeMapper.selectById(typeId).getTypeContent();
    }

    @Override
    public Boolean alreadyExist(String typeContent) {
        List<TaskType> taskTypeList = taskTypeMapper.selectList(new EntityWrapper<TaskType>()
                .eq("type_content", typeContent)
        );
        return taskTypeList.size() != 0;
    }

    @Override
    public List<TaskType> getAllTypeWithoutPage() {
        return taskTypeMapper.selectList(new EntityWrapper<TaskType>());
    }
}
