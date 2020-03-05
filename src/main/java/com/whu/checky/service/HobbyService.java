package com.whu.checky.service;

import com.whu.checky.domain.Hobby;

import java.util.List;

public interface HobbyService {
    List<String> getUserHobbies(String userId, int num);

    List<String> getHobbies(String userId, int num);

    void updateUserHobbies(String userId, List<String> hobbies);
}
