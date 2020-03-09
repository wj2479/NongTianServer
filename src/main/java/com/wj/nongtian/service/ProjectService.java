package com.wj.nongtian.service;

import com.wj.nongtian.entity.Project;

import java.util.List;

public interface ProjectService {

    boolean isProjectExist(int pid);

    int getProjectIdByUserId(int uid);

    int getUserIdByProjectId(int pid);

    Project getProjectById(int pid);

    List<Project> getSubProjectsByParentId(int pid);

    int getProjectRank(int pid);

    List<Project> getTop1ProjectInArea(int pid);

    int getProjectPlanMonth(int pid);

    boolean setProjectPlanThisMonth(int pid, int uid, int target);

    int updateProjectSchedule(int pid,int schedule);
}
