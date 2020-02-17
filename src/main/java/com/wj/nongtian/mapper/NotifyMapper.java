package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.Notify;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NotifyMapper {

    /**
     * 根据UID获取用户发布的通知
     *
     * @param uid
     * @return
     */
    @Select("select * from notify where uid = #{uid}")
    List<Notify> getPublishNotifysByUserId(@Param("uid") int uid);

    /**
     * 添加新的通知
     *
     * @param notify
     * @return
     */
    @Insert("insert into notify(uid,title,content,type,createtime) values(#{uid},#{title},#{content},#{type},now())")
    //加入该注解可以保持对象后，查看对象插入id
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addNotify(Notify notify);

    /**
     * 添加通知接收记录
     *
     * @param nid 通知ID
     * @param uid 用户ID
     * @return 受影响的行数
     */
    @Insert("insert into notify_read(nid,uid,isread) values(#{nid},#{uid},1)")
    int addNotifyReceiver(@Param("nid") int nid, @Param("uid") int uid);

    /**
     * 设置通知状态已读
     *
     * @param nid
     * @param uid
     * @return
     */
    @Update("update notify_read set isread = 1 , readtime = now() where uid = #{uid} and nid = #{nid}")
    int setNotifyRead(@Param("nid") int nid, @Param("uid") int uid);

    /**
     * 获取通知的接受者ID列表
     *
     * @param nid
     * @return
     */
    @Select("select distinct uid from notify_read where nid = #{nid}")
    List<Integer> getNotifyReceivers(@Param("nid") int nid);

    /**
     * 根据用户ID 获取接收到的通知ID列表
     *
     * @param uid
     * @return
     */
    @Select("select distinct nid from notify_read where uid = #{uid} ")
    List<Integer> getReceiveNotifyIdsByUserId(@Param("uid") int uid);

    /**
     * 根据通知ID获取通知对象
     *
     * @param nid
     * @return
     */
    @Select("select * from notify where id = #{nid}")
    Notify getNotifyById(@Param("nid") int nid);
}
