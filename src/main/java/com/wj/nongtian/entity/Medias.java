package com.wj.nongtian.entity;

/**
 * 图片或者视频等多媒体对象
 */
public class Medias {

    /**
     * 保存根路径
     */
    private String rootPath = "";
    /**
     * 保存路径
     */
    private String path = "";
    /**
     * 文件名字
     */
    private String name = "";
    /**
     * md5校验码
     */
    private String md5 = "";
    /**
     * 文件类型
     */
    private String type = "";

    public String getRootpath() {
        return rootPath;
    }

    public void setRootpath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
