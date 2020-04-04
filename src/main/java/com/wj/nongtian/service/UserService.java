package com.wj.nongtian.service;


import com.wj.nongtian.entity.Area;
import com.wj.nongtian.entity.User;

import java.util.List;

public interface UserService {

    boolean isUserExist(String username);

    boolean isUserExist(int id);

    User login(String username, String password);

    User getUser(int id);

    /**
     * 仅获取当前区域下的所有用户
     *
     * @param areaId
     * @return
     */
    List<User> getUsersByAreaId(int areaId);

    /**
     * 获取当前区域及子区域下所有的监理
     *
     * @param area
     * @return
     */
    List<User> getAllSupervisorsByAreaId(Area area);

    /**
     * 获取当前区域及子区域下所有的监理ID
     *
     * @param area
     * @return
     */
    List<Integer> getAllSupervisorIdsByAreaId(Area area);

    boolean setPassword(String username, String newPwd);

    boolean updateUserInfo(Integer uid, String nickname, String phone, Integer age, Integer gender);
}
