package com.whu.checky.service;

import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Like;

import java.util.List;

public interface LikeService {
    //点赞
    Integer Like(Like like);
    //点赞
    Integer UnLike(String userId,String essayId);
    //点赞
    Integer queryLike(String userId,String essayId);

}
