package com.whu.checky.controller;

import com.whu.checky.domain.Essay;
import com.whu.checky.domain.User;
import com.whu.checky.service.EssayService;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/userRank")
public class UserRankController {
    @Autowired
    private UserService userService;
    @Autowired
    private EssayService essayService;

    //根据评论数排序
    @RequestMapping("/rankByComment")
    public List<UserAndNum> sortByComment(@RequestBody String jsonstr) {
        List<Essay> essayRankList = essayService.sortByComment();
        //返回值
        List<UserAndNum> RankList = new ArrayList<>();
        for(int i=0; i<essayRankList.size(); i++) {
            Essay essay = essayRankList.get(i);
            RankList.add(new UserAndNum(userService.queryUser(essay.getUserId()),essayService.getCommentSum(essay.getUserId())));
        }
        return RankList;
    }

    //根据点赞数排序
    @RequestMapping("/rankByLike")
    public List<UserAndNum> sortByLike(@RequestBody String jsonstr) {
        List<Essay> essayRankList = essayService.sortByLike();
        List<UserAndNum> RankList = new ArrayList<>();
        for(int i=0; i<essayRankList.size(); i++) {
            Essay essay = essayRankList.get(i);
            RankList.add(new UserAndNum(userService.queryUser(essay.getUserId()),essayService.getLikeSum(essay.getUserId())));
        }
        return RankList;
    }
}

    class UserAndNum
    {
        private User user;
        private int num;

        public UserAndNum(User user,int num)
        {
            this.user=user;
            this.num=num;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }