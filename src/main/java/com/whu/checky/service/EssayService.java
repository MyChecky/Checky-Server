package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Essay;
<<<<<<< HEAD
import org.apache.ibatis.annotations.Param;
=======
>>>>>>> e6b0da8bd1a7a087f07027969ed1183606b33320

import java.util.List;

public interface EssayService {
    //发布动态
    int addEssay(Essay essay);
    //修改动态
    int modifyEssay(Essay essay);
    //删除动态
    int deleteEssay(String essayId);
    //上传文件
    void uploadFile();
    //展示动态
    List<Essay> displayEssay(Page<Essay> page);
    //展示动态
    List<Essay> displayEssay(String targetUserId, Page<Essay> page);
    //展示动态
    List<Essay> queryUserEssays(String userId);
    //展示动态
    Essay queryEssayById(String essayId);

    //展示动态
    List<Essay> queryEssaysByUserName(String username);


    int updateEssay(Essay essay);

    List<Essay> queryEssaysAll(Page<Essay> p, String startTime, String endTime);

    List<Essay> queryEssaysLikeNickname(Page<Essay> p, String startTime, String endTime, String keyword);

    List<Essay> queryEssaysLikeContent(Page<Essay> p, String startTime, String endTime, String keyword);
<<<<<<< HEAD

    //根据评论数排序
    List<Essay> sortByComment();

    //根据点赞数数排序
    List<Essay> sortByLike();

    //根据用户Id获取总评论数
    int getCommentSum(String userId);

    //根据用户Id获取总点赞数
    int getLikeSum(String userId);


=======
>>>>>>> e6b0da8bd1a7a087f07027969ed1183606b33320
}
