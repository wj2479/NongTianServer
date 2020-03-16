package com.wj.nongtian.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义配置对象
 */
@Component
@ConfigurationProperties(prefix = "myconfig")
public class MyConfig {

    /**
     * 文件上传的文件夹
     */
    String fileUploadFolder;
    /**
     * 浏览器访问路径
     */
    String fileUri;
    /**
     * 静态资源对外暴露的访问路径(访问图片的路径)
     */
    String fileStaticAccessPath;
    /**
     * 升级APP上传的路径
     */
    String appFolder;
    /**
     * 图片上传的路径
     */
    String imageFolder;

    /**
     * 服务器地址
     */
    String baseUrl;

    public String getFileUploadFolder() {
        return fileUploadFolder;
    }

    public void setFileUploadFolder(String fileUploadFolder) {
        this.fileUploadFolder = fileUploadFolder;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getFileStaticAccessPath() {
        return fileStaticAccessPath;
    }

    public void setFileStaticAccessPath(String fileStaticAccessPath) {
        this.fileStaticAccessPath = fileStaticAccessPath;
    }

    public String getAppFolder() {
        return appFolder;
    }

    public void setAppFolder(String appFolder) {
        this.appFolder = appFolder;
    }

    public String getImageFolder() {
        return imageFolder;
    }

    public void setImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
