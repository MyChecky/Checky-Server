package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.AdminMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component("adminMenuMapper")
public interface AdminMenuMapper extends BaseMapper<AdminMenu> {
    //
}
