package com.whu.checky.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whu.checky.domain.Administrator;
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
public class ReportControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception{
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void addReport() throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        Report report = new Report();
        report.setReportTime("2019-07-16 21:53:26");
        report.setReportType("1");
        report.setCheckId("0");
        report.setUserId("0");
        report.setSupervisorId("0");
        report.setTaskId("0");
        report.setEssayId("0");
        report.setReportContent("");
        String json=mapper.writeValueAsString(report);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/report/add")
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
    public void queryUserReports() throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        String userId = "0";
        String json=mapper.writeValueAsString(userId);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/report/queryUserReports")
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
    public void deal() throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        Report report = new Report();
        report.setReportId("d1b855e2-b046-4099-852f-208c54a857e9");
        report.setReportContent("这个签到没有问题！");
        String json=mapper.writeValueAsString(report);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/report/deal")
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
        String reportId = "d1b855e2-b046-4099-852f-208c54a857e9";
        String json = mapper.writeValueAsString(reportId);

        System.out.println("before--------------------post");
        System.out.println(json);

        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/report/del")
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
