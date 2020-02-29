package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.AdministratorMapper;
import com.whu.checky.service.AdministratorService;
import com.whu.checky.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("administratorService")
public class AdministratorServiceImpl implements AdministratorService {

    @Autowired
    private AdministratorMapper mapper;

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
            administrator.setDepartment(temp.get(0).getDepartment());
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
    public List<Administrator> getAllAdmins(int page, int pageSize) {
        return mapper.selectPage(new Page<Administrator>(page, pageSize), new EntityWrapper<Administrator>().orderBy("user_id"));
    }

    @Override
    public int getAllAdminsNum() {
        return mapper.selectCount(new EntityWrapper<>());
    }

    @Override
    public List<Administrator> queryAdmins(int page, String keyword) {
        return mapper.selectPage(new Page<User>(page,10),new EntityWrapper<Administrator>().like("user_name",keyword
        ).orderBy("user_id"));
    }

    @Override
    public Administrator queryAdmin(String userId) {
        return mapper.selectById(userId);
    }


}
