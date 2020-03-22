package com.wj.nongtian.controller;

import com.wj.nongtian.ResultCode;
import com.wj.nongtian.entity.Track;
import com.wj.nongtian.service.TrackService;
import com.wj.nongtian.service.UserService;
import com.wj.nongtian.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户记录相关请求
 */
@RestController
@RequestMapping("/track")
public class TrackController {

    private Logger logger = Logger.getLogger(getClass());

    private final UserService userService;
    private final TrackService trackService;

    public TrackController(UserService userService, TrackService trackService) {
        this.userService = userService;
        this.trackService = trackService;
    }

    @RequestMapping(value = "/addTracks", method = RequestMethod.GET)
    public String addTracks(Track track) {

        if (track == null || !userService.isUserExist(track.getUid())) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_USERNAME_NOT_FOUND);
        }

        boolean isSuccess = trackService.addTrack(track);
        if (isSuccess) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "发布成功");
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "发布失败");
        }
    }

}
