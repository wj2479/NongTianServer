package com.wj.nongtian.service;


import com.wj.nongtian.entity.User;

import java.util.List;

public interface UserService {

    boolean isUserExist(String username);

    boolean isUserExist(int id);

    User login(String username, String password);

    User getUser(int id);

    List<User> getUsersByAreaId(int areaId);

    boolean setPassword(String username, String newPwd);
}
