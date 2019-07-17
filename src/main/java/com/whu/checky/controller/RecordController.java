package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Record;
import com.whu.checky.service.MoneyService;
import com.whu.checky.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @RequestMapping("/checkRecords")
    public JSONObject getCheckRecords(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String checkId= (String)object.get("checkId");
        List<Record> records=recordService.getRecordsByCheckId(checkId);
        JSONObject res=new JSONObject();
        Record textRecord=null;
        for (Record record: records){
            if (record.getRecordType().equals("text")){
                textRecord=record;
            }

        }
        records.remove(textRecord);
        res.put("text",textRecord);
        res.put("image",records);

        return res;
    }

    @RequestMapping("/essayRecords")
    public List<Record> getEssayRecords(@RequestBody String jsonstr){
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String essayId= (String)object.get("checkId");
        return recordService.getRecordsByEssayId(essayId);
    }
}
