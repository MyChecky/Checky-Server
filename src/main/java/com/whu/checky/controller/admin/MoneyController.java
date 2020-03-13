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
    HashMap<String, Object> queryUser(@RequestBody String body) {
        String userId = JSONObject.parseObject(body).getString("userId");
        int page = JSONObject.parseObject(body).getInteger("page");
        Integer pageSize = JSON.parseObject(body).getInteger("pageSize");
        if(pageSize == null){
            pageSize = 10;
        }
        Page<MoneyFlow> p = new Page<MoneyFlow>(page, pageSize);
        boolean isAsc = false;

        List<MoneyFlow> moneyFlows = moneyService.queryUserMoneyFlowWithName(userId, p, isAsc);
        HashMap<String, Object> resp = new HashMap<>();
        resp.put("state", "ok");
        resp.put("moneyFlow", moneyFlows);
        resp.put("size", (int)Math.ceil(p.getTotal() / (double)pageSize));
        resp.put("total", p.getTotal());
        return resp;
    }

    @PostMapping("/all")
    public HashMap<String, Object> all(@RequestBody String body) {
        JSONObject json = JSONObject.parseObject(body);
        int page = json.getInteger("page");
        Integer pageSize = JSON.parseObject(body).getInteger("pageSize");
        if(pageSize == null){
            pageSize = 5;
        }
        Page<MoneyFlow> p = new Page<MoneyFlow>(page, pageSize);
        boolean isAsc = false;

        List<MoneyFlow> moneyFlows = moneyService.queryAllMoneyFlows(p, isAsc);
        HashMap<String, Object> resp = new HashMap<>();
        resp.put("state", "ok");
        resp.put("moneyFlow", moneyFlows);
        resp.put("size",  (int)Math.ceil(p.getTotal() / (double)pageSize));
        resp.put("total", p.getTotal());
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
    public JSONObject query(@RequestBody String jsonstr) {
        /*
        * moneyType: this.moneyType, moneyTest: this.moneyTest, moneyIO: this.moneyIO,
          startTime: this.startTime, endTime: this.endTime, searchType: this.searchType,
          keyword: this.keyword, page: this.page, pageSize: this.pageSize
        * */
        JSONObject res = new JSONObject();
        JSONObject object = (JSONObject) JSON.parse(jsonstr);
        String moneyType = object.getString("moneyType");
        int moneyTest = object.getInteger("moneyTest");
        String moneyIO = object.getString("moneyIO");
        String startTime = object.getString("startTime");
        startTime = startTime != null ? startTime : "1970-01-01";
        String endTime = object.getString("endTime");
        endTime = endTime != null ? endTime : "2999-01-01";
        String keyword = object.getString("keyword");
//        String searchType = object.getString("searchType");
        Integer page = object.getInteger("page");
        Integer pageSize = object.getInteger("pageSize");

        Page<MoneyFlow> p = new Page<>(page, pageSize);
        List<MoneyFlow> moneyFlows = moneyService.queryMoneyflowsForAdmin(p, startTime, endTime, moneyType, moneyIO,
                moneyTest, keyword);

        res.put("size", (int)Math.ceil(p.getTotal() / (double) pageSize));
        res.put("total", p.getTotal());
        res.put("state", "ok");
        res.put("moneyFlows", moneyFlows);
        return res;
    }

}
