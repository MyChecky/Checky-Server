package com.whu.checky.domain;

/**
 * 生成type的建议
 */
public class Suggestion {
    private String suggestionId;

    private String userId;
    /**
     * 建议发布时间
     */
    private String suggestionTime;

    private String suggestionContent;

    public String getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(String suggestionId) {
        this.suggestionId = suggestionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSuggestionTime() {
        return suggestionTime;
    }

    public void setSuggestionTime(String suggestionTime) {
        this.suggestionTime = suggestionTime;
    }

    public String getSuggestionContent() {
        return suggestionContent;
    }

    public void setSuggestionContent(String suggestionContent) {
        this.suggestionContent = suggestionContent;
    }
}
