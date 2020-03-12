package com.whu.checky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Administrator;
import com.whu.checky.service.AdministratorService;
import com.whu.checky.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/register")
    public String register(@RequestBody String body){
        String message = "ok";

        //构建Administrator
        Administrator administrator = parserJson2User(body);

        //处理登录
        try{
            int result = administratorService.register(administrator);
            if(result == 1)
                message = "duplicatename";
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        //
        return message;
    }

    @PostMapping("/updateAdmin")
    public HashMap<String, String> updateAdmin(@RequestBody String body){
        JSONObject object = JSONObject.parseObject(body);
        String userId = object.getString("userId");
        String userName = object.getString("userName");
        String userTel = object.getString("userTel");
        String userEmail = object.getString("userEmail");
        String department = object.getString("department");
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
        Map<String, Boolean> menus = administratorService.getAdminPowers(userId);
        resp.put("menus", menus);
        return resp;
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

