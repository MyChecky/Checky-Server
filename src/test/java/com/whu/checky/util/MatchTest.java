package com.whu.checky.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.whu.checky.domain.Hobby;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.TaskType;
import com.whu.checky.domain.User;
import com.whu.checky.domain.UserFriend;
import com.whu.checky.domain.UserHobby;
import com.whu.checky.service.HobbyService;
import com.whu.checky.service.TaskService;
import com.whu.checky.service.TaskSupervisorService;
import com.whu.checky.service.TaskTypeService;
import com.whu.checky.service.UserFriendService;
import com.whu.checky.service.UserHobbyService;
import com.whu.checky.service.UserService;

import org.junit.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MatchTest {
    @Autowired
    Match cls;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    UserFriendService userFriendService;

    @Autowired
    UserHobbyService userHobbyService;

    @Autowired
    TaskSupervisorService taskSupervisorService;

    @Autowired
    HobbyService hobbyService;

    @Autowired
    TaskTypeService taskTypeService;

    @Test
    public void matchTest() throws Exception {
        Hobby running = new Hobby();
        running.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        running.setHobbyId(1);
        running.setHobbyValue("run");
        hobbyService.addHobby(running);

        Hobby swimming = new Hobby();
        swimming.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        swimming.setHobbyId(2);
        swimming.setHobbyValue("swim");
        hobbyService.addHobby(swimming);

        User taskOwner = new User();
        taskOwner.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
        taskOwner.setUserName("taskOwner");
        taskOwner.setUserGender(1);
        taskOwner.setUserAvatar("");
        taskOwner.setUserTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        taskOwner.setLongtitude(70);
        taskOwner.setLatitude(80);
        userService.register(taskOwner);

        User user1 = new User();
        user1.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
        user1.setUserName("user1");
        user1.setUserGender(1);
        user1.setUserAvatar("");
        user1.setUserTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        user1.setLongtitude(71);
        user1.setLatitude(82);
        userService.register(user1);

        User user2 = new User();
        user2.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
        user2.setUserName("user2");
        user2.setUserGender(1);
        user2.setUserAvatar("");
        user2.setUserTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        user2.setLongtitude(170);
        user2.setLatitude(120);
        userService.register(user2);

        UserHobby taskOwnerHobby = new UserHobby();
        taskOwnerHobby.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        taskOwnerHobby.setUserId(taskOwner.getUserId());
        taskOwnerHobby.setHobbyId(running.getHobbyValue());
        userHobbyService.addUserHobby(taskOwnerHobby);

        UserHobby user1Hobby = new UserHobby();
        user1Hobby.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        user1Hobby.setUserId(user1.getUserId());
        user1Hobby.setHobbyId(running.getHobbyValue()); 
        userHobbyService.addUserHobby(user1Hobby);

        UserHobby user2Hobby = new UserHobby();
        user2Hobby.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        user2Hobby.setUserId(user2.getUserId());
        user2Hobby.setHobbyId(swimming.getHobbyValue());
        userHobbyService.addUserHobby(user2Hobby);

        UserFriend taskOwnerFriendUser = new UserFriend();
        taskOwnerFriendUser.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        taskOwnerFriendUser.setFromUserId(taskOwner.getUserId());
        taskOwnerFriendUser.setToUserId(user1.getUserId());
        taskOwnerFriendUser.setAdd_state(1);
        userFriendService.addUserFriend(taskOwnerFriendUser);

        TaskType taskType = new TaskType();
        taskType.setTypeId("A");
        taskType.setTypeContent("");
        taskTypeService.addTaskType(taskType);

        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString().replaceAll("-", ""));
        task.setTypeId(taskType.getTypeId());
        task.setTaskTitle("");
        task.setTaskStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1)));
        task.setTaskEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        task.setCheckNum(3);
        task.setIfTest(1);
        task.setMinCheck(0.5);
        task.setMinCheckType("");
        task.setCheckFrec("1100111");

        task.setUserId(taskOwner.getUserId());
        task.setSupervisorNum(3);
        task.setTaskState("nomatch");
        task.setSupervisorType(2); // Match for strangers
        task.setIfArea(2); // Match users far away
        task.setIfHobby(1); // Match users with same hobbies
        taskService.addTask(task);

        long numTaskSupervisorsBeforeMatch = taskSupervisorService.getNumTaskSupervisors();

        cls.match();

        task = taskService.queryTask(task.getTaskId());
        final int expectedMatchNum = 2;
        Assert.assertEquals(expectedMatchNum, task.getMatchNum());
        Assert.assertEquals("nomatch", task.getTaskState());

        user1 = userService.queryUser(user1.getUserId());
        Assert.assertEquals(1, user1.getSuperviseNum().intValue());
        user2 = userService.queryUser(user2.getUserId());
        Assert.assertEquals(1, user2.getSuperviseNum().intValue());

        final List<TaskSupervisor> addedTaskSupervisorRecords = taskSupervisorService.getMostRecentRecords(expectedMatchNum);
        final String taskId = task.getTaskId();
        Set<String> expectedUserIds = new HashSet<>();
        expectedUserIds.add(user1.getUserId());
        expectedUserIds.add(user2.getUserId());

        addedTaskSupervisorRecords.forEach(ts -> {
            Assert.assertEquals(taskId, ts.getTaskId());
            Assert.assertTrue(expectedUserIds.contains(ts.getSupervisorId()));
            expectedUserIds.remove(ts.getSupervisorId());
        });

        Assert.assertEquals(numTaskSupervisorsBeforeMatch + expectedMatchNum, taskSupervisorService.getNumTaskSupervisors());

        addedTaskSupervisorRecords.stream().forEach(ts -> taskSupervisorService.delTaskSupervisor(ts));

        taskService.delTask(task.getTaskId());

        taskTypeService.DeleteTaskType(taskType.getTypeId());

        userFriendService.delUserFriend(taskOwnerFriendUser);

        userHobbyService.delUserHobby(user2Hobby);
        userHobbyService.delUserHobby(user1Hobby);
        userHobbyService.delUserHobby(taskOwnerHobby);

        userService.deleteUser(user2.getUserId());
        userService.deleteUser(user1.getUserId());
        userService.deleteUser(taskOwner.getUserId());

        hobbyService.delHobby(swimming);
        hobbyService.delHobby(running);
    }
}