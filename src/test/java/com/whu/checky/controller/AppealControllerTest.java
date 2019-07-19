package com.whu.checky.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Report;
import com.whu.checky.domain.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(locations= {"classpath*:application.yml"})
public class AppealControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception{
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void addAppeal() throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        Appeal appeal = new Appeal();
        appeal.setUserId("0");
        appeal.setTaskId("0");
        appeal.setCheckId("0");
        appeal.setAppealContent("我觉得有问题！");
        String json=mapper.writeValueAsString(appeal);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/appeal/add")
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
    public void display2User() throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        String userId = "0";
        String json=mapper.writeValueAsString(userId);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/appeal/display2User")
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
    public void del() throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        String appealId = "da83ae1f-81f2-4730-8";
        String json = mapper.writeValueAsString(appealId);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/appeal/del")
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
