package com.wj.nongtian.entity;

/**
 * 简化的项目对象
 */
public class SimpleProject {
    /**
     * 项目ID ，主键
     */
    private int id;

    /**
     * 项目名称
     */
    private String name = "";

    /**
     * 项目进度
     */
    private int process;

    /**
     * 上级项目的ID  根项目的id为-1
     */
    private int parentId;

    /**
     * 项目的级别
     */
    private int level = 0;

    /**
     * 创建时间
     */
    private String createTime = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
