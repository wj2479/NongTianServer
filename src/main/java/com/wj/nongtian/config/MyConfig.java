package com.wj.nongtian.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义配置对象
 */
@Component
@ConfigurationProperties(prefix = "myconfig")
public class MyConfig  {

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
}
