package com.whu.checky.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.whu.checky.domain.Administrator;
import com.whu.checky.mapper.AdministratorMapper;
import com.whu.checky.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.IntToDoubleFunction;

@Service("AdministratorService")
public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    private AdministratorMapper mapper;

    @Override
    public boolean register(Administrator administrator) throws Exception {
        if(mapper.selectByName(administrator.getUserName())==null) {
            //id自增
            String id = mapper.selectMaxId();
            int idNum = Integer.parseInt(id) + 1;
            administrator.setUserId(String.format("%d",idNum));
            mapper.insert(administrator);
            return true;
        }
        else{
            throw new Exception("已存在的用户名");
        }
    }

    @Override
    public boolean login(Administrator administrator) throws Exception{
        Administrator temp = mapper.selectByName(administrator.getUserName());
        if(temp == null){
            throw new Exception("不存在的用户名");
        }
        else if(!temp.getUserPassword().equals(administrator.getUserPassword())) {
            throw new Exception("密码错误");
        }
        else
            return true;
    }


    @Override
    public boolean update(Administrator administrator) {
        mapper.updateById(administrator);
        return true;
    }


    @Override
    public boolean deleteById(Administrator administrator) {
        mapper.deleteById(administrator);
        return false;
    }
}
