package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.AppealMapper;
import com.whu.checky.mapper.UserMapper;
import com.whu.checky.service.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("appealService")
public class AppealServiceImpl implements AppealService {

    @Autowired
    private AppealMapper mapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean addAppeal(Appeal appeal) {
        try {
            mapper.insert(appeal);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteAppeal(String appealId) {
        int n = mapper.deleteById(appealId);
        return n != 0;
    }

    @Override
    public Appeal getAppealById(String appealId) {
        return mapper.selectById(appealId);
    }

    @Override
    public Integer updateAppeal(Appeal appeal) {
        return mapper.updateById(appeal);
    }

    @Override
    public List<Appeal> queryAppealFromUser(String userId) {
        return mapper.selectList(new EntityWrapper<Appeal>().
                eq("user_id", userId)
                .orderBy("appeal_time", false)
        );

    }


    @Override
    public List<Appeal> displayAppeals(Page<Appeal> page) {
        return mapper.selectPage(page, new EntityWrapper<Appeal>()
                .orderBy("appeal_time", true));
    }

    @Override
    public List<Appeal> queryAppealByUserName(String username) {
        return mapper.queryAppealByUserName(username);
    }

    @Override
    public List<Appeal> queryAppealsAll(Page<Appeal> p, String startTime, String endTime) {
        List<Appeal> appeals = mapper.selectPage(p, new EntityWrapper<Appeal>()
                .ge("appeal_time", startTime)
                .le("appeal_time", endTime)
                .orderBy("appeal_time", false));
        return appeals;
    }

    @Override
    public List<Appeal> queryAppealsLikeNickname(Page<Appeal> p, String startTime, String endTime, String keyword) {
        List<User> users = userMapper.selectList(new EntityWrapper<User>().like("user_name", keyword));
        List<String> userIds = new ArrayList<>();
        for (User user : users) {
            userIds.add(user.getUserId());
        }

        List<Appeal> appeals = mapper.selectPage(p, new EntityWrapper<Appeal>()
                .in("user_id", userIds)
                .ge("appeal_time", startTime)
                .le("appeal_time", endTime)
                .orderBy("appeal_time", false));
        return appeals;
    }

    @Override
    public List<Appeal> queryAppealsLikeContent(Page<Appeal> p, String startTime, String endTime, String keyword) {
        List<Appeal> appeals = mapper.selectPage(p, new EntityWrapper<Appeal>()
                .like("appeal_content", keyword)
                .ge("appeal_time", startTime)
                .le("appeal_time", endTime)
                .orderBy("appeal_time", false));
        return appeals;
    }

//    @Override
//    public int queryAllAppealNum() {
//        return  mapper.selectCount(new EntityWrapper<>());
//    }

}
