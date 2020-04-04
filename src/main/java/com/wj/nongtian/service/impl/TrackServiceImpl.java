package com.wj.nongtian.service.impl;

import com.wj.nongtian.entity.Track;
import com.wj.nongtian.entity.UserLastLocation;
import com.wj.nongtian.mapper.TrackMapper;
import com.wj.nongtian.service.TrackService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class TrackServiceImpl implements TrackService {
    private Logger logger = Logger.getLogger(getClass());
    @Resource
    private TrackMapper trackMapper;

    @Override
    public boolean addTrack(Track track) {
        try {
            int count = trackMapper.addTrack(track);
            if (count == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加新轨迹异常：" + e.toString());
        }
        return false;
    }

    @Override
    public List<UserLastLocation> getLastLocations(List<Integer> childUserIdsList, String date) {
        return trackMapper.getLastLocations(childUserIdsList, date);
    }

    @Override
    public List<Track> getTracks(int uid, String date) {
        return trackMapper.getTracks(uid, date);
    }
}
