package com.wj.nongtian.service;


import com.wj.nongtian.entity.ProjectDailyReport;
import com.wj.nongtian.entity.ReportMedia;

import java.util.List;

public interface ReportService {

    List<ProjectDailyReport> getDailyReports(int pid);

    int addDailyReport(ProjectDailyReport dailyReport);

    int addReportMedias(ReportMedia medias);

    List<ReportMedia> getReportMedias(int rid);
}
