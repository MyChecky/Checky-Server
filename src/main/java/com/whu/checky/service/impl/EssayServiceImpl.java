package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.whu.checky.domain.*;
import com.whu.checky.mapper.*;
import com.whu.checky.service.EssayService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("essayService")
public class EssayServiceImpl implements EssayService {
    @Autowired
    private EssayMapper essayMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskTagMapper taskTagMapper;

    @Autowired
    private CheckMapper checkMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public int addEssay(Essay essay) {
        return essayMapper.insert(essay);
    }

    @Override
    public int modifyEssay(Essay essay) {
        return essayMapper.updateById(essay);
    }

    @Override
    public int deleteEssay(String essayId) {
        return essayMapper.deleteById(essayId);
    }

    @Override
    public void uploadFile() {

    }

    @Override
    public List<Essay> displayEssay(Page<Essay> page) {
        return essayMapper.selectPage(page, new EntityWrapper<Essay>()
                .eq("if_delete", 0)
                .orderBy("essay_time", false)
                .orderBy("like_num"));
    }

    @Override
    public List<Essay> displayEssayByTopicId(Page<Essay> page, String topicId) {
        return essayMapper.selectPage(page, new EntityWrapper<Essay>()
                .eq("topic_id", topicId)
                .eq("if_delete", 0)
                .orderBy("essay_time", false)
                .orderBy("like_num"));
    }

    @Override
    public List<Essay> displayEssayByTagId(Page<Essay> page, String tagId) {
        List<Essay> essayListRes = new ArrayList<>();
        // get task_id by tag_id
        List<TaskTag> taskTagList = taskTagMapper.selectList(new EntityWrapper<TaskTag>()
                .eq("tag_id", tagId));
        if (taskTagList.isEmpty()) return essayListRes;
        List<String> taskIdList = new ArrayList<>();
        for (TaskTag taskTag : taskTagList)
            taskIdList.add(taskTag.getTaskId());

        // get check_id by task_id
        List<Check> checkList = checkMapper.selectList(new EntityWrapper<Check>()
                .in("task_id", taskIdList));
        if (checkList.isEmpty()) return essayListRes;
        List<String> checkIdList = new ArrayList<>();
        for (Check check : checkList)
            checkIdList.add(check.getCheckId());

        // get essay_id by check_id
        List<Record> recordList = recordMapper.selectList(new EntityWrapper<Record>()
                .in("check_id", checkIdList)
                .isNotNull("essay_id"));
        if (recordList.isEmpty()) return essayListRes;
        List<String> essayIdList = new ArrayList<>();
        for (Record record : recordList)
            essayIdList.add(record.getEssayId());

        // get essay by essay_id
        return essayMapper.selectPage(page, new EntityWrapper<Essay>()
                .in("essay_id", essayIdList)
                .orderBy("essay_time", false));
    }

    @Override
    public List<Essay> displayEssay(String targetUserId, Page<Essay> page) {
        return essayMapper.selectPage(
                page,
                new EntityWrapper<Essay>()
                        .eq("user_id", targetUserId)
                        .and()
                        .eq("if_delete", 0)
                        .orderBy("essay_time", false)
                        .orderBy("like_num"));
    }

    @Override
    public List<Essay> queryUserEssays(String userId) {
        return essayMapper.selectPage(
                new Page<Essay>(1, 10),
                new EntityWrapper<Essay>()
                        .eq("user_id", userId)
                        .and()
                        .eq("if_delete", 0)
                        .orderBy("essay_time", false));
    }

    @Override
    public Essay queryEssayById(String essayId) {
        return essayMapper.selectById(essayId);
    }

    @Override
    public List<Essay> queryEssaysByUserName(String username) {
        return essayMapper.queryEssaysByUserName(username);
    }

    @Override
    public int updateEssay(Essay essay) {
        return essayMapper.updateById(essay);
    }

    @Override
    public List<Essay> queryEssaysAll(Page<Essay> p, String startTime, String endTime) {
        return essayMapper.selectPage(p, new EntityWrapper<Essay>()
                .ge("essay_time", startTime)
                .le("essay_time", endTime)
                .orderBy("essay_time", false));
    }

    @Override
    public List<Essay> queryEssaysLikeNickname(Page<Essay> p, String startTime, String endTime, String keyword) {
        List<User> users = userMapper.selectList(new EntityWrapper<User>().like("user_name", keyword));
        List<String> userIds = new ArrayList<>();
        for (User user : users) {
            userIds.add(user.getUserId());
        }
        return essayMapper.selectPage(p, new EntityWrapper<Essay>()
                .in("user_id", userIds)
                .andNew()
                .ge("essay_time", startTime)
                .or()
                .le("essay_time", endTime)
                .orderBy("essay_time", false));
    }

    @Override
    public List<Essay> queryEssaysLikeContent(Page<Essay> p, String startTime, String endTime, String keyword) {
        return essayMapper.selectPage(p, new EntityWrapper<Essay>()
                .like("essay_content", keyword)
                .ge("essay_time", startTime)
                .le("essay_time", endTime)
                .orderBy("essay_time", false));
    }

    @Override
    public List<Essay> sortByComment() {
        return essayMapper.selectList(
                new EntityWrapper<Essay>()
                        .setSqlSelect("user_id AS userId ")
                        .where("DATE_SUB(CURDATE(), INTERVAL 3 DAY) <= essay_time")
                        .and()
                        .eq("if_delete", 0)
                        .groupBy("user_id")
                        .orderBy("sum(comment_num)")
                        .last("desc")
        );
    }


    @Override
    public List<Essay> sortByLike() {
        return essayMapper.selectList(
                new EntityWrapper<Essay>()
                        .setSqlSelect("user_id AS userId ")
                        .where("DATE_SUB(CURDATE(), INTERVAL 3 DAY) <= essay_time")
                        .and()
                        .eq("if_delete", 0)
                        .groupBy("user_id")
                        .orderBy("sum(like_num)")
                        .last("desc")
        );
    }

    @Override
    public int getCommentSum(String userId) {
        return essayMapper.getCommentSum(userId);
    }

    @Override
    public int getLikeSum(String userId) {
        return essayMapper.getLikeSum(userId);
    }

    @Override
    public String deleteEssayByTopicId(String topicId) {
//        essayMapper.delete(new EntityWrapper<Essay>()
//                .eq("topic", topicId)
//        );

        List<Essay> essayList = essayMapper.selectList(new EntityWrapper<Essay>()
                .eq("topic_id", topicId));
        for(Essay essay: essayList){
            essay.setTopicId(null);
            essayMapper.updateById(essay);
        }

        return MyConstants.RESULT_OK;
    }
}
