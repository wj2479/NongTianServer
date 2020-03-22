package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.DaySchedule;
import com.wj.nongtian.entity.ProjectDailyReport;
import com.wj.nongtian.entity.ReportComment;
import com.wj.nongtian.entity.ReportMedia;
import org.apache.ibatis.annotations.*;

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

    @Select("select max(schedule) schedule, date ,max(createtime) updateTime\n" +
            "from (select project_day_report.*,date_format(IFNULL(updatetime,createtime),'%Y-%m-%d') as date \n" +
            "\tfrom project_day_report\n" +
            "\twhere pid = #{pid}) p\n" +
            "\twhere date is not NULL\n" +
            "GROUP BY date \n" +
            "order by date desc")
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

    @Select("select * from \n" +
            "(\n" +
            " select project_day_report.*,date_format(IFNULL(updatetime,createtime),'%Y-%m-%d') as date , IFNULL(updatetime,createtime) time\n" +
            " from project_day_report\n" +
            " where pid = #{pid} \n" +
            ") p \n" +
            "where date = #{date}  \n" +
            "order by time ")
    List<ProjectDailyReport> getDailyReportByProjectIdAndDate(@Param("pid") int pid, @Param("date") String date);

    /**
     * 获取项目的不合格记录
     *
     * @param ids id的列表  使用,分割
     */
    List<ProjectDailyReport> getUnqualifiedReports(@Param("ids") List<Integer> ids);

    /**
     * 获取项目的日报记录
     *
     * @param ids id的列表  使用,分割
     */
    List<ProjectDailyReport> getReportsByIds(@Param("ids") List<Integer> ids, @Param("check") int check);

    @Select("select count(*) from user_focus_report where uid = #{uid} and rid = #{rid}")
    boolean isReportFocused(@Param("uid") int uid, @Param("rid") int rid);

    /**
     * 删除关注日报
     *
     * @param uid
     * @param rid
     * @return
     */

    @Delete("delete from user_focus_report where uid = #{uid} and rid = #{rid} ")
    int deleteFocusReport(@Param("uid") int uid, @Param("rid") int rid);

    /**
     * 添加关注日报
     *
     * @param uid
     * @param rid
     * @return
     */
    @Insert("insert into user_focus_report(uid,rid,createtime) values (#{uid},#{rid},now())")
    int addFocusReport(@Param("uid") int uid, @Param("rid") int rid);

    /**
     * 查询用户关注的日报IDS
     *
     * @param uid
     * @return
     */
    @Select("SELECT DISTINCT rid FROM user_focus_report WHERE uid = #{uid}")
    List<Integer> getFocusReportIds(@Param("uid") int uid);

    /**
     * 插入日报评论
     *
     * @param reportComment
     * @return
     */
    @Insert("insert into report_comment_content(rid,sender,content,createtime) values(#{rid},#{sender},#{content},now())")
    //加入该注解可以保持对象后，查看对象插入id
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addReportComment(ReportComment reportComment);

    @Insert("insert into report_comment_pics(cid,rootpath,path,`name`,md5,type,createtime) values(#{rid},#{rootPath},#{path},#{name},#{md5},#{type},now())")
    //加入该注解可以保持对象后，查看对象插入id
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addCommentMedias(ReportMedia medias);

    @Select("select * from report_comment_content where rid = #{rid} order by createtime desc ")
    List<ReportComment> getReportComments(@Param("rid") int rid);

    @Select("select * from report_comment_pics where cid=#{cid}")
    List<ReportMedia> getCommentMedias(@Param("cid") int id);
}
