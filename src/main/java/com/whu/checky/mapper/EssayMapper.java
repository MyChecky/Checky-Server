package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Essay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("essayMapper")
public interface EssayMapper extends BaseMapper<Essay> {
    List<Essay> queryEssaysByUserName(@Param("username") String username);
}
