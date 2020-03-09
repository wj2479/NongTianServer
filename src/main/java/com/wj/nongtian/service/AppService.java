package com.wj.nongtian.service;


import com.wj.nongtian.entity.AppUpdate;

public interface AppService {

    /**
     * 获取版本更新记录
     *
     * @return
     */
    AppUpdate getUpdateInfo();

    /**
     * 添加版本更新记录
     *
     * @param versionCode 版本号（更新的重要标识）
     * @param versionName 版本名字
     * @param info        更新信息
     * @param configPath  本地路径
     * @param filename    文件名字
     * @return 是否添加成功
     */
    boolean addUpdateInfo(int versionCode, String versionName, String info, String configPath, String filename);
}
