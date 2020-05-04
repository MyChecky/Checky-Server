package com.whu.checky.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Assert;

import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.*;
import com.whu.checky.mapper.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributeTest {
    @Autowired
    Distribute cls;

    String testTaskId = "eac6210e-8867-4ac4-a04d-ee227b0f6f65";

    @Test   
    public void assignMoneyTest() throws Exception{
        Task task = cls.taskService.queryTask(testTaskId);

        cls.assignMoney(task);
    }
}