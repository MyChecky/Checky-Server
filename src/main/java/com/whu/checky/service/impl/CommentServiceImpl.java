package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Comment;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.AppealMapper;
import com.whu.checky.mapper.CommentMapper;
import com.whu.checky.mapper.UserMapper;
import com.whu.checky.service.AppealService;
import com.whu.checky.service.CommentService;
import com.whu.checky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public List<Comment> queryCommentByEssayId(String essayId) {
        List<Comment> comments= commentMapper.selectList(new EntityWrapper<Comment>().eq("essay_id",essayId)
                .orderBy("comment_time",true)
        );
        for (Comment comment:comments){
            User user=userMapper.selectById(comment.getUserId());
            comment.setUserName(user.getUserName());
            comment.setUserAvatar(user.getUserAvatar());
        }
        return comments;
    }

    @Override
    public Integer addComment(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public Integer deleteComment(String commentId) {
        return commentMapper.deleteById(commentId);
    }
}