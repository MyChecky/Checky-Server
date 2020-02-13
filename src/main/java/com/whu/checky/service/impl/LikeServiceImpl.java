package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.EssayLike;
import com.whu.checky.mapper.AdministratorMapper;
import com.whu.checky.mapper.EssayLikeMapper;
import com.whu.checky.service.AdministratorService;
import com.whu.checky.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("likeService")
public class LikeServiceImpl implements LikeService {

    @Autowired
    private EssayLikeMapper essayLikeMapper;
    @Override
    public Integer Like(EssayLike essayLike) {
        return essayLikeMapper.like(essayLike.getUserId(),essayLike.getEssayId(),essayLike.getAddTime());
    }

    @Override
    public Integer UnLike(String userId,String essayId) {
        return essayLikeMapper.unLike(userId,essayId);
    }

    @Override
    public EssayLike queryLike(String userId, String essayId) {
        return essayLikeMapper.queryLike(userId,essayId);
    }

    @Override
    public List<EssayLike> queryAllLikeByEssayId(String essayId) {
        return essayLikeMapper.queryAllLikeByEssayId(essayId);
    }
}
