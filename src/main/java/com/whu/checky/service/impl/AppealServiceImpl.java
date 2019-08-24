package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Appeal;
import com.whu.checky.mapper.AppealMapper;
import com.whu.checky.service.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("appealService")
public class AppealServiceImpl implements AppealService {

    @Autowired
    private AppealMapper mapper;

    @Override
    public boolean addAppeal(Appeal appeal) {
        try{
            mapper.insert(appeal);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteAppeal(String appealId) {
        int n = mapper.deleteById(appealId);
        return n!=0;
    }

    @Override
    public void updateAppeal() {

    }

    @Override
    public List<Appeal> queryAppealFromUser(String userId) {
        return mapper.selectList(new EntityWrapper<Appeal>().
                eq("user_id",userId)
                .orderBy("appeal_time",false)
        );

    }


    @Override
    public List<Appeal> displayAppeal(Page<Appeal> page) {
        return mapper.selectPage(
                page,
                new EntityWrapper<Appeal>().orderBy("appeal_time",true));
    }


}
