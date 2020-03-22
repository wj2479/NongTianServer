package com.wj.nongtian.controller;

import com.alibaba.druid.util.StringUtils;
import com.wj.nongtian.ResultCode;
import com.wj.nongtian.entity.Notify;
import com.wj.nongtian.entity.NotifyReceiver;
import com.wj.nongtian.service.NotifyService;
import com.wj.nongtian.service.UserService;
import com.wj.nongtian.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通知相关请求
 */
@RestController
@RequestMapping("/notify")
public class NotifyController {

    private Logger logger = Logger.getLogger(getClass());

    private final NotifyService notifyService;
    private final UserService userService;

    public NotifyController(NotifyService notifyService, UserService userService) {
        this.notifyService = notifyService;
        this.userService = userService;
    }

    @RequestMapping(value = "/getPublishNotify", method = RequestMethod.GET)
    public String getPublishNotify(Integer uid) {
        if (uid == null || !userService.isUserExist(uid)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        List<Notify> notifyList = notifyService.getPublishNotify(uid);
        if (notifyList != null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, notifyList);
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED);
        }
    }

    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public String publish(Notify notify) {
        if (StringUtils.isEmpty(notify.getContent()) || !userService.isUserExist(notify.getUid())) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        boolean isSuccess = notifyService.publishNotify(notify);
        if (isSuccess) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "发布成功");
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "发布失败");
        }
    }

    @RequestMapping(value = "/getReceivedNotify", method = RequestMethod.GET)
    public String getReceivedNotify(Integer uid) {
        if (uid == null || !userService.isUserExist(uid)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        List<NotifyReceiver> notifyList = notifyService.getReceivedNotify(uid);
        if (notifyList != null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, notifyList);
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED);
        }
    }

    @RequestMapping(value = "/setNotifyRead", method = RequestMethod.GET)
    public String setNotifyRead(Integer uid, Integer nid) {
        if (uid == null || !userService.isUserExist(uid) || nid == null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        boolean isSuccess = notifyService.setNotifyRead(uid, nid);
        if (isSuccess) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "设置成功");
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "设置失败");
        }
    }

}
