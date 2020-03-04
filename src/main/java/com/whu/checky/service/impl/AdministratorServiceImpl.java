package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.AdminMenu;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Menu;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.AdminMenuMapper;
import com.whu.checky.mapper.AdministratorMapper;
import com.whu.checky.mapper.MenuMapper;
import com.whu.checky.service.AdministratorService;
import com.whu.checky.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("administratorService")
public class AdministratorServiceImpl implements AdministratorService {

    @Autowired
    private AdministratorMapper mapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private AdminMenuMapper adminMenuMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public int register(Administrator administrator) throws Exception {
        List<Administrator> administrators = mapper.selectList(new EntityWrapper<Administrator>().eq("user_name", administrator.getUserName()));
        int length = administrators.size();
        if (length == 0) {
            //id自增
            String id = mapper.selectMaxId();
            int idNum = Integer.parseInt(id) + 1;
            administrator.setUserId(String.format("%d", idNum));
            mapper.insert(administrator);
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int login(Administrator administrator) throws Exception {
        List<Administrator> temp = mapper.selectList(new EntityWrapper<Administrator>().
                eq("user_name", administrator.getUserName()));
        int length = temp.size();
        if (length == 0) {
            return 1;
        } else if (!temp.get(0).getUserPassword().equals(administrator.getUserPassword())) {
            return 2;
        } else {
            if (temp.get(0).getSessionId() != null) redisService.delSessionId(temp.get(0).getSessionId());
            administrator.setUserId(temp.get(0).getUserId());
            mapper.updateById(administrator);
            redisService.saveUserOrAdminBySessionId(administrator.getSessionId(), administrator);
            return 0;
        }

    }

    @Override
    public int update(Administrator administrator) {
        return mapper.updateById(administrator);
    }

    @Override
    public boolean deleteById(Administrator administrator) {
        mapper.deleteById(administrator);
        return false;
    }

    @Override
    public List<Administrator> getAllAdmins(Page<Administrator> page) {
        return mapper.selectPage(page, new EntityWrapper<Administrator>().
                orderBy("user_id"));
    }

    @Override
    public int getAllAdminsNum() {
        return mapper.selectCount(new EntityWrapper<>());
    }

    @Override
    public List<Administrator> queryAdmins(int page, String keyword) {
        return mapper.selectPage(new Page<User>(page, 10), new EntityWrapper<Administrator>().like("user_name", keyword
        ).orderBy("user_id"));
    }

    @Override
    public Administrator queryAdmin(String userId) {
        return mapper.selectById(userId);
    }

    @Override
    public List<Administrator> queryAdminsWithPage(Page<Administrator> p, String keyWord) {
        return mapper.selectPage(p, new EntityWrapper<Administrator>().like("user_name", keyWord));
    }

    @Override
    public Map<String, Boolean> getAdminPowers(String userId) {
        List<AdminMenu> adminMenus = adminMenuMapper.selectList(new EntityWrapper<AdminMenu>()
                .eq("user_id", userId));
        Map<String, Boolean> re = new HashMap<>();
        for (AdminMenu adminMenu : adminMenus) {
            Menu menu = menuMapper.selectById(adminMenu.getMenuId());
            if (menu.getFlag() == 1) // 菜单状态可用
                re.put(menu.getMenuName(), true);
        }
        // 要把false的菜单一并返回
        List<Menu> menus = menuMapper.selectList(new EntityWrapper<Menu>());
        for (Menu menu : menus) {
            if (!re.containsKey(menu.getMenuName())) {
                re.put(menu.getMenuName(), false);
            }
        }
        return re;
    }


}
