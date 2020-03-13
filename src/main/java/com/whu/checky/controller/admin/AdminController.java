package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Administrator;
import com.whu.checky.service.AdministratorService;
import com.whu.checky.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private RedisService redisService;

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/addAdmin")
    public HashMap<String, String> addAdmin(@RequestBody String body) throws Exception {
        JSONObject object = JSONObject.parseObject(body);
        HashMap<String,String> ans = new HashMap<>(); // 返回值
        String password = object.getString("password");
        String userName = object.getString("userName");
        String userTel = object.getString("userTel");
        String userEmail = object.getString("userEmail");
        /*"menus":[
            {"0": "money"},
            {"1": "task"}
        ]*/
        JSONArray arrayMenus = object.getJSONArray("menus");
        List<String> menus = new ArrayList<>();
        int i = 0;
        for(Object menu: arrayMenus){
            JSONObject menuJson = (JSONObject) menu;
            String menuStr = menuJson.getString(String.valueOf(i));
            menus.add(menuStr);
            i++;
        }
        // 添加admin到数据库
        Administrator administrator = new Administrator();
        administrator.setUserName(userName);
        administrator.setUserTel(userTel);
        administrator.setUserEmail(userEmail);
        administrator.setSessionId("has_never_login");
        administrator.setUserPassword(password);
        try{
            int registerResult = administratorService.register(administrator);
            if(registerResult == 1){
                log.error("Something Wrong When Register An Administrator!");
                log.error("Please Check The NAME Of The NEW Administrator!");
                ans.put("state", "fail");
                return ans;
            }
        }catch (Exception ex){
            log.error("Something Wrong When Register An Administrator!");
            log.error(ex.getMessage());
            ans.put("state", "fail");
            return ans;
        }
        administratorService.updateAdminMenus(administrator.getUserId(), menus);
        ans.put("state", "ok");
        return ans;
    }

    @PostMapping("/updateAdmin")
    public HashMap<String, String> updateAdmin(@RequestBody String body){
        JSONObject object = JSONObject.parseObject(body);
        String userId = object.getString("userId");
        String userName = object.getString("userName");
        String userTel = object.getString("userTel");
        String userEmail = object.getString("userEmail");
        /*"menus":[
            {"0": "money"},
            {"1": "task"}
        ]*/
        JSONArray arrayMenus = object.getJSONArray("menus");

        List<String> menus = new ArrayList<>();
        int i = 0;
        for(Object menu: arrayMenus){
            JSONObject menuJson = (JSONObject) menu;
            String menuStr = menuJson.getString(String.valueOf(i));
            menus.add(menuStr);
            i++;
        }
        administratorService.updateAdminMenus(userId, menus);
        Administrator administrator = administratorService.queryAdmin(userId);
        administrator.setUserName(userName);
        administrator.setUserTel(userTel);
        administrator.setUserEmail(userEmail);
        int updateState = administratorService.update(administrator);

        HashMap<String,String> ans = new HashMap<>();
        if(updateState == 1)
            ans.put("state", "ok");
        else
            ans.put("state", "fail");
        return ans;
    }

    @PostMapping("/login")
    public HashMap<String,Object> login(@RequestBody String body) {
        String message = "ok";
        HashMap<String,Object> ans = new HashMap<>();
        //构建Administrator
        Administrator administrator = parserJson2User(body);
        String sessionKey = UUID.randomUUID().toString();
        administrator.setSessionId(sessionKey);
        //处理登录
        try{
            int result = administratorService.login(administrator);
            if(result == 1)
                message = "missUsername";
            else if(result == 2)
                message = "wrongPassword";
            else{
                ans.put("sessionKey",sessionKey);
                ans.put("userId",administrator.getUserId());
                Map<String, Boolean> menus = administratorService.getAdminPowers(administrator.getUserId());
                ans.put("menus", menus);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        ans.put("state",message);
        return ans;
    }

    @PostMapping("logout")
    public HashMap<String,String> logout(@RequestBody String body){
        HashMap<String,String> ans = new HashMap<>();
        try {
            JSONObject data = JSON.parseObject(body);
            String sessionKey = data.getString("sessionKey");
            redisService.delSessionId(sessionKey);
            ans.put("state","ok");
        }catch (Exception e){
            ans.put("state","fail");
        }
        return ans;
    }

    @PostMapping("/all")
    public HashMap<String,Object> getAllAdmins(@RequestBody String body){
        int page = JSON.parseObject(body).getInteger("page");
        Integer pageSize = JSON.parseObject(body).getInteger("pageSize");
        if(pageSize == null){
            pageSize = 10;
        }
        Page<Administrator> p = new Page<Administrator>(page, pageSize);

        List<Administrator> adminList = administratorService.getAllAdmins(p);
        for(Administrator administrator: adminList){
            administrator.setUserPassword(null);
            List<String> permissions = administratorService.getPermissionsById(administrator.getUserId());
            administrator.setPermissions(permissions);
        }
        HashMap<String,Object> resp = new HashMap<>();
        resp.put("state","ok");
        resp.put("size",(int)Math.ceil(p.getTotal() / (double)pageSize));
        resp.put("total", p.getTotal());
        resp.put("admins",adminList);
        return resp;
    }

    @RequestMapping("/queryByKeyWord")
    HashMap<String,Object> queryUserByKeyWord(@RequestBody String body){
        String keyWord=JSON.parseObject(body).getString("keyword");
        int page = JSON.parseObject(body).getInteger("page");
        Integer pageSize = JSON.parseObject(body).getInteger("pageSize");
        if(pageSize == null){
            pageSize = 10;
        }
        Page<Administrator> p = new Page<Administrator>(page, pageSize);
        List<Administrator> adminList=administratorService.queryAdminsWithPage(p, keyWord);
        for(Administrator administrator: adminList){
            administrator.setUserPassword(null);
        }
        HashMap<String,Object> resp = new HashMap<>();
        resp.put("size", (int) Math.ceil(p.getTotal() / (double) pageSize));
        resp.put("total", p.getTotal());
        resp.put("state","ok");
        resp.put("admins",adminList);
        return resp;
    }

    @PostMapping("/query") // 查询单个用户
    HashMap<String,Object> queryAdmin(@RequestBody String body){
        String userId = JSONObject.parseObject(body).getString("userId");
        Administrator admin = administratorService.queryAdmin(userId);
        admin.setUserPassword(null);
        HashMap<String,Object> resp = new HashMap<>();
        resp.put("state","ok");
        resp.put("admin",admin);
        List<String> menus = administratorService.getPermissionsById(userId);
        resp.put("menus", menus);
        return resp;
    }

    @PostMapping("/deleteAdmin")
    HashMap<String, Object> deleteAdmin(@RequestBody String body){
        HashMap<String, Object> res = new HashMap<>();
        String userId = JSONObject.parseObject(body).getString("userId");
        Integer deleteResult = administratorService.deleteById(userId);
        if(deleteResult == 1)
            res.put("state", "ok");
        else
            res.put("state", "fail");
        return res;


    }

    private Administrator parserJson2User(String body){
        //解析json获取登录信息
        JSONObject object = JSONObject.parseObject(body);
        String name = object.getString("username");
        String password = object.getString("password");

        //构造登录用户
        Administrator administrator = new Administrator();
        administrator.setUserName(name);
        administrator.setUserPassword(password);

        return administrator;
    }

}

