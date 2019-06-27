package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Suggestion;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "suggestionMapper")
public interface SuggestionMapper extends BaseMapper<Suggestion> {
    Integer updateState(String suggestionid,String finalState);
}
