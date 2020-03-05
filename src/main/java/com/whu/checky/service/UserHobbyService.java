package com.whu.checky.service;

import java.util.List;

import com.whu.checky.domain.UserHobby;

public interface UserHobbyService {
    public List<UserHobby> getUserHobbies(String userId);
}
