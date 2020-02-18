package com.whu.checky.service.impl;

import com.whu.checky.domain.Parameter;
import com.whu.checky.mapper.ParameterMapper;
import com.whu.checky.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("/ParameterService")
public class ParameterServiceImpl implements ParameterService {
    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    public Parameter getValueByParam(String paramName) {
        return parameterMapper.getValueByParam(paramName);
    }
}
