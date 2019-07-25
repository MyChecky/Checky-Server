package com.whu.checky.service;

import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Record;

import java.util.List;

public interface RecordService {

    List<Record> getRecordsByCheckId(String checkId);
    List<Record> getRecordsByEssayId(String EssayId);
    Integer deleteRecordById(String recordId);

}
