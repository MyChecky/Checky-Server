package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.whu.checky.domain.Essay;
import com.whu.checky.mapper.EssayMapper;
import com.whu.checky.service.EssayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("essayService")
public class EssayServiceImpl implements EssayService {
    @Autowired
    private EssayMapper essayMapper;
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
        return essayMapper.selectPage(
                page,
                new EntityWrapper<Essay>().orderBy("essay_time",false).orderBy("like_num"));
    }

    @Override
    public List<Essay> queryUserEssays(String userId) {
        return essayMapper.selectPage(
                new Page<Essay>(1,10),
                new EntityWrapper<Essay>()
                        .eq("user_id",userId)
                        .orderBy("essay_time",false));
    }

    @Override
    public Essay queryEssayById(String essayId) {
        return essayMapper.selectById(essayId);
    }
}
