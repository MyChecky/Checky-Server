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
    //根据最近3天各个用户所有打卡的点赞数进行排序
//    List<Essay> sortByLike();
    //根据最近3天各个用户所有打卡的评论数进行排序
//    List<Essay> sortByComment();
    //获取最近三天某个用户的所有打卡评论总数
    int getCommentSum(@Param("userId") String userId);
    //获取最近三天某个用户的所有打卡点赞总数
    int getLikeSum(@Param("userId") String userId);
}
