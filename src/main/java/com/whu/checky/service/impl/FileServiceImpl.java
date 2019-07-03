package com.whu.checky.service.impl;

import com.whu.checky.domain.Record;
import com.whu.checky.mapper.RecordMapper;
import com.whu.checky.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("FileService")
public class FileServiceImpl implements FileService {


    @Autowired
    RecordMapper recordMapper;

    @Override
    public String saveFile2Database(Record record) {
        String recordId = UUID.randomUUID().toString().substring(0,12);
        record.setRecordId(recordId);
        recordMapper.insert(record);
        return recordId;
    }
}
