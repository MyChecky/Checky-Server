package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.MoneyFlow;
import com.whu.checky.domain.Pay;
import com.whu.checky.service.MoneyService;
import com.whu.checky.util.MyConstants;
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
        Integer pageSize = json.getInteger("pageSize");
        if(pageSize == null){
            pageSize = 5;
        }
        Page<Pay> p = new Page<Pay>(page, pageSize);
        boolean isAsc = false;

        List<Pay> pays = moneyService.rechargeList(p, isAsc);

        HashMap<String, Object> resp = new HashMap<>();
        resp.put("pays", pays);
        resp.put("state", MyConstants.RESULT_OK);
        resp.put("size", (int)Math.ceil(p.getTotal() / (double)pageSize));
        resp.put("total", p.getTotal());
        return resp;
    }

    // 查询某人充值列表 注意page为0开始，不是1
    @PostMapping("/rechargeUser")
    public HashMap<String, Object> rechargeUser(@RequestBody String body) {
        JSONObject json = JSONObject.parseObject(body);
        int page = json.getInteger("page");
        String userId = json.getString("userId");

        Integer pageSize = json.getInteger("pageSize");
        if(pageSize == null){
            pageSize = 5;
        }
        Page<Pay> p = new Page<Pay>(page, pageSize);
        boolean isAsc = false;
        List<Pay> pays = moneyService.rechargeUser(userId, p, isAsc);

        HashMap<String, Object> resp = new HashMap<>();
        resp.put("pays", pays);
        resp.put("state", MyConstants.RESULT_OK);
        resp.put("size", (int)Math.ceil(p.getTotal() / (double)pageSize));
        resp.put("total", p.getTotal());
        return resp;
    }

    @PostMapping("queryByKeyword")
    public HashMap<String, Object> queryByKeyword(@RequestBody String body){
        /*startTime: this.startTime, endTime: this.endTime, searchType: this.searchType,
          keyword: this.keyword, page: this.page, pageSize: this.pageSize, payType: this.payType
        * */
        HashMap<String, Object> res = new HashMap<>();
        JSONObject object = (JSONObject) JSON.parse(body);
        String payType = object.getString("payType");
        String startTime = object.getString("startTime");
        startTime = startTime != null ? startTime : MyConstants.START_TIME;
        String endTime = object.getString("endTime");
        endTime = endTime != null ? endTime : MyConstants.END_TIME;
        String keyword = object.getString("keyword");
//        String searchType = object.getString("searchType");
        Integer page = object.getInteger("page");
        Integer pageSize = object.getInteger("pageSize");
        Page<MoneyFlow> p = new Page<>(page, pageSize);
        List<Pay> pays = moneyService.queryPaysForAdmin(p, startTime, endTime, payType, keyword);

        res.put("size", (int)Math.ceil(p.getTotal() / (double) pageSize));
        res.put("total", p.getTotal());
        res.put("state", MyConstants.RESULT_OK);
        res.put("pays", pays);
        return res;
    }
}
