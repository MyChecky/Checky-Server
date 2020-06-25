package com.whu.checky.util;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MockDataGenTest {
    @Autowired
    private MockDataGen clz;

    @Test
    public void addTask() throws IOException {
        clz.addTask();
    }

    @Test
    public void addReport() throws IOException {
        clz.addReport();
    }

    @Test
    public void porcessReport() throws IOException {
        clz.processReport();
    }

    @Test
    public void addCheck() throws IOException {
        clz.addCheck();
    }

    @Test
    public void addSupervise() throws IOException {
        clz.addSupervise();
    }

    @Test
    public void checkIn() {
        clz.checkIn();
    }

    @Test
    public void checkTaskSuccess() {
        clz.checkTaskSuccess();
    }

    @Test
    public void assignMoney() {
        clz.assignMoney();
    }
}