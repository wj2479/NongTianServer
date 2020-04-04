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

    private final UserService userService;
    private final AreaService areaService;

    public UserController(UserService userService, AreaService areaService) {
        this.userService = userService;
        this.areaService = areaService;
    }

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

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String changePassword(String username, String oldPwd, String newPwd) {
        logger.info("修改密码请求:" + username + "  " + oldPwd + " " + newPwd);
        if (StringUtils.isEmpty(username)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_USERNAME_EMPTY);
        }
        // 判断新旧密码是不是一致
        if (oldPwd.equals(newPwd)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_LOGIN_FAILED, "新旧密码一致");
        }

        // 首先判断用户是不是存在
        boolean isExist = userService.isUserExist(username);

        if (!isExist) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_USERNAME_NOT_FOUND);
        }

        User user = userService.login(username, oldPwd);

        String result = "";
        if (user != null) {
            boolean isSuccess = userService.setPassword(username, newPwd);
            if (isSuccess) {
                result = JsonUtils.getJsonResult(ResultCode.RESULT_OK, "密码修改成功");
            } else {
                result = JsonUtils.getJsonResult(ResultCode.RESULT_LOGIN_FAILED, "密码修改失败");
            }

        } else {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_LOGIN_FAILED, "旧密码错误");
        }

        logger.info("修改密码结果:" + result);
        return result;
    }


    @RequestMapping(value = "/updateInfo", method = RequestMethod.GET)
    public String updateInfo(Integer uid, String nickname, String phone, Integer age, Integer gender) {
        logger.info("更新用户信息请求:" + uid + "  " + nickname + " " + phone + " " + age + " " + gender);
        if (uid == null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_USERNAME_EMPTY);
        }

        if (StringUtils.isEmpty(nickname) && StringUtils.isEmpty(phone) && age == null && gender == null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED);
        }

        // 首先判断用户是不是存在
        boolean isExist = userService.isUserExist(uid);

        if (!isExist) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_USERNAME_NOT_FOUND);
        }

        String result = "";
        boolean isSuccess = userService.updateUserInfo(uid, nickname, phone, age, gender);
        if (isSuccess) {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_OK, "修改成功");
        } else {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_LOGIN_FAILED, "修改失败");
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

    @RequestMapping(value = "/getUserById", method = RequestMethod.GET)
    public String getUserById(Integer uid) {
        if (uid == null || !userService.isUserExist(uid)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        try {
            User user = userService.getUser(uid);

            if (user != null) {
                logger.info("查询到用户:" + user.toString());
                areaService.initParentAreas(user.getArea());

                return JsonUtils.getJsonResult(ResultCode.RESULT_OK, user);
            } else {
                return JsonUtils.getJsonResult(ResultCode.RESULT_LOGIN_FAILED);
            }
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
