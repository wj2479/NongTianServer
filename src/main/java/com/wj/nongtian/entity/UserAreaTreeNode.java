package com.wj.nongtian.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户区域节点
 *
 * @Author wj
 * @Date 2020/1/2
 * @Desc
 * @Url http://www.chuangze.cn
 */
public class UserAreaTreeNode {

    /**
     * 区域对象
     */
    private Area area;
    /**
     * 子节点列表
     */
    private List<UserAreaTreeNode> childs = null;
    /**
     * 节点根用户列表
     */
    private List<User> userInfos = null;

    public UserAreaTreeNode() {
        childs = new ArrayList<>(5);
        userInfos = new ArrayList<>();
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<UserAreaTreeNode> getChilds() {
        return childs;
    }

    public void setChilds(List<UserAreaTreeNode> childs) {
        this.childs = childs;
    }

    public List<User> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<User> userInfos) {
        this.userInfos = userInfos;
    }

    @Override
    public String toString() {
        return "UserAreaTreeNode{" +
                "area=" + area +
                ", childs=" + childs +
                ", userInfos=" + userInfos +
                '}';
    }
}
