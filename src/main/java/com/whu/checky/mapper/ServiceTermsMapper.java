package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.ServiceTerms;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "ServiceTermsMapper")
public interface ServiceTermsMapper extends BaseMapper<ServiceTerms> {
    @Select("SELECT service_id AS serviceId, service_content AS serviceContent, service_time AS serviceTime\n" +
            "FROM service WHERE service_id = (SELECT MAX(service_id) FROM service)")
    ServiceTerms getLatestServiceTerms();

    @Select("SELECT MAX(service_id) FROM service")
    int getMaxServiceId();
}
