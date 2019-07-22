package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Like;
import com.whu.checky.mapper.AdministratorMapper;
import com.whu.checky.mapper.LikeMapper;
import com.whu.checky.service.AdministratorService;
import com.whu.checky.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("likeService")
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;
    @Override
    public Integer Like(Like like) {
        return likeMapper.insert(like);
    }

    @Override
    public Integer UnLike(String userId,String essayId) {
        return likeMapper.unLike(userId,essayId);
    }

    @Override
    public Integer queryLike(String userId, String essayId) {
        return likeMapper.queryLike(userId,essayId);
    }
}
