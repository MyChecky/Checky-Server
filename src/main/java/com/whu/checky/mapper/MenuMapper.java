package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "MenuMapper")
public interface MenuMapper extends BaseMapper<Menu> {
    //还没写都要查什么,可能有这俩
    String getMenuFlagByName(@Param("menuName") String menuName);
    String getMenuURLByName(@Param("menuName") String menuName);

}
