package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.Track;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface TrackMapper {

    /**
     * 添加新的通知
     *
     * @param track
     * @return
     */
    @Insert("insert into tracks (uid,accuracy,lng,lat,deviceVersion,direction,deviceModel,deviceFacturer,locationType,speed,locationTime,address,remark,createtime)\n" +
            "values(#{uid},#{accuracy},#{lng},#{lat},#{deviceVersion},#{direction},#{deviceModel},#{deviceFacturer},#{locationType},#{speed},#{locationTime},#{address},#{remark},now())")
    //加入该注解可以保持对象后，查看对象插入id
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addTrack(Track track);
}
