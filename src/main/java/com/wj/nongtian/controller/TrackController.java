package com.wj.nongtian.controller;

import com.alibaba.druid.util.StringUtils;
import com.wj.nongtian.ResultCode;
import com.wj.nongtian.entity.Track;
import com.wj.nongtian.entity.User;
import com.wj.nongtian.entity.UserLastLocation;
import com.wj.nongtian.service.AreaService;
import com.wj.nongtian.service.TrackService;
import com.wj.nongtian.service.UserService;
import com.wj.nongtian.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 用户记录相关请求
 */
@RestController
@RequestMapping("/track")
public class TrackController {

    private Logger logger = Logger.getLogger(getClass());

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final UserService userService;
    private final AreaService areaService;
    private final TrackService trackService;

    public TrackController(UserService userService, AreaService areaService, TrackService trackService) {
        this.userService = userService;
        this.areaService = areaService;
        this.trackService = trackService;
    }

    /**
     * 添加轨迹记录
     *
     * @param track
     * @return
     */
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

    /**
     * 获取子用户的最后位置列表
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "/getChildUserLastPosition", method = RequestMethod.GET)
    public String getChildUserLastPosition(Integer uid, String date, Integer level) {

        logger.info("查询用户最后未知请求:" + uid + "  " + date);
        if (uid == null || !userService.isUserExist(uid)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        try {
            User user = userService.getUser(uid);
            List<Integer> childUserIdsList = userService.getAllSupervisorIdsByAreaId(user.getArea());

            if (StringUtils.isEmpty(date)) {
                date = dateFormat.format(new Date());
            }
            List<UserLastLocation> userLastLocationList = trackService.getLastLocations(childUserIdsList, date);

            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, userLastLocationList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "发布失败");
    }

    @RequestMapping(value = "/getTracks", method = RequestMethod.GET)
    public String getTracks(Integer uid, String date) {
        logger.info("查询用户轨迹请求:" + uid + "  " + date);

        if (uid == null || !userService.isUserExist(uid)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_USERNAME_NOT_FOUND);
        }

        if (StringUtils.isEmpty(date)) {
            date = dateFormat.format(new Date());
        }

        List<Track> tracks = trackService.getTracks(uid, date);
        if (tracks != null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, tracks);
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED);
        }
    }

}
