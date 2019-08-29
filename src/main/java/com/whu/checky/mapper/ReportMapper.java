package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "reportMapper")
public interface ReportMapper extends BaseMapper<Report> {
    List<Report> getReportList();
    int updateState(@Param("reportId") String reportId, @Param("result") String result);

}
