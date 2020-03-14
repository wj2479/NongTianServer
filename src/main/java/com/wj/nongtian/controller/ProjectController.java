package com.wj.nongtian.controller;

import com.wj.nongtian.ResultCode;
import com.wj.nongtian.entity.Project;
import com.wj.nongtian.entity.Role;
import com.wj.nongtian.entity.User;
import com.wj.nongtian.service.AreaService;
import com.wj.nongtian.service.ProjectService;
import com.wj.nongtian.service.UserService;
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
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getProjectByUserId", method = RequestMethod.GET)
    public String getProjectByUserId(Integer uid) {
        if (uid == null || uid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        User user = userService.getUser(uid);

        logger.info("查询的用户信息:" + user.toString());

        if (user != null) {
            // 如果是监理用户  就获取监理对应的项目
            if (user.getRole().getCode() == Role.CODE.SUPERVISOR.getValue()) {
                int pid = projectService.getProjectIdByUserId(uid);
                if (pid < 0) {
                    return JsonUtils.getJsonResult(ResultCode.RESULT_PROJECT_NOT_FOUND);
                }
                return getProjectById(pid);
            } else {
                // 如果是管理员  就查询所管辖的项目

                List<Project> projects = projectService.getProjectsByAreaId(user.getArea().getId());
                if (projects != null && projects.size() > 0) {
                    return JsonUtils.getJsonResult(ResultCode.RESULT_OK, projects.get(0));
                }

                return JsonUtils.getJsonResult(ResultCode.RESULT_PROJECT_NOT_FOUND);
            }
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "没有找到该用户");
        }
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
            projects.forEach(proj -> {
                        areaService.initParentAreas(proj.getArea());
                        int userId = projectService.getUserIdByProjectId(proj.getId());
                        if (userId != -1) {
                            User user = userService.getUser(userId);
                            proj.setManager(user);
                        }
                    }
            );
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
