package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.EssayLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component("essayLikeMapper")
public interface EssayLikeMapper extends BaseMapper<EssayLike> {
    Integer like(@Param("userId")String userId,@Param("essayId")String essayId,@Param("addTime")String addTime);
    EssayLike queryLike(@Param("userId")String userId,@Param("essayId")String essayId);
    Integer unLike(@Param("userId")String userId,@Param("essayId")String essayId);
}
