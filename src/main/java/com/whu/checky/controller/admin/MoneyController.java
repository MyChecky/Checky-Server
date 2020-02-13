package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.service.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin/moneyFlows")
@Component("AdminMoneyController")
public class MoneyController {

    @Autowired
    MoneyService moneyService;

    @PostMapping("/all")
    public HashMap<String, Object> all(@RequestBody String body) {
        JSONObject json = JSONObject.parseObject(body);
        int page = json.getInteger("page");
        Page<MoneyFlow> p = null;

        if (page != -1) {
            p = new Page<>(page, 5);
        }

        HashMap<String, Object> resp = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        if (json.containsKey("userId")) {
            resp.put("type", "userId");
            resp.put("moneyFlows", moneyService.queryUserTestMoneyFlow(json.getString("userId"), p));
        } else {
            resp.put("moneyFlows", moneyService.queryAllTestMoneyFlow(p));
            resp.put("type", "all");
        }
        if (p != null) resp.put("moneyFlowsSize", p.getTotal());
        resp.put("state", "ok");
        return resp;
    }

    @PostMapping("/graph")
    public HashMap<String, Object> graph(@RequestBody String body) {
        JSONObject json = JSONObject.parseObject(body);
        String year = json.getString("year");
        HashMap<String, Object> ret = moneyService.getTestGraphData(year);
        ret.put("state", "ok");
        return ret;

    }

    //根据username模糊搜索的申诉
    @RequestMapping("/query")
    public JSONObject query(@RequestBody String jsonstr){
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String username=object.getString("username");
        List<MoneyFlow> moneyFlows=moneyService.queryTestMoneyFlowByUserName(username);
        res.put("state","ok");
        res.put("moneyFlows",moneyFlows);
        return res;
    }

}
