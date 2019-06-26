package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Suggestion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SuggestionMapper extends BaseMapper<Suggestion> {
}
