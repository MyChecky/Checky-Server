package com.whu.checky.controller;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppealTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception{
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void addAppeal() throws Exception{
        JSONObject json = new JSONObject();
        json.put("userId","oM2yQ4jR0La_jZ8hyxkERsqNTh_8");
        json.put("taskId","e0ff35e0-5ae0-4b54-a375-66e8aee6657d");
        json.put("checkId","4c32a083489749df9676f576afce09dc");
        json.put("content","打卡被多人恶意审核");


        System.out.println("before--------------------post");
        System.out.println(json.toString());
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/appeal/add")
                .contentType("application/json;charset=UTF-8")
                .content(json.toJSONString())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println("after---------------------post");
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("==========结果为：==========\n" + result + "\n");
    }

    @Test
    public void delAppeal() throws Exception {
        JSONObject json = new JSONObject();
        json.put("userId","oM2yQ4jR0La_jZ8hyxkERsqNTh_8");



        System.out.println("before--------------------post");
        System.out.println(json.toString());
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/appeal/display2User")
                .contentType("application/json;charset=UTF-8")
                .content(json.toJSONString())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println("after---------------------post");
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("==========结果为：==========\n" + result + "\n");
    }

    @Test
    public void queryAppeal2User() throws Exception {
        JSONObject json = new JSONObject();
        json.put("appealId","798d32c5-2725-46ca-b");



        System.out.println("before--------------------post");
        System.out.println(json.toString());
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/appeal/del")
                .contentType("application/json;charset=UTF-8")
                .content(json.toJSONString())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println("after---------------------post");
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("==========结果为：==========\n" + result + "\n");
    }
}
