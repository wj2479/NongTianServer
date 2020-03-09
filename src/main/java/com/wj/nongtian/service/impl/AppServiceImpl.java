package com.wj.nongtian.service.impl;

import com.wj.nongtian.entity.AppUpdate;
import com.wj.nongtian.mapper.AppMapper;
import com.wj.nongtian.service.AppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service
public class AppServiceImpl implements AppService {

    @Resource
    private AppMapper appMapper;

    @Override
    public AppUpdate getUpdateInfo() {
        return appMapper.getUpdateInfo();
    }

    @Override
    public boolean addUpdateInfo(int versionCode, String versionName, String info, String configPath, String filename) {
        int count = appMapper.addUpdateInfo(versionCode, versionName, info, configPath, filename);
        if (count == 1) {
            return true;
        }
        return false;
    }
}
