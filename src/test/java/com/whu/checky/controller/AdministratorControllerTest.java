package com.whu.checky.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Task;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(locations= {"classpath*:application.yml"})
public class AdministratorControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception{
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void register() throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        Administrator administrator = new Administrator();
        administrator.setUserName("woshishei");
        administrator.setUserPassword("1234");
        String json=mapper.writeValueAsString(administrator);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/admin/register")
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
    public void login() throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        Administrator administrator = new Administrator();
        administrator.setUserName("woshishei");
        administrator.setUserPassword("0000");
        String json=mapper.writeValueAsString(administrator);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/admin/login")
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
