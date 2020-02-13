package com.whu.checky.service;

import com.whu.checky.domain.EssayLike;

import java.util.List;


public interface LikeService {
    //点赞
    Integer Like(EssayLike like);
    //取消点赞
    Integer UnLike(String userId,String essayId);
    //查询点赞
    EssayLike queryLike(String userId,String essayId);
    // 文章所有点赞
    List<EssayLike> queryAllLikeByEssayId(String essayId);

}
