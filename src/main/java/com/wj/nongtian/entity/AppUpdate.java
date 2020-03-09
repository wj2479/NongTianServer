package com.wj.nongtian.entity;

/**
 * APP更新对象
 */
public class AppUpdate extends BaseAppUpdate{
    /**
     * 主键
     */
    int id;

    /**
     * 文件保存的根目录
     */
    String rootPath = "";
    /**
     * 保存的文件名
     */
    String fileName = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
