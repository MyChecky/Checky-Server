package com.whu.checky.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.whu.checky.controller.TaskController;
import com.whu.checky.controller.TaskTypeController;
import com.whu.checky.controller.WechatController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class MockDataGen {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockDataGen.class);
    @Autowired
    private TaskController taskController;

    private String readClzPathResource(String path) throws IOException {
        File resource = new ClassPathResource(path).getFile();
        return new String(Files.readAllBytes(resource.toPath()));    
    }

    public void addTask() throws IOException {
        LOGGER.info(taskController.addTask(readClzPathResource("addTaskRequest.json")).toString());
    }

    public void addReport() {

    }
}