<<<<<<< HEAD
package com.whu.checky.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskTypeMapperTest {
    @Autowired
    TaskTypeMapper taskTypeMapper;

    private String typeId = "1747492e-1dd8-4975-82f8-32b204e49891";

    @Test
    public void test() {
        Long totalNum = taskTypeMapper.selectById(typeId).getTotalNum();
        taskTypeMapper.incTotalNum(typeId);
        Assert.assertEquals((Long)(totalNum + 1), taskTypeMapper.selectById(typeId).getTotalNum());
    }
=======
package com.whu.checky.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskTypeMapperTest {
    @Autowired
    TaskTypeMapper taskTypeMapper;

    private String typeId = "1747492e-1dd8-4975-82f8-32b204e49891";

    @Test
    public void test() {
        Long totalNum = taskTypeMapper.selectById(typeId).getTotalNum();
        taskTypeMapper.incTotalNum(typeId);
        Assert.assertEquals((Long)(totalNum + 1), taskTypeMapper.selectById(typeId).getTotalNum());
    }
>>>>>>> e6b0da8bd1a7a087f07027969ed1183606b33320
}