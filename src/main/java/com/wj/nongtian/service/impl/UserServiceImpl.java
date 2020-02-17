package com.wj.nongtian.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.wj.nongtian.entity.User;
import com.wj.nongtian.mapper.UserMapper;
import com.wj.nongtian.service.UserService;
import com.wj.nongtian.utils.RandomUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = Logger.getLogger(getClass());

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean isUserExist(String username) {
        return userMapper.isUserNameExists(username);
    }

    @Override
    public boolean isUserExist(int id) {
        return userMapper.isUserIdExists(id);
    }

    @Override
    public User login(String username, String password) {
        String salt = userMapper.getSalt(username);
        if (StringUtils.isEmpty(salt)) { // 如果密码没有加盐 就直接登录
            // 随机长度的随机数
            int randomLen = new Random().nextInt(3) + 8;
            salt = RandomUtils.getRandomString(randomLen);
            String sha1 = DigestUtils.sha1Hex(password + salt);

            return userMapper.getLoginInfo(username, password);
        } else {
            String sha1 = DigestUtils.sha1Hex(password + salt);
            return userMapper.getLoginInfo(username, sha1);
        }
    }

    @Override
    public User getUser(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public List<User> getUsersByAreaId(int areaId) {
        return userMapper.getUsersByAreaId(areaId);
    }


}
