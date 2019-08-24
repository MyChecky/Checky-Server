package com.whu.checky.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Report;
import com.whu.checky.mapper.ReportMapper;
import com.whu.checky.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;


    @Override
    public int addReport(Report report) {
        return reportMapper.insert(report);
    }

    @Override
    public int deleteReport(String reportId) {
        return reportMapper.deleteById(reportId);
    }

    @Override
    public int dealReport(Report report) {
        return reportMapper.updateById(report);
    }

    @Override
    public List<Report> queryUserReports(String userId) {
        return reportMapper.selectList(new EntityWrapper<Report>().eq("user_id",userId));
    }

    @Override
    public Report queryReportById(String reportId) {
        return reportMapper.selectById(reportId);
    }

    @Override
    public List<Report> displayReports(Page<Report> page) {
        return reportMapper.selectPage(
                page,
                new EntityWrapper<Report>().orderBy("report_time",true));
    }
}
