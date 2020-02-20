package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.AppUpdate;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface AppMapper {

    /**
     * 获取最新的版本更新信息
     *
     * @return
     */
    @Select("select * from app_update order by versioncode desc limit 1")
    @Results({
            @Result(property = "versionCode", column = "versioncode"),
            @Result(property = "versionName", column = "versionname"),
            @Result(property = "createTime", column = "createtime"),
    })
    AppUpdate getUpdateInfo();
}
