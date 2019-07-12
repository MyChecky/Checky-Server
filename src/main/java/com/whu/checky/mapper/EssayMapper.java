package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Essay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component("essayMapper")
public interface EssayMapper extends BaseMapper<Essay> {
}
