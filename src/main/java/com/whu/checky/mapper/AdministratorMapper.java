package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Appeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "AdministratorMapper")
public interface AdministratorMapper extends BaseMapper<Administrator> {
    String selectMaxId();
    String getAdminById(String userId);
}
