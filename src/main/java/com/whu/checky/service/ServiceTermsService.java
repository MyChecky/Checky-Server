package com.whu.checky.service;

import com.whu.checky.domain.ServiceTerms;

public interface ServiceTermsService {
    ServiceTerms getLatestServiceTerms();

    int addServiceTerms(ServiceTerms serviceTerms);
}
