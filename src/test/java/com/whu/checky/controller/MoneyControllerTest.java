package com.whu.checky.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whu.checky.domain.MoneyFlow;
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

import java.util.UUID;

//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(locations= {"classpath*:application.yml"})
public class MoneyControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception{
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void pay() throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        MoneyFlow moneyFlow = new MoneyFlow();
        moneyFlow.setFlowId(UUID.randomUUID().toString());
        moneyFlow.setUserID("000");
        moneyFlow.setIfTest(1);
        moneyFlow.setFlowIO("O");
        moneyFlow.setFlowType("pay");
        moneyFlow.setFlowMoney(0.0);
        moneyFlow.setFlowTime("2019-07-17 14:21:56");
        String json=mapper.writeValueAsString(moneyFlow);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/money/pay")
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
    public void payback() throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        String taskId = "2169ed87-d297-4de8-9fe4-095bc8d764b9";
        String json=mapper.writeValueAsString(taskId);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/money/payback")
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
    public void queryMoneyRecord() throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        String userId = "0";
        String json=mapper.writeValueAsString(userId);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/money/queryMoneyRecord")
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