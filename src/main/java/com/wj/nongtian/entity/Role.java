package com.wj.nongtian.entity;

/**
 * 用户角色对象
 */
public class Role {
    /**
     * 角色ID
     */
    private int id;
    /**
     * 角色编码
     */
    private int code;
    /**
     * 角色描述
     */
    private String desc = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
