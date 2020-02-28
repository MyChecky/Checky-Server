package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.service.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin/moneyFlows")
@Component("AdminMoneyController")
public class MoneyController {

    @Autowired
    MoneyService moneyService;

    @PostMapping("/user")
    HashMap<String,Object> queryUser(@RequestBody String body){
        String userId = JSONObject.parseObject(body).getString("userId");
        int page = JSONObject.parseObject(body).getInteger("page");
        int pageSize = 5;
        String dateType = "DESC"; //dateType为 大写DESC或ASC

        List<MoneyFlow> moneyFlow = moneyService.queryUserMoneyFlowWithName(page, userId, pageSize, dateType);
        HashMap<String,Object> resp = new HashMap<>();
        resp.put("state","ok");
        resp.put("moneyFlow",moneyFlow);
        return resp;
    }

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
            resp.put("moneyFlows", moneyService.queryUserMoneyFlow(json.getString("userId"), p));
        } else {
            resp.put("moneyFlows", moneyService.queryAllMoneyFlow(p));
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
        HashMap<String, Object> ret = moneyService.getAllGraphData(year);
        ret.put("state", "ok");
        return ret;

    }

    //根据username模糊搜索的申诉
    @RequestMapping("/query")
    public JSONObject query(@RequestBody String jsonstr){
        JSONObject res=new JSONObject();
        JSONObject object= (JSONObject) JSON.parse(jsonstr);
        String username=object.getString("username");
        List<MoneyFlow> moneyFlows=moneyService.queryMoneyFlowByUserName(username);
        res.put("state","ok");
        res.put("moneyFlows",moneyFlows);
        return res;
    }

}
