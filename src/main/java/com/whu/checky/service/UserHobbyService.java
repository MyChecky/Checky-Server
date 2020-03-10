package com.whu.checky.service;

import java.util.List;

import com.whu.checky.domain.UserHobby;

public interface UserHobbyService {
    List<UserHobby> getUserHobbies(String userId);
    void addUserHobby(UserHobby userHobby);
    void delUserHobby(UserHobby userHobby);
}
