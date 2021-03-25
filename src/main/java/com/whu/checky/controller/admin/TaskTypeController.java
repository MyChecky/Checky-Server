package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskType;
import com.whu.checky.service.TaskTypeService;
import com.whu.checky.util.MyConstants;
import com.whu.checky.util.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/taskType")
@Component("AdminTaskTypeController")
public class TaskTypeController {
    @Autowired
    private TaskTypeService taskTypeService;

    //用于新添任务类型的
    @PostMapping("/addType")
    public String addType(@RequestBody String jsonstr) throws Exception {
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String typeContent = (String) object.get("typeContent");
        if(taskTypeService.alreadyExist(typeContent))
        {
            return "该任务类型已经存在，无需新增";
        }
        else {
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
    @RequestMapping("/allType")
    public JSONObject allType(@RequestBody String jsonstr) {
        JSONObject json = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        Integer page = object.getInteger("page");
        Integer pageSize = object.getInteger("pageSize");
        Page<TaskType> p = new Page<>(page, pageSize);
        JSONObject res=new JSONObject();
        List<TaskType> taskTypes = taskTypeService.ListAllTaskType(p);
        res.put("page",page);
        res.put("taskTypes",taskTypes);
        return res;
    }
    @RequestMapping("/getAllTypeWithoutPage")
    public JSONObject getAllTypeWithoutPage(@RequestBody String jsonstr) {
        JSONObject res=new JSONObject();
        List<TaskType> taskTypes = taskTypeService.getAllTypeWithoutPage();
        res.put("taskTypes",taskTypes);
        return res;
    }
    @PostMapping("/update")
    public JSONObject update(@RequestBody String body) {
        JSONObject res=new JSONObject();
        TaskType taskType= JSON.parseObject(body,new TypeReference<TaskType>(){});
        if(taskTypeService.updataTaskType(taskType)==1) {
            res.put("state", MyConstants.RESULT_OK);
        }else {
            res.put("state",MyConstants.RESULT_FAIL);
        }
        return res;
    }

    @PostMapping("/delete")
    public JSONObject delete(@RequestBody String body) {
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(body);
        String typeId=(String)object.get("typeId");
        if(taskTypeService.DeleteTaskType(typeId)==1) {
            res.put("state",MyConstants.RESULT_OK);
        }else {
            res.put("state",MyConstants.RESULT_FAIL);
        }
        return res;
    }


}

