package com.whu.checky.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Report;

import java.util.List;

public interface ReportService {
    //添加举报
    int addReport(Report report);
    //删除举报
    int deleteReport(String reportId);
    //对举报进行处理举报
    int dealReport(Report report);
    //查询举报
    List<Report> queryUserReports(String userId);
    //查看具体的某个举报
    Report queryReportById(String reportId);
    //管理员查看所有申诉
    List<Report> displayReports(Page<Report> page);

    //对举报进行处理举报
    int updateState(String reportId,String result);

    List<Report> queryReportByUserName(String username);

    List<Report> queryAppealsAll(Page<Report> p, String startTime, String endTime);

    List<Report> queryAppealsLikeNickname(Page<Report> p, String startTime, String endTime, String keyword);

    List<Report> queryAppealsLikeContent(Page<Report> p, String startTime, String endTime, String keyword);
}
