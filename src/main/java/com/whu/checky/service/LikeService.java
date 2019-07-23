package com.whu.checky.service;

import com.whu.checky.domain.EssayLike;


public interface LikeService {
    //点赞
    Integer Like(EssayLike like);
    //点赞
    Integer UnLike(String userId,String essayId);
    //点赞
    EssayLike queryLike(String userId,String essayId);

}
