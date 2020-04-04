package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.Track;
import com.wj.nongtian.entity.UserLastLocation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    List<UserLastLocation> getLastLocations(@Param("ids") List<Integer> childUserIdsList, @Param("date") String date);

    /**
     * 获取用户轨迹
     *
     * @param uid
     * @param date
     * @return
     */
    @Select("select * \n" +
            "from (\n" +
            "\tselect *,DATE_FORMAT(createtime,'%Y-%m-%d')  date from  tracks \n" +
            ") p\n" +
            "where p.uid = #{uid}  and date = #{date} \n" +
            "order by locationTime ")
    List<Track> getTracks(@Param("uid") int uid, @Param("date") String date);
}
