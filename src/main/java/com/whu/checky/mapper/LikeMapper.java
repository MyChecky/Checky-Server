package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component("LikeMapper")
public interface LikeMapper extends BaseMapper<Like> {
    Integer queryLike(@Param("userId")String userId,@Param("essayId")String essayId);
    Integer unLike(@Param("userId")String userId,@Param("essayId")String essayId);
}
