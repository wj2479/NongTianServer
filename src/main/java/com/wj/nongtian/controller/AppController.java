package com.wj.nongtian.controller;

import com.wj.nongtian.ResultCode;
import com.wj.nongtian.entity.AppUpdate;
import com.wj.nongtian.service.AppService;
import com.wj.nongtian.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * App管理相关请求
 */
@RestController
@RequestMapping("/app")
public class AppController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private AppService appService;

    @RequestMapping(value = "/getUpdateInfo", method = RequestMethod.GET)
    public String getUpdateInfo() {
        AppUpdate appUpdate = appService.getUpdateInfo();

        if (appUpdate != null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, appUpdate);
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED);
        }
    }

}
