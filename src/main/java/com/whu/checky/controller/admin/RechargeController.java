package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Pay;
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
@RequestMapping("/admin/moneyRecharge")
@Component("AdminRechargeController")
public class RechargeController {

    @Autowired
    MoneyService moneyService;

    // 建议后期增加 排序方式/每页显示数量。。。
    // 查询充值列表 注意page为0开始，不是1
    @PostMapping("/rechargeList")
    public HashMap<String, Object> rechargeList(@RequestBody String body) {
        JSONObject json = JSONObject.parseObject(body);
        int page = json.getInteger("page");

        int pageSize = 5;
        String dateType = "DESC"; //dateType为 大写DESC或ASC
        List<Pay> pays = moneyService.rechargeList(page, pageSize, dateType);

        HashMap<String, Object> resp = new HashMap<>();
        resp.put("pays", pays);
        resp.put("state", "ok");
        resp.put("size", (int)Math.ceil(moneyService.rechargeListSize() / (double)pageSize));
        return resp;
    }

    // 查询某人充值列表 注意page为0开始，不是1
    @PostMapping("/rechargeUser")
    public HashMap<String, Object> rechargeUser(@RequestBody String body) {
        JSONObject json = JSONObject.parseObject(body);
        int page = json.getInteger("page");
        String userId = json.getString("userId");

        int pageSize = 5;
        String dateType = "DESC"; //dateType为 大写DESC或ASC
        List<Pay> pays = moneyService.rechargeUser(page, userId, pageSize, dateType);

        HashMap<String, Object> resp = new HashMap<>();
        resp.put("pays", pays);
        resp.put("state", "ok");
        resp.put("size", (int)Math.ceil(moneyService.rechargeUserSize(userId) / (double)pageSize));
        return resp;
    }
}
