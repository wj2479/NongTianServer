package com.wj.nongtian.controller;

import com.alibaba.druid.util.StringUtils;
import com.wj.nongtian.ResultCode;
import com.wj.nongtian.config.MyConfig;
import com.wj.nongtian.entity.AppUpdate;
import com.wj.nongtian.entity.BaseAppUpdate;
import com.wj.nongtian.service.AppService;
import com.wj.nongtian.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * App管理相关请求
 */
@RestController
@RequestMapping("/app")
public class AppController {

    private Logger logger = Logger.getLogger(getClass());

    private final AppService appService;
    private final MyConfig mConfig;

    public AppController(AppService appService, MyConfig mConfig) {
        this.appService = appService;
        this.mConfig = mConfig;
    }

    @RequestMapping(value = "/getUpdateInfo", method = RequestMethod.GET)
    public String getUpdateInfo(HttpServletRequest request) {
        AppUpdate appUpdate = appService.getUpdateInfo();

        if (appUpdate != null) {
            BaseAppUpdate base = new BaseAppUpdate();
            base.setContent(appUpdate.getContent());
            base.setVersionCode(appUpdate.getVersionCode());
            base.setVersionName(appUpdate.getVersionName());
            base.setCreateTime(appUpdate.getCreateTime());
            base.setUpdateStatus(appUpdate.getUpdateStatus());

            if (StringUtils.isEmpty(appUpdate.getUrl())) {
                // 重新封装返回的URL
                StringBuilder url = new StringBuilder();
                if (StringUtils.isEmpty(mConfig.getBaseUrl())) {
                    url.append("http://");
                    try {
                        InetAddress address = InetAddress.getLocalHost();
                        url.append(address.getHostAddress());
                    } catch (UnknownHostException e) {
                        url.append(request.getServerName());
                    }
                    url.append(":");
                    url.append(request.getServerPort());
                    url.append("/");
                } else {
                    url.append(mConfig.getBaseUrl());
                }
                url.append(mConfig.getAppFolder()).append(appUpdate.getFileName());

                base.setUrl(url.toString());
            } else {
                base.setUrl(appUpdate.getUrl());
            }

            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, base);
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED);
        }
    }

    @RequestMapping(value = "/addUpdateInfo", method = RequestMethod.POST)
    public String addUpdateInfo(Integer versionCode, String versionName, String info, MultipartFile file) {
        if (StringUtils.isEmpty(versionName) || versionCode == null || StringUtils.isEmpty(info)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "参数不能为空");
        }

        if (file == null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "请选择上传的文件");
        }
        try {
            String configPath = mConfig.getFileUploadFolder() + mConfig.getAppFolder();
            File uploadDir = new File(configPath);
            System.out.println("生成路径：" + uploadDir.getAbsolutePath());
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String filename = file.getOriginalFilename();
//            String suffixName = filename.substring(filename.lastIndexOf("."));
//            String name = UUID.randomUUID().toString();
//            filename = name + suffixName;//图片重命名

            // 要保存的目标文件
            File targetFile = new File(configPath + "/" + filename);
            if (targetFile.exists()) {
                targetFile.delete();
            }

            file.transferTo(targetFile);

            boolean isSuccess = appService.addUpdateInfo(versionCode, versionName, info, configPath, filename);

            if (isSuccess) {
                return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "上传更新信息成功");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED);

    }

}
