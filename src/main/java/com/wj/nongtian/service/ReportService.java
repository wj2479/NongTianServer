package com.wj.nongtian.service;


import com.wj.nongtian.entity.DaySchedule;
import com.wj.nongtian.entity.ProjectDailyReport;
import com.wj.nongtian.entity.ReportComment;
import com.wj.nongtian.entity.ReportMedia;

import java.util.List;

public interface ReportService {

    List<ProjectDailyReport> getDailyReports(int pid);

    /**
     * 获取指定日期上传的日报列表
     *
     * @param pid
     * @param date
     * @return
     */
    List<ProjectDailyReport> getDailyReports(int pid, String date);

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

    /**
     * 获取项目的不合格记录
     *
     * @param ids id的列表  使用,分割
     */
    List<ProjectDailyReport> getUnqualifiedReports(List<Integer> ids);

    /**
     * 获取项目的日报记录
     *
     * @param ids id的列表  使用,分割
     */
    List<ProjectDailyReport> getReportsByIds(List<Integer> ids);

    /**
     * 日报是不是被该用户关注
     *
     * @param uid
     * @param rid
     * @return
     */
    boolean isReportFocused(int uid, int rid);

    /**
     * 取消/关注 日志
     *
     * @param uid
     * @param rid
     * @param isFocus
     * @return
     */
    int focusReport(int uid, int rid, boolean isFocus);

    /**
     * 获取用户关注的日报ID列表
     *
     * @param uid
     * @return
     */
    List<Integer> getFocusReportIds(int uid);

    /**
     * 添加日报评论
     *
     * @return
     */
    int addReportComment(ReportComment reportComment);

    /**
     * 添加评论的媒体文件
     *
     * @param medias
     * @return
     */
    int addCommentMedias(ReportMedia medias);

    /**
     * 获取日报的评论数据
     *
     * @param rid
     * @return
     */
    List<ReportComment> getReportComments(int rid);

    /**
     * 获取日报的图片
     *
     * @param id
     * @return
     */
    List<ReportMedia> getCommentMedias(int id);
}
