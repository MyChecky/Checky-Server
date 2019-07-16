package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Record;
import com.whu.checky.mapper.AppealMapper;
import com.whu.checky.mapper.RecordMapper;
import com.whu.checky.service.AppealService;
import com.whu.checky.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("recordService")
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;


    @Override
    public List<Record> getRecordsByCheckId(String checkId) {
        return recordMapper.selectList(new EntityWrapper<Record>().eq("check_id",checkId));
    }

    @Override
    public List<Record> getRecordsByEssayId(String EssayId) {
        return recordMapper.selectList(new EntityWrapper<Record>().eq("essay_id",EssayId));
    }
}
