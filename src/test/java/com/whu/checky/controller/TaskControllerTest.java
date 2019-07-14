package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whu.checky.domain.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(locations= {"classpath*:application.yml"})
public class TaskControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception{
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void addTask() throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        Task task=new Task();
        task.setTaskId(UUID.randomUUID().toString());
        task.setUserId("oM2yQ4jR0La_jZ8hyxkERsqNTh_8");
        task.setTypeId("a8179f78-69ac-4723-bf23-7b4c695bdf7f");
        task.setTaskTitle("背单词100天");
        task.setTaskContent("每天背200单词");
        task.setTaskStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        task.setTaskEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        task.setTaskState("uncheck");
        task.setTaskMoney(200.0);
        task.setSupervisorNum(0);
        task.setRefundMoney(110.0);
        task.setCheckTimes(10);
        task.setCheckFrec("1111111");
        String json=mapper.writeValueAsString(task);
        System.out.println("before--------------------post");
        System.out.println(json.toString());
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/task/addTask")
                .contentType("application/json;charset=UTF-8")
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println("after---------------------post");
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("==========结果为：==========\n" + result + "\n");
    }

    @Test
    public void queryUserTasks () throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        String userId ="oM2yQ4jR0La_jZ8hyxkERsqNTh_8";
        String json=mapper.writeValueAsString(userId);
        System.out.println("before--------------------post");
        System.out.println(json.toString());
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/task/queryUserTasks")
                .contentType("application/json;charset=UTF-8")
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println("after---------------------post");
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("==========结果为：==========\n" + result + "\n");

    }

    @Test
    public void listTasks() throws Exception {
        System.out.println("before--------------------post");
        ResultActions action = mvc.perform(MockMvcRequestBuilders.post("/task/listTasks").contentType("application/json;charset=UTF-8").accept(MediaType.APPLICATION_JSON));
        MvcResult mvcResult = action.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        System.out.println("after---------------------post");
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("==========结果为：==========\n" + result + "\n");
    }

    @Test
    public void queryTask() throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        String taskid ="fce6af72-dfda-45cb-9c1c-58105599efe3";
        String json=mapper.writeValueAsString(taskid);
        System.out.println("before--------------------post");
        System.out.println(json.toString());
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/task/queryTask")
                .contentType("application/json;charset=UTF-8")
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println("after---------------------post");
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("==========结果为：==========\n" + result + "\n");
    }

    @Test
    public void finishTask() throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        JSONObject request=new JSONObject();
        request.put("taskid","00");
        request.put("state","fail");
        String json=mapper.writeValueAsString(request);
        System.out.println("before--------------------post");
        System.out.println(json.toString());
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/task/finishTask")
                .contentType("application/json;charset=UTF-8")
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println("after---------------------post");
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("==========结果为：==========\n" + result + "\n");
    }

    @Test
    public void getDistribute() throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        JSONObject request=new JSONObject();
        request.put("taskId","4464440e-d6a9-4432-9fa7-1110e61116fa");
        String json = mapper.writeValueAsString(request);
        System.out.println("before--------------------post");
        System.out.println(json);
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/task/getDistribute")
                .contentType("application/json;charset=UTF-8")
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println("after---------------------post");
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("==========结果为：==========\n" + result + "\n");
    }
}