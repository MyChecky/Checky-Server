//package com.whu.checky.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.junit.Assert.*;
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SuperviseControllerTest {
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mvc;
//
//    @Before
//    public void setUp() throws Exception {
//        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
//    }
//
//    @Test
//    public void addCheck() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        JSONObject object=new JSONObject();
//        object.put("userId",);
//        object.put("date1",);
//        object.put("date2",);
//
//        String json = mapper.writeValueAsString();
//
//        System.out.println("before--------------------post");
//        System.out.println(json);
//
//        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/check/addCheck")
//                .contentType("application/json;charset=UTF-8")
//                .content(json)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//        System.out.println("after---------------------post");
//        String result = mvcResult.getResponse().getContentAsString();
//        System.out.println("==========结果为：==========\n" + result + "\n");
//    }
//}