package com.whu.checky.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import org.springframework.data.annotation.Id;

public class Comment {
    @TableId
    private String commentId;

    private String userId;

    private String essayId;

    /**
     * 评论时间
     */
    private String commentTime;
    /**
     * 评论内容
     */
    private String commentContent;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEssayId() {
        return essayId;
    }

    public void setEssayId(String essayId) {
        this.essayId = essayId;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}

