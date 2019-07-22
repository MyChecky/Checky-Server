package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "CommentMapper")
public interface CommentMapper extends BaseMapper<Comment> {
}
