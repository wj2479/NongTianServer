package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.Project;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ProjectMapper {

    /**
     * 判断项目是不是存在
     *
     * @param pid
     * @return
     */
    boolean isProjectExists(@Param("pid") int pid);

    /**
     * 根据用户的ID获取关联的项目ID
     *
     * @param uid
     * @return
     */
    List<String> getProjectIdByUserId(@Param("uid") int uid);

    /**
     * 根据项目的ID 获取所属用户ID
     *
     * @param pid
     * @return
     */
    @Select("select uid from project_user where pid = #{pid}")
    List<String> getUserIdByProjectId(@Param("pid") int pid);

    /**
     * 根据项目的ID 获取项目信息
     *
     * @param pid
     * @return
     */
    Project getProjectById(@Param("pid") int pid);

    /**
     * 根据上级ID获取下级项目列表
     *
     * @param pid
     * @return
     */
    List<Project> getSubProjectsByParentId(@Param("pid") int pid);

    /**
     * 根据项目 获取同级项目的排名
     *
     * @param pid
     * @param parentId
     * @param level
     * @return
     */
    int getProjectAreaRank(@Param("pid") int pid, @Param("parentId") int parentId, @Param("level") int level);

    /**
     * 获取当前同级项目 排名第一的项目
     *
     * @param parentId
     * @param level
     * @return
     */
    List<Project> getTop1ProjectArea(@Param("parentId") int parentId, @Param("level") int level);

    /**
     * 获取项目的月度计划
     *
     * @param pid
     * @param year
     * @param month
     */
    int getProjectPlanMonth(@Param("pid") int pid, @Param("year") int year, @Param("month") int month);

    /**
     * 插入项目的月度计划
     *
     * @param pid
     * @param uid
     * @param year
     * @param month
     * @param target
     * @return
     */
    int saveProjectPlanMonth(@Param("pid") int pid, @Param("uid") int uid, @Param("year") int year, @Param("month") int month, @Param("target") int target);

    @Update("update project_info set process = #{schedule} , updatetime = now()  where id= #{pid}")
    int updateProjectSchedule(@Param("pid") int pid, @Param("schedule") int schedule);

    /**
     * 根据地区ID 获取本地区的项目列表
     *
     * @param areaId
     * @return
     */
    List<Project> getProjectsByAreaId(@Param("areaId") int areaId);
}
