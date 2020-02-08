package com.wj.nongtian.service;


import com.wj.nongtian.entity.User;

public interface UserService {

    boolean isUserExist(String username);

    boolean isUserExist(int id);

    User login(String username, String password);
}
