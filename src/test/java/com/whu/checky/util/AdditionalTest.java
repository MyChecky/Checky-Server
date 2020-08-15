<<<<<<< HEAD
package com.whu.checky.util;

import com.whu.checky.service.TaskTypeService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdditionalTest {
    @Autowired
    TaskTypeService taskTypeService;

    @Test
    public void checkUpdatePassNum() {
        taskTypeService.incPassNum("1747492e-1dd8-4975-82f8-32b204e49891");
    }

    @Test
    public void checkUpdateTotalNum() {
        taskTypeService.incTotalNum("1747492e-1dd8-4975-82f8-32b204e49891");
    }
=======
package com.whu.checky.util;

import com.whu.checky.service.TaskTypeService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdditionalTest {
    @Autowired
    TaskTypeService taskTypeService;

    @Test
    public void checkUpdatePassNum() {
        taskTypeService.incPassNum("1747492e-1dd8-4975-82f8-32b204e49891");
    }

    @Test
    public void checkUpdateTotalNum() {
        taskTypeService.incTotalNum("1747492e-1dd8-4975-82f8-32b204e49891");
    }
>>>>>>> e6b0da8bd1a7a087f07027969ed1183606b33320
}