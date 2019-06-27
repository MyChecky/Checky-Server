package com.whu.checky.service;

import com.whu.checky.domain.Suggestion;
import com.whu.checky.domain.TaskType;

import java.util.List;

public interface SuggestionService {
    //新加一条打卡建议
    Integer addSuggestion(Suggestion suggestion);
    //对一条打卡建议进行处理
    Integer updataSuggestion(String suggestionid,String finalState);
    //对打卡建议进行查询
    Suggestion QuerySuggestion(String suggestionId);
    //查询所有的建议
    List<Suggestion> ListSuggestion();
}
