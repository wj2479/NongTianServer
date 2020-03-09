package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.AppUpdate;
import org.apache.ibatis.annotations.*;

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
            @Result(property = "rootPath", column = "rootpath"),
            @Result(property = "fileName", column = "filename"),
            @Result(property = "updateStatus", column = "updatestatus"),
            @Result(property = "createTime", column = "createtime")
    })
    AppUpdate getUpdateInfo();

    @Insert("insert into app_update(versioncode,versionname,content,rootpath,filename,createtime) values(#{versioncode},#{versionname},#{content},#{rootpath},#{filename},now())")
    int addUpdateInfo(@Param("versioncode") int versionCode,@Param("versionname") String versionName, @Param("content")String info, @Param("rootpath")String configPath,@Param("filename") String filename);
}
