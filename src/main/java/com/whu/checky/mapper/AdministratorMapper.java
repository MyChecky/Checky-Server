package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Administrator;
import com.whu.checky.domain.Appeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "AdminMapper")
public interface AdministratorMapper extends BaseMapper<Administrator> {
    Administrator selectByName(@Param("userName") String userName);

    String selectMaxId();
}
