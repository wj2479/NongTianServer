package com.wj.nongtian.service.impl;

import com.wj.nongtian.entity.Project;
import com.wj.nongtian.mapper.ProjectMapper;
import com.wj.nongtian.service.ProjectService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

@Transactional
@Service
public class ProjectServiceImpl implements ProjectService {

    private Logger logger = Logger.getLogger(getClass());

    @Resource
    private ProjectMapper projectMapper;

    Calendar calendar = Calendar.getInstance();

    @Override
    public boolean isProjectExist(int pid) {
        return projectMapper.isProjectExists(pid);
    }

    @Override
    public int getProjectIdByUserId(int uid) {
        int result = -1;
        List<String> list = projectMapper.getProjectIdByUserId(uid);
        if (list.size() > 0) {
            try {
                result = Integer.parseInt(list.get(0));
            } catch (Exception e) {

            }
        }
        return result;
    }

    @Override
    public int getUserIdByProjectId(int pid) {
        int result = -1;
        List<String> list = projectMapper.getUserIdByProjectId(pid);
        if (list.size() > 0) {
            try {
                result = Integer.parseInt(list.get(0));
            } catch (Exception e) {

            }
        }
        return result;
    }

    @Override
    public Project getProjectById(int pid) {
        return projectMapper.getProjectById(pid);
    }

    @Override
    public List<Project> getSubProjectsByParentId(int pid) {
        return projectMapper.getSubProjectsByParentId(pid);
    }

    @Override
    public List<Project> getProjectsByAreaId(int areaId) {
        return projectMapper.getProjectsByAreaId(areaId);
    }

    @Override
    public int getProjectRank(int pid) {
        Project project = getProjectById(pid);
        if (project != null) {
            int rank = projectMapper.getProjectAreaRank(pid, project.getParentId(), project.getLevel());
            return rank;
        }
        return -1;
    }

    @Override
    public List<Project> getTop1ProjectInArea(int pid) {
        Project project = getProjectById(pid);
        if (project != null) {
            List<Project> topList = projectMapper.getTop1ProjectArea(project.getParentId(), project.getLevel());
            return topList;
        }
        return null;
    }

    @Override
    public int getProjectPlanMonth(int pid) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int plan = -1;
        try {
            plan = projectMapper.getProjectPlanMonth(pid, year, month);
        } catch (Exception e) {

        }
        return plan;
    }

    @Override
    public boolean setProjectPlanThisMonth(int pid, int uid, int target) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        try {
            int count = projectMapper.saveProjectPlanMonth(pid, uid, year, month, target);
            if (count == 1)
                return true;
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public int updateProjectSchedule(int pid, int schedule) {
        Project project = projectMapper.getProjectById(pid);
        if (project != null) {
            int num = projectMapper.updateProjectSchedule(pid, schedule);
            logger.info("更新项目进度:" + pid + "-->" + schedule);

            if (project.getParentId() > 0) {
                List<Project> projects = projectMapper.getSubProjectsByParentId(project.getParentId());
                int avgSchedule = 0;

                if (projects != null && projects.size() > 0) {
                    int sum = 0;
                    for (Project proj : projects) {
                        sum += proj.getProcess();
                    }
                    avgSchedule = sum / projects.size();
                    return updateProjectSchedule(project.getParentId(), avgSchedule);
                }
            }
            return 1;
        }
        return -1;
    }
}
