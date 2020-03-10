package com.whu.checky.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.whu.checky.domain.Hobby;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.TaskSupervisor;
import com.whu.checky.domain.User;
import com.whu.checky.domain.UserFriend;
import com.whu.checky.domain.UserHobby;
import com.whu.checky.service.HobbyService;
import com.whu.checky.service.TaskService;
import com.whu.checky.service.TaskSupervisorService;
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

    @Test
    public void matchTest() throws Exception {
        Hobby running = new Hobby();
        running.setHobbyId(UUID.randomUUID().toString());
        running.setHobbyValue("running");
        running.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        hobbyService.addHobby(running);

        Hobby swimming = new Hobby();
        swimming.setHobbyId(UUID.randomUUID().toString());
        swimming.setHobbyValue("swimming");
        swimming.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        hobbyService.addHobby(swimming);

        User taskOwner = new User();
        taskOwner.setUserId(UUID.randomUUID().toString());
        taskOwner.setLongtitude(70);
        taskOwner.setLatitude(80);
        userService.register(taskOwner);

        User user1 = new User();
        user1.setUserId(UUID.randomUUID().toString());
        user1.setLongtitude(71);
        user1.setLatitude(82);
        userService.register(user1);

        User user2 = new User();
        user2.setUserId(UUID.randomUUID().toString());
        user2.setLongtitude(170);
        user2.setLatitude(120);
        userService.register(user2);

        UserHobby taskOwnerHobby = new UserHobby();
        taskOwnerHobby.setUserId(taskOwner.getUserId());
        taskOwnerHobby.setHobbyId(running.getHobbyId());
        userHobbyService.addUserHobby(taskOwnerHobby);

        UserHobby user1Hobby = new UserHobby();
        user1Hobby.setUserId(user1.getUserId());
        user1Hobby.setHobbyId(running.getHobbyId());
        userHobbyService.addUserHobby(user1Hobby);

        UserHobby user2Hobby = new UserHobby();
        user2Hobby.setUserId(user2.getUserId());
        user2Hobby.setHobbyId(swimming.getHobbyId());
        userHobbyService.addUserHobby(user2Hobby);

        UserFriend taskOwnerFriendUser = new UserFriend();
        taskOwnerFriendUser.setFromUserId(taskOwner.getUserId());
        taskOwnerFriendUser.setToUserId(user1.getUserId());
        taskOwnerFriendUser.setAdd_state(1);
        userFriendService.addUserFriend(taskOwnerFriendUser);

        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString());
        task.setUserId(taskOwner.getUserId());
        task.setSupervisorNum(3);
        task.setTaskState("nomatch");
        task.setSupervisorType(2); // Match for strangers
        task.setIfArea(2); // Match users far away
        task.setIfHobby(1); // Match users with same hobbies
        taskService.addTask(task);

        long numTaskSupervisorPairs = taskSupervisorService.getNumTaskSupervisors();

        cls.match();

        Assert.assertEquals(2, task.getMatchNum());
        Assert.assertEquals("nomatch", task.getTaskState());

        Assert.assertEquals(1, user1.getSuperviseNum().intValue());
        Assert.assertEquals(1, user2.getSuperviseNum().intValue());

        List<TaskSupervisor> addedTaskSupervisorRecords = taskSupervisorService.getMostRecentRecords(2);
        Assert.assertEquals(task.getTaskId(), addedTaskSupervisorRecords.get(0).getTaskId());
        Assert.assertEquals(task.getTaskId(), addedTaskSupervisorRecords.get(1).getTaskId());
        Assert.assertTrue(
                addedTaskSupervisorRecords.stream().anyMatch(ts -> ts.getSupervisorId().equals(user1.getUserId())));
        Assert.assertTrue(
                addedTaskSupervisorRecords.stream().anyMatch(ts -> ts.getSupervisorId().equals(user2.getUserId())));

        Assert.assertEquals(numTaskSupervisorPairs + 2, taskSupervisorService.getNumTaskSupervisors());

        addedTaskSupervisorRecords.stream().forEach(ts -> taskSupervisorService.delTaskSupervisor(ts));

        taskService.delTask(task.getTaskId());

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