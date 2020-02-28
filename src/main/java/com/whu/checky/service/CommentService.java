package com.whu.checky.service;

import com.whu.checky.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> queryCommentByEssayId(String essayId);
    Integer addComment(Comment comment);
    Integer deleteComment(String commentId);

    Comment queryCommentById(String commentId);

    int updateComment(Comment comment);
}
