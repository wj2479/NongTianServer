package com.wj.nongtian.service;

import com.wj.nongtian.entity.Project;

import java.util.List;

public interface ProjectService {

    boolean isProjectExist(int pid);

    int getProjectIdByUserId(int uid);

    Project getProjectById(int pid);

    List<Project> getSubProjectsByParentId(int pid);

    int getProjectRank(int pid);

    List<Project> getTop1ProjectInArea(int pid);

    int getProjectPlanMonth(int pid);

    boolean setProjectPlanThisMonth(int pid, int uid, int target);
}
