package com.wj.nongtian.entity;

/**
 * 版本更新信息的基类
 */
public class BaseAppUpdate {

    /**
     * 版本号
     */
    private int versionCode;
    /**
     * 版本名称
     */
    private String versionName = "";
    /**
     * 更新内容
     */
    private String content = "";
    /**
     * 文件下载地址
     */
    private String url = "";
    /**
     * 版本更新状态     0 正常更新  1 强制更新
     */
    private int updateStatus = 0;
    /**
     * 创建时间
     */
    private String createTime = "";

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(int updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
