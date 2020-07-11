package com.whu.checky.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whu.checky.domain.AddApiTest;
import com.whu.checky.domain.AddApiTestAux;
import com.whu.checky.service.AddApiTestService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/addApiTest")
public class AddApiTestController {
    @Autowired
    AddApiTestService addApiTestService;

    @RequestMapping("/add")
    public JSONObject add(@RequestBody String jsonstr) {
        AddApiTest addApiTest = JSON.parseObject(jsonstr, new TypeReference<AddApiTest>() {
        });
        addApiTest.setAddApiId(UUID.randomUUID().toString());                     // 生成主键 id
        addApiTest.setAddApiTime(MyConstants.DATETIME_FORMAT.format(new Date()));  // 记录添加时间
        int result = addApiTestService.addAddApiTest(addApiTest);
        String addResult = result == 1 ? MyConstants.RESULT_OK : MyConstants.RESULT_FAIL;

        JSONObject object = new JSONObject();
        object.put("state", addResult);
        return object;
    }

    @RequestMapping("/get")
    public JSONObject get(@RequestBody String jsonstr) {
        String id = JSON.parseObject(jsonstr).getString("id");
        AddApiTestAux addApiTestAux = addApiTestService.selectAddApiTestSafelyById(id);

        JSONObject object = new JSONObject();
        object.put("state", MyConstants.RESULT_OK);
        object.put("addApiTest", addApiTestAux);
        return object;
    }
}
