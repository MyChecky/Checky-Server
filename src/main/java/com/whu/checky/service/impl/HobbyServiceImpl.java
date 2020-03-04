package com.whu.checky.service.impl;

import com.whu.checky.domain.UserHobby;
import com.whu.checky.mapper.HobbyMapper;
import com.whu.checky.mapper.UserHobbyMapper;
import com.whu.checky.service.HobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("HobbyService")
public class HobbyServiceImpl implements HobbyService {
    @Autowired
    private HobbyMapper hobbyMapper;

    @Autowired
    private UserHobbyMapper userHobbyMapper;





}
