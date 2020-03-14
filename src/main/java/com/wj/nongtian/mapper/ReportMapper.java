package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.DaySchedule;
import com.wj.nongtian.entity.ProjectDailyReport;
import com.wj.nongtian.entity.ReportMedia;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReportMapper {

    /**
     * 根据项目的ID 获取项目日报
     *
     * @param pid
     * @return
     */
    List<ProjectDailyReport> getDailyReportByProjectId(@Param("pid") int pid);

    /**
     * 添加日报记录
     *
     * @param dailyReport
     * @return
     */
    @Insert("insert into project_day_report(pid,uid,title,content,`check`,`schedule`,lnglat,address,createtime) values(#{pid},#{uid},#{title},#{content},#{check},#{schedule},#{lnglat},#{address},now())")
    //加入该注解可以保持对象后，查看对象插入id
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addDailyReport(ProjectDailyReport dailyReport);

    /**
     * 添加日报上传的音视频记录
     *
     * @param medias
     * @return
     */
    @Insert("insert into report_picture_video(rid,rootpath,path,`name`,md5,type,createtime) values(#{rid},#{rootPath},#{path},#{name},#{md5},#{type},now())")
    //加入该注解可以保持对象后，查看对象插入id
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addReportMedias(ReportMedia medias);

    /**
     * 获取日报的音视频记录
     *
     * @param rid
     * @return
     */
    @Select("select * from report_picture_video where rid=#{rid}")
    List<ReportMedia> getReportMedias(@Param("rid") int rid);

    @Select("select max(schedule) schedule, date ,IFNULL(updatetime,createtime) updateTime\n" +
            "from (select project_day_report.*,date_format(IFNULL(updatetime,createtime),'%Y-%m-%d') as date \n" +
            "\tfrom project_day_report\n" +
            "\twhere pid = #{pid}) p\n" +
            "GROUP BY date \n" +
            "order by date ")
    List<DaySchedule> getDailySchedule(@Param("pid") int pid);

    @Select("select * \n" +
            "from (\n" +
            "\tselect report_picture_video.*,date_format(report_picture_video.createtime,'%Y-%m-%d') as date \n" +
            "\tfrom project_day_report,report_picture_video\n" +
            "\twhere project_day_report.pid= #{pid} and project_day_report.id = report_picture_video.rid \n" +
            "\torder by createtime desc\n" +
            ") p \n" +
            "where p.date = #{date}")
    List<ReportMedia> getDailyMedias(@Param("pid") int pid, @Param("date") String date);
}
