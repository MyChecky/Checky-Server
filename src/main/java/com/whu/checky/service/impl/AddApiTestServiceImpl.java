package com.whu.checky.service.impl;

import com.whu.checky.domain.AddApiTest;
import com.whu.checky.domain.AddApiTestAux;
import com.whu.checky.mapper.AddApiTestMapper;
import com.whu.checky.service.AddApiTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("addApiTestService")
public class AddApiTestServiceImpl implements AddApiTestService {
    @Autowired
    private AddApiTestMapper addApiTestMapper;

    @Override
    public int addAddApiTest(AddApiTest addApiTest) {
        return addApiTestMapper.insert(addApiTest);  // mapper 提供
    }

    @Override
    public AddApiTest selectAddApiTestById(String id) {
        return addApiTestMapper.selectById(id);  // mapper 提供
    }

    @Override
    public AddApiTestAux selectAddApiTestSafelyById(String id) {
        return addApiTestMapper.selectSafelyById(id);  // 自己写的
    }
}
