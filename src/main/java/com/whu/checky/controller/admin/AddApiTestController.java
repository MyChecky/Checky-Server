package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whu.checky.domain.AddApiTest;
import com.whu.checky.service.AddApiTestService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/addApiTest")
@Component("AdminAddApiTestController") // 防止组件名相同冲突
public class AddApiTestController {
    @Autowired
    AddApiTestService addApiTestService;

    @RequestMapping("/get")
    public JSONObject get(@RequestBody String jsonstr) {
        String id = JSON.parseObject(jsonstr).getString("id");
        AddApiTest addApiTest = addApiTestService.selectAddApiTestById(id);

        JSONObject object = new JSONObject();
        object.put("state", MyConstants.RESULT_OK);
        object.put("addApiTest", addApiTest);
        return object;
    }
}
