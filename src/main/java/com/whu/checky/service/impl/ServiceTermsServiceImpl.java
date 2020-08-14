package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.ServiceTerms;
import com.whu.checky.mapper.ServiceTermsMapper;
import com.whu.checky.service.ServiceTermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ServiceTermsService")
public class ServiceTermsServiceImpl implements ServiceTermsService {
    @Autowired
    private ServiceTermsMapper serviceTermsMapper;

    @Override
    public ServiceTerms getLatestServiceTerms() {
        return serviceTermsMapper.getLatestServiceTerms();
    }

    @Override
    public int addServiceTerms(ServiceTerms serviceTerms) {
        int maxIdNow = serviceTermsMapper.getMaxServiceId();
        serviceTerms.setServiceId(maxIdNow+1);
        return serviceTermsMapper.insert(serviceTerms);
    }
}
