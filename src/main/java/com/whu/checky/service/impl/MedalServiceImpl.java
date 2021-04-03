package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Medal;
import com.whu.checky.domain.Task;
import com.whu.checky.domain.UserMedal;
import com.whu.checky.mapper.MedalMapper;
import com.whu.checky.mapper.TaskMapper;
import com.whu.checky.mapper.UserMedalMapper;
import com.whu.checky.service.MedalService;
import com.whu.checky.service.TaskService;
import com.whu.checky.util.MyConstants;
import com.whu.checky.util.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("/medalService")
public class MedalServiceImpl implements MedalService {
    @Autowired
    private MedalMapper medalMapper;

    @Autowired
    private UserMedalMapper userMedalMapper;

    @Override
    public List<Medal> getMedalListByUserId(String userId) {
        List<Medal> medalListRes = new ArrayList<>();

        List<UserMedal> userMedalList = userMedalMapper.selectList(new EntityWrapper<UserMedal>()
                .eq("user_id", userId));
        if (userMedalList.isEmpty()) return medalListRes;

        List<String> medalIdList = new ArrayList<>();
        for (UserMedal userMedal : userMedalList)
            medalIdList.add(userMedal.getMedalId());
        return medalMapper.selectList(new EntityWrapper<Medal>()
                .in("medal_id", medalIdList));
    }
}
