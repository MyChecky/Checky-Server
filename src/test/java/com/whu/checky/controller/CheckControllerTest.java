package com.whu.checky.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Check;
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
//@ContextConfiguration(locations= {"classpath*:application.yml"})
public class CheckControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void addCheck() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Check check = new Check();
        check.setUserId("oM2yQ4jR0La_jZ8hyxkERsqNTh_8");
        check.setTaskId("e0ff35e0-5ae0-4b54-a375-66e8aee6657d");
        check.setCheckTime("2019 07 01 12 29 00");
        String json = mapper.writeValueAsString(check);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/check/addCheck")
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
    public void queryCheck() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String checkId = "a1075a47fa104acfaeaf03df853ac75c";
        String json = mapper.writeValueAsString(checkId);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/Checky/check/queryCheck")
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
    public void listCheck() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String userId = "oM2yQ4jR0La_jZ8hyxkERsqNTh_8";
        String json = mapper.writeValueAsString(userId);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/Checky/check/listCheck")
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
    public void updateCheck() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Check check = new Check();
        check.setCheckId("a1075a47fa104acfaeaf03df853ac75c");
        check.setUserId("oM2yQ4jR0La_jZ8hyxkERsqNTh_8");
        check.setTaskId("e0ff35e0-5ae0-4b54-a375-66e8aee6657d");
        check.setCheckTime("2019 07 01 12 29 00");
        check.setCheckState("success");
        check.setPassNum(1);
        check.setSuperviseNum(1);
        String json = mapper.writeValueAsString(check);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/Checky/check/updateCheck")
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
    public void deleteCheck() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String check_id = "a1075a47fa104acfaeaf03df853ac75c";
        String json = mapper.writeValueAsString(check_id);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/Checky/check/deleteCheck")
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
