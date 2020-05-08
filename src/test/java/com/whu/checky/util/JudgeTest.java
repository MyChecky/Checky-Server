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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class JudgeTest {
    @Autowired
    Judge cls;

    String testCheckId = "f725e4c0e1604d59a246a1e6958fbb70";

    String testSupervisorId = "ow-ZO5TlM1wFcAqQLbvFN9SWJYtc";

    @Test
    public void supervisorPassTest() throws Exception{
        //Assert.assertEquals(true, cls.supervisorPass(testCheckId, testSupervisorId));
    }

    @Test
    public void checkinTest() throws Exception{
        cls.checkin();
    }

    @Test
    public void checkTaskSuccessTest() throws Exception{
        cls.checkTaskSuccess();
    }
}