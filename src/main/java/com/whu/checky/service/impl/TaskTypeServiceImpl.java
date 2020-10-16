package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Tag;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskType;
import com.whu.checky.mapper.TagMapper;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.TaskTypeMapper;
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
        return taskTypeMapper.selectPage(p,new EntityWrapper<TaskType>());
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
        taskMapper.delete(new EntityWrapper<Task>()
        .eq("type_id",typeId)
        );
        tagMapper.delete(new EntityWrapper<Tag>()
        .eq("type_id",typeId));
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
        .eq("type_content",typeContent)
        );
        return taskTypeList.size()!=0;
    }
}
