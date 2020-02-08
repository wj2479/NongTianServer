package com.wj.nongtian.controller;

import com.alibaba.druid.util.StringUtils;
import com.wj.nongtian.ResultCode;
import com.wj.nongtian.entity.User;
import com.wj.nongtian.service.AreaService;
import com.wj.nongtian.service.UserService;
import com.wj.nongtian.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

        logger.info("查询到登录结果:" + user.toString());
        areaService.initParentAreas(user.getArea());

        String result = "";
        if (user != null) {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_OK, "登录成功", user);
        } else {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_LOGIN_FAILED);
        }
        return result;
    }


}
