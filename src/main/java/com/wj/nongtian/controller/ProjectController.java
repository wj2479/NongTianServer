package com.wj.nongtian.controller;

import com.wj.nongtian.ResultCode;
import com.wj.nongtian.entity.Project;
import com.wj.nongtian.service.AreaService;
import com.wj.nongtian.service.ProjectService;
import com.wj.nongtian.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 工程相关请求
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ProjectService projectService;
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/getProjectByUserId", method = RequestMethod.GET)
    public String getProjectByUserId(Integer uid) {
        if (uid == null || uid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        int pid = projectService.getProjectIdByUserId(uid);
        if (pid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PROJECT_NOT_FOUND);
        }
        return getProjectById(pid);
    }

    @RequestMapping(value = "/getProjectById", method = RequestMethod.GET)
    public String getProjectById(Integer pid) {
        if (pid == null || pid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        Project project = projectService.getProjectById(pid);
        areaService.initParentAreas(project.getArea());
        if (project == null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PROJECT_NOT_FOUND);
        }
        logger.info("工程信息:" + project.toString());
        return JsonUtils.getJsonResult(ResultCode.RESULT_OK, project);
    }

    @RequestMapping(value = "/getSubProjectsByParentId", method = RequestMethod.GET)
    public String getSubProjectsByParentId(Integer pid) {
        if (pid == null || pid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        List<Project> projects = projectService.getSubProjectsByParentId(pid);
        if (projects != null && projects.size() > 0) {
            projects.forEach(proj -> areaService.initParentAreas(proj.getArea()));
        }
        return JsonUtils.getJsonResult(ResultCode.RESULT_OK, projects);
    }

    @RequestMapping(value = "/getProjectRank", method = RequestMethod.GET)
    public String getProjectRank(Integer pid) {
        if (pid == null || pid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        int rank = projectService.getProjectRank(pid);
        return JsonUtils.getJsonResult(ResultCode.RESULT_OK, rank);
    }

    @RequestMapping(value = "/getTop1ProjectInArea", method = RequestMethod.GET)
    public String getTop1ProjectInArea(Integer pid) {
        if (pid == null || pid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        List<Project> list = projectService.getTop1ProjectInArea(pid);
        return JsonUtils.getJsonResult(ResultCode.RESULT_OK, list);
    }

    @RequestMapping(value = "/getProjectPlanMonth", method = RequestMethod.GET)
    public String getProjectPlanMonth(Integer pid) {
        if (pid == null || pid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        int plan = projectService.getProjectPlanMonth(pid);
        return JsonUtils.getJsonResult(ResultCode.RESULT_OK, plan);
    }

    @RequestMapping(value = "/setProjectPlanThisMonth", method = RequestMethod.GET)
    public String setProjectPlanThisMonth(Integer pid, Integer uid, Integer target) {
        if (pid == null || pid < 0 || uid == null || uid < 0 || target == null || target < 0 || target > 100) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        int plan = projectService.getProjectPlanMonth(pid);
        if (plan > 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "本月已设置");
        }

        boolean success = projectService.setProjectPlanThisMonth(pid, uid, target);
        if (success) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "设置成功");
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "设置失败");
        }
    }

}
