package com.wj.nongtian.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.wj.nongtian.entity.Area;
import com.wj.nongtian.entity.Role;
import com.wj.nongtian.entity.User;
import com.wj.nongtian.mapper.AreaMapper;
import com.wj.nongtian.mapper.UserMapper;
import com.wj.nongtian.service.UserService;
import com.wj.nongtian.utils.RandomUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = Logger.getLogger(getClass());

    @Resource
    private UserMapper userMapper;
    @Resource
    private AreaMapper areaMapper;

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

    @Override
    public List<User> getAllSupervisorsByAreaId(Area area) {
        List<User> childUserList = new ArrayList<>();
        fillAreaUser(area, childUserList, null);
        return childUserList;
    }

    @Override
    public List<Integer> getAllSupervisorIdsByAreaId(Area area) {
        List<Integer> childUserIdsList = new ArrayList<>();
        fillAreaUser(area, null, childUserIdsList);
        return childUserIdsList;
    }

    @Override
    public boolean setPassword(String username, String newPwd) {

        // 设置密码的时候 重新生成salt值
        int randomLen = new Random().nextInt(3) + 8;
        String salt = RandomUtils.getRandomString(randomLen);
        String password = DigestUtils.sha1Hex(newPwd + salt);

        int count = userMapper.updateUserPassword(username, salt, password);
        if (count == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserInfo(Integer uid, String nickname, String phone, Integer age, Integer gender) {
        int count = userMapper.updateUserInfo(uid, nickname, phone, age, gender);
        return true;
    }


    /**
     * 递归的填充下级用户
     *
     * @param
     */
    private void fillAreaUser(Area area, List<User> childUserList, List<Integer> childUserIdsList) {
        if (area == null || (childUserList == null && childUserIdsList == null)) {
            return;
        }

        // 如果这个区域还有下级区域
        if (area.getRegionLevel() < 4) {
            // 获取下级区域列表
            List<Area> areaList = areaMapper.getSubAreaById(area.getId());
            for (Area a : areaList) {
                fillAreaUser(a, childUserList, childUserIdsList);
            }
        } else {
            List<User> userList = getUsersByAreaId(area.getId());
            for (User user : userList) {
                if (user.getRole().getCode() == Role.CODE.SUPERVISOR.getValue()) {
                    if (childUserList != null) {
                        childUserList.add(user);
                    }
                    if (childUserIdsList != null) {
                        childUserIdsList.add(user.getId());
                    }
                }
            }
        }
    }


}
