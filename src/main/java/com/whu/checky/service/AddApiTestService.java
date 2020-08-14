package com.whu.checky.service;

import com.whu.checky.domain.AddApiTest;
import com.whu.checky.domain.AddApiTestAux;

public interface AddApiTestService {
    int addAddApiTest(AddApiTest addApiTest);

    AddApiTest selectAddApiTestById(String id);  // for admin

    AddApiTestAux selectAddApiTestSafelyById(String id);  // for user
}
