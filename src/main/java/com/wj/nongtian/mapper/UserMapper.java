package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    boolean isUserNameExists(@Param("username") String username);

    /**
     * 判断用户ID是否存在
     *
     * @param id
     * @return
     */
    boolean isUserIdExists(@Param("uid") int id);

    /**
     * 根据用户名 获取salt
     *
     * @param username
     * @return
     */
    String getSalt(@Param("username") String username);

    /**
     * 获取登录信息
     *
     * @param username
     * @param password
     * @return
     */
    User getLoginInfo(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户ID获取用户信息
     *
     * @param id
     * @return
     */
    User getUserById(@Param("uid") int id);

    /**
     * 获取区域下的所有用户信息
     *
     * @param areaId
     * @return
     */
    List<User> getUsersByAreaId(@Param("areaId") int areaId);

}
