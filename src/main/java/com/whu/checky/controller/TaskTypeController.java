package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.Suggestion;
import com.whu.checky.domain.TaskType;
import com.whu.checky.service.TagService;
import com.whu.checky.service.TaskTypeService;
import com.whu.checky.util.MyConstants;
import com.whu.checky.util.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/taskType")
public class TaskTypeController {

    @Autowired
    private TaskTypeService taskTypeService;

    @Autowired
    private TagService tagService;

    //用于新添任务类型的
    @PostMapping("/addType")
    public String addType(@RequestBody String jsonstr) throws Exception {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String typeContent = (String) object.get("typeContent");
        if (taskTypeService.alreadyExist(typeContent)) {
            return "该任务类型已经存在，无需新增";
        } else {
            TaskType taskType = new TaskType();
            taskType.setTypeContent(typeContent);
            taskType.setPassNum((long) 0);
            taskType.setTotalNum((long) 0);
            taskType.setTypeId(MyStringUtil.getUUID());
            int result = taskTypeService.addTaskType(taskType);
            if (result == 0) {
                //添加失败
                return MyConstants.RESULT_INSERT_FAIL;
            } else {
                //添加成功
                return MyConstants.RESULT_OK;
            }
        }
    }

    @PostMapping("/delType")
    public String delType(@RequestBody String jsonstr) {
        String taskTypeid = (String) JSON.parse(jsonstr);
        int result = taskTypeService.DeleteTaskType(taskTypeid);
        if (result == 0) {
            //删除失败
            return MyConstants.RESULT_DELETE_FAIL;
        } else {
            //删除成功
            return MyConstants.RESULT_OK;
        }
    }


    @RequestMapping("/allType")
    public List<TaskType> allType() {
        List<TaskType> taskTypes = taskTypeService.AllTaskType();
        for(TaskType taskType: taskTypes){
            taskType.setTags(tagService.getTagsByTypeId(taskType.getTypeId()));
        }
        return taskTypes;
    }


    @RequestMapping("/udpateType")
    public String udpateType(@RequestBody String jsonstr) {
        TaskType taskType = JSON.parseObject(jsonstr, new TypeReference<TaskType>() {
        });
        int res = taskTypeService.updataTaskType(taskType);
        if (res == 0) {
            return MyConstants.RESULT_UPDATE_FAIL;
        } else {
            return MyConstants.RESULT_OK;
        }
    }
}
