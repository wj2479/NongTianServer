package com.wj.nongtian.service.impl;

import com.wj.nongtian.entity.DaySchedule;
import com.wj.nongtian.entity.ProjectDailyReport;
import com.wj.nongtian.entity.ReportMedia;
import com.wj.nongtian.mapper.ReportMapper;
import com.wj.nongtian.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Override
    public List<ProjectDailyReport> getDailyReports(int pid) {
        List<ProjectDailyReport> dailyReports = null;
        try {
            dailyReports = reportMapper.getDailyReportByProjectId(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dailyReports;
    }

    @Override
    public List<ProjectDailyReport> getDailyReports(int pid, String date) {
        List<ProjectDailyReport> dailyReports = null;
        try {
            dailyReports = reportMapper.getDailyReportByProjectIdAndDate(pid, date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dailyReports;
    }

    @Override
    public List<DaySchedule> getDailySchedule(int pid) {
        List<DaySchedule> daySchedules = reportMapper.getDailySchedule(pid);
        return daySchedules;
    }

    @Override
    public List<ReportMedia> getDailyMedias(int pid, String date) {
        return reportMapper.getDailyMedias(pid, date);
    }

    @Override
    public int addDailyReport(ProjectDailyReport dailyReport) {
        return reportMapper.addDailyReport(dailyReport);
    }

    @Override
    public int addReportMedias(ReportMedia medias) {
        return reportMapper.addReportMedias(medias);
    }

    @Override
    public List<ReportMedia> getReportMedias(int rid) {
        return reportMapper.getReportMedias(rid);
    }

}
