package com.wj.nongtian.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfiger implements WebMvcConfigurer {

    @Autowired
    MyConfig myConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("配置：" + myConfig.fileUri);

        registry.addResourceHandler(myConfig.fileStaticAccessPath).addResourceLocations("file:" + myConfig.fileUploadFolder);
    }
}
