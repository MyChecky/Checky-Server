package com.whu.checky.service;

import com.whu.checky.domain.Medal;

import java.util.List;

public interface MedalService {

    List<Medal> getMedalListByUserId(String userId);

    int insertAreaSpecialMedal(String medalNameAux);
}
