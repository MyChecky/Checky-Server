package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.whu.checky.domain.Administrator;
import com.whu.checky.mapper.AdministratorMapper;
import com.whu.checky.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.IntToDoubleFunction;

@Service("AdministratorService")
public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    private AdministratorMapper mapper;

    @Override
    public int register(Administrator administrator) throws Exception {
        List<Administrator> administrators = mapper.selectList(new EntityWrapper <Administrator>().eq("user_name",administrator.getUserName()));
        int length = administrators.size();
        if(length == 0) {
            //id自增
            String id = mapper.selectMaxId();
            int idNum = Integer.parseInt(id) + 1;
            administrator.setUserId(String.format("%d",idNum));
            mapper.insert(administrator);
            return 0;
        }
        else{
            return 1;
        }
    }

    @Override
    public int login(Administrator administrator) throws Exception{
        List<Administrator> temp = mapper.selectList(new EntityWrapper <Administrator>().eq("user_name",administrator.getUserName()));
        int length = temp.size();
        if(length == 0){
            return 1;
        }
        else if(!temp.get(0).getUserPassword().equals(administrator.getUserPassword())) {
            return 2;
        }
        else
            return 0;
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
