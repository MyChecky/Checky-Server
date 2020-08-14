package com.whu.checky.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Appeal;
import com.whu.checky.domain.Report;
import com.whu.checky.domain.User;
import com.whu.checky.mapper.ReportMapper;
import com.whu.checky.mapper.UserMapper;
import com.whu.checky.service.ReportService;
import com.whu.checky.util.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserMapper userMapper;

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
        return reportMapper.selectList(new EntityWrapper<Report>().eq("user_id", userId));
    }

    @Override
    public List<Report> displayReports(Page<Report> page) {
        return reportMapper.selectPage(
                page,
                new EntityWrapper<Report>().orderBy("report_time", true));
    }

    @Override
    public Report getReportById(String reportId) {
        return reportMapper.selectById(reportId);
    }

    @Override
    public Integer updateReport(Report report) {
        return reportMapper.updateById(report);
    }

    @Override
    public List<Report> getSameReportsByReport(Report report) {
        switch (report.getReportType()) {
            case MyConstants.REPORT_TYPE_ESSAY:
                return reportMapper.selectList(new EntityWrapper<Report>()
                        .eq("report_type", report.getReportType())
                        .eq("essay_id", report.getEssayId()));
            case MyConstants.REPORT_TYPE_CHECK:
                return reportMapper.selectList(new EntityWrapper<Report>()
                        .eq("report_type", report.getReportType())
                        .eq("check_id", report.getCheckId()));
            case MyConstants.REPORT_TYPE_SUPERVISOR:
                return reportMapper.selectList(new EntityWrapper<Report>()
                        .eq("report_type", report.getReportType())
                        .eq("supervisor_id", report.getSupervisorId())
                        .eq("task_id", report.getTaskId())
                        .orderBy("report_time", true)); // 第一个是最早举报的
            case MyConstants.REPORT_TYPE_TASK:
                return reportMapper.selectList(new EntityWrapper<Report>()
                        .eq("report_type", report.getReportType())
                        .eq("task_id", report.getTaskId()));
        }
        return null;
    }

    @Override
    public List<Report> queryReportByUserName(String username) {
        return reportMapper.queryReportByUserName(username);
    }

    @Override
    public List<Report> queryAppealsAll(Page<Report> p, String startTime, String endTime) {
        return reportMapper.selectPage(p, new EntityWrapper<Report>()
                .ge("report_time", startTime)
                .le("report_time", endTime)
                .orderBy("report_time", false));
    }

    @Override
    public List<Report> queryAppealsLikeNickname(Page<Report> p, String startTime, String endTime, String keyword) {
        List<User> users = userMapper.selectList(new EntityWrapper<User>().like("user_name", keyword));
        List<String> userIds = new ArrayList<>();
        for (User user : users) {
            userIds.add(user.getUserId());
        }

        List<Report> reports = reportMapper.selectPage(p, new EntityWrapper<Report>()
                .in("user_id", userIds)
                .ge("report_time", startTime)
                .le("report_time", endTime)
                .orderBy("report_time", false));
        return reports;
    }

    @Override
    public List<Report> queryAppealsLikeContent(Page<Report> p, String startTime, String endTime, String keyword) {
        return reportMapper.selectPage(p, new EntityWrapper<Report>()
                .like("report_content", keyword)
                .ge("report_time", startTime)
                .le("report_time", endTime)
                .orderBy("report_time", false));
    }

    @Override
    public List<Report> getAllReports() {
        return reportMapper.selectList(new EntityWrapper<>());
    }
}
