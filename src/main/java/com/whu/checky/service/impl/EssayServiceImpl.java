package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.whu.checky.domain.Essay;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.EssayMapper;
import com.whu.checky.mapper.UserMapper;
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
    public List<Essay> sortByComment(){
        return essayMapper.selectList(
                new EntityWrapper<Essay>()
                        .setSqlSelect("user_id AS userId ")
                        .where("DATE_SUB(CURDATE(), INTERVAL 3 DAY) <= essay_time" )
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
                        .where("DATE_SUB(CURDATE(), INTERVAL 3 DAY) <= essay_time" )
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
}
