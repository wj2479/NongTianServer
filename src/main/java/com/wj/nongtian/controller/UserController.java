package com.wj.nongtian.controller;

import com.alibaba.druid.util.StringUtils;
import com.wj.nongtian.ResultCode;
import com.wj.nongtian.entity.Area;
import com.wj.nongtian.entity.User;
import com.wj.nongtian.entity.UserAreaTreeNode;
import com.wj.nongtian.service.AreaService;
import com.wj.nongtian.service.UserService;
import com.wj.nongtian.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户账号相关请求
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_USERNAME_EMPTY);
        }

        if (StringUtils.isEmpty(password)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PASSWORD_EMPTY);
        }

        boolean isExist = userService.isUserExist(username);
        if (!isExist) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_USERNAME_NOT_FOUND);
        }

        logger.info("接收到登录参数:" + username + "  " + password);

        User user = userService.login(username, password);

        String result = "";
        if (user != null) {
            logger.info("查询到登录结果:" + user.toString());
            areaService.initParentAreas(user.getArea());

            result = JsonUtils.getJsonResult(ResultCode.RESULT_OK, "登录成功", user);
        } else {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_LOGIN_FAILED);
        }
        return result;
    }


    @RequestMapping(value = "/getSubAreaUser", method = RequestMethod.GET)
    public String getSubAreaUser(Integer uid) {
        if (uid == null || !userService.isUserExist(uid)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        try {
            User user = userService.getUser(uid);
            List<Area> areaList = areaService.getSubAreaById(user.getArea().getId());

            List<UserAreaTreeNode> treeNodes = new ArrayList<>();
            for (Area area : areaList) {
                UserAreaTreeNode treeNode = new UserAreaTreeNode();
                treeNode.setArea(area);
                fillAreaUser(treeNode);
                treeNodes.add(treeNode);
            }
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, treeNodes);
        } catch (Exception e) {

        }
        return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "获取失败");
    }

    /**
     * 递归的填充下级用户
     *
     * @param treeNode
     */
    private void fillAreaUser(UserAreaTreeNode treeNode) {
        List<User> userList = userService.getUsersByAreaId(treeNode.getArea().getId());
        // 如果这个区域还有下级区域
        if (treeNode.getArea().hasChild()) {
            treeNode.setUserInfos(userList);

            // 获取下级区域列表
            List<Area> areaList = areaService.getSubAreaById(treeNode.getArea().getId());
            List<UserAreaTreeNode> treeNodes = new ArrayList<>();

            for (Area area : areaList) {
                UserAreaTreeNode node = new UserAreaTreeNode();
                node.setArea(area);
                fillAreaUser(node);
                treeNodes.add(node);
            }
            treeNode.setChilds(treeNodes);
        } else {
            List<UserAreaTreeNode> treeNodes = new ArrayList<>();
            UserAreaTreeNode node = new UserAreaTreeNode();
            node.setUserInfos(userList);
            treeNodes.add(node);
            treeNode.setChilds(treeNodes);
        }

    }


}
