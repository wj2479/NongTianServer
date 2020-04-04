package com.wj.nongtian.service;


import com.wj.nongtian.entity.Track;
import com.wj.nongtian.entity.UserLastLocation;

import java.util.List;

public interface TrackService {

    boolean addTrack(Track track);

    /**
     * 获取用户最后一次的轨迹
     *
     * @param childUserIdsList
     * @return
     */
    List<UserLastLocation> getLastLocations(List<Integer> childUserIdsList, String date);

    /**
     * 获取用户轨迹
     *
     * @param uid
     * @param date
     * @return
     */
    List<Track> getTracks(int uid, String date);
}
