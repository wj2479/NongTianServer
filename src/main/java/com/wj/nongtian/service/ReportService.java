package com.wj.nongtian.service;


import com.wj.nongtian.entity.DaySchedule;
import com.wj.nongtian.entity.ProjectDailyReport;
import com.wj.nongtian.entity.ReportMedia;

import java.util.List;

public interface ReportService {

    List<ProjectDailyReport> getDailyReports(int pid);

    /**
     * 获取每日的项目进度汇总
     *
     * @param pid
     * @return
     */
    List<DaySchedule> getDailySchedule(int pid);

    /**
     * 获取项目某一天的照片汇总
     *
     * @param pid
     * @param date
     * @return
     */
    List<ReportMedia> getDailyMedias(int pid, String date);

    int addDailyReport(ProjectDailyReport dailyReport);

    int addReportMedias(ReportMedia medias);

    List<ReportMedia> getReportMedias(int rid);
}
