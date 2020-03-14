package com.wj.nongtian.entity;

/**
 * 用户
 */
public class User {

    /**
     * 用户ID ，主键
     */
    private int id;

    /**
     * 用户名
     */
    private String userName = "";

    /**
     * 密码，采用SHA1（密码+salt)加密的方式
     */
    private String passWord = "";

    /**
     * 加密的salt值  每个用户随机生成一个
     */
    private String salt = "";

    /**
     * 昵称
     */
    private String nickName = "";

    /**
     * 手机号码
     */
    private String phone = "";

    /**
     * 手机号是否验证
     */
    private boolean phoneVerified;

    /**
     * 所属区域
     */
    private Area area;

    /**
     * 角色类型
     */
    private Role role;

    /**
     * 年龄
     */
    private int age;

    /**
     * 性别
     */
    private int gender;

    /**
     * 头像地址
     */
    private String avater = "";

    /**
     * 监理公司
     */
    private Company company;

    /**
     * 创建时间
     */
    private String createTime = "";

    /**
     * 更新时间
     */
    private String updateTime = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", salt='" + salt + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneVerified=" + phoneVerified +
                ", area=" + area +
                ", role=" + role +
                ", age=" + age +
                ", gender=" + gender +
                ", avater='" + avater + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
