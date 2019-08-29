package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Suggestion;
import com.whu.checky.mapper.SuggestionMapper;
import com.whu.checky.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("suggestionService")
public class SuggestionServiceImpl implements SuggestionService {
    @Autowired
    private SuggestionMapper suggestionMapper;
    @Override
    public Integer addSuggestion(Suggestion suggestion) {
        return suggestionMapper.insert(suggestion);
    }

    @Override
    public Integer updataSuggestion(String suggestionid,String finalState) {
        return suggestionMapper.updateState(suggestionid,finalState);
    }

    @Override
    public Suggestion QuerySuggestion(String suggestionId) {
        return suggestionMapper.selectById(suggestionId);
    }

    @Override
    public List<Suggestion> ListSuggestion() {
        return suggestionMapper.selectList(new EntityWrapper<Suggestion>());
    }
    @Override
    public List<Suggestion> displaySuggestions(Page<Suggestion> page) {
        return suggestionMapper.selectPage(
                page,
                new EntityWrapper<Suggestion>().orderBy("suggestion_time",true));
    }

    @Override
    public int deleteSuggestion(String suggestionId) {
        return suggestionMapper.deleteById(suggestionId);
    }
}
