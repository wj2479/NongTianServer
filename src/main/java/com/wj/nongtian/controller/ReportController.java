package com.wj.nongtian.controller;

import com.alibaba.druid.util.StringUtils;
import com.wj.nongtian.ResultCode;
import com.wj.nongtian.config.MyConfig;
import com.wj.nongtian.entity.DaySchedule;
import com.wj.nongtian.entity.ProjectDailyReport;
import com.wj.nongtian.entity.ReportMedia;
import com.wj.nongtian.entity.ResponseMedia;
import com.wj.nongtian.service.ProjectService;
import com.wj.nongtian.service.ReportService;
import com.wj.nongtian.service.UserService;
import com.wj.nongtian.utils.JsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * 日报相关请求
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    private Logger logger = Logger.getLogger(getClass());

    Calendar calendar = Calendar.getInstance();

    @Autowired
    private ReportService reportService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MyConfig mConfig;

    @Deprecated
    @RequestMapping(value = "/getDailyReports", method = RequestMethod.GET)
    public String getDailyReports(Integer pid, HttpServletRequest request) {
        if (pid == null || pid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        String result = "";
        List<ProjectDailyReport> dailyReports = reportService.getDailyReports(pid);

        if (dailyReports != null) {
            for (ProjectDailyReport dailyReport : dailyReports) {
                List<ReportMedia> reportMedia = reportService.getReportMedias(dailyReport.getId());
                System.out.println("项目日报记录" + dailyReport.getId() + " -->" + reportMedia.size());
                if (reportMedia != null && reportMedia.size() > 0) {
                    List<ResponseMedia> list = new ArrayList<>();
                    for (ReportMedia media : reportMedia) {
                        ResponseMedia responseMedia = new ResponseMedia();
                        responseMedia.setName(media.getName());
                        responseMedia.setMd5(media.getMd5());
                        responseMedia.setType(media.getType());
                        // 重新封装返回的URL
                        String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + mConfig.getImageFolder() + media.getPath();
                        responseMedia.setUrl(url);
                        list.add(responseMedia);
                    }
                    dailyReport.setMediaList(list);
                }
            }
        }

        if (dailyReports != null) {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_OK, dailyReports);
        } else {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "没有找到结果");
        }
        return result;
    }

    @Deprecated
    @RequestMapping(value = "/uploadDailyReport2", method = RequestMethod.POST)
    public String uploadDailyReport2(String title, String content, Integer pid, Integer uid, Integer check, Integer schedule, String address, String lnglat, MultipartFile[] files) throws IOException {

        // 判断项目ID是不是正确
        if (pid != null && pid > 0) {
            boolean isProjectExists = projectService.isProjectExist(pid);
            if (!isProjectExists) {
                return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR, "项目ID不存在");
            }
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        // 判断用户ID是不是正确
        if (uid != null && uid > 0) {
            boolean isUserExists = userService.isUserExist(uid);
            if (!isUserExists) {
                return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR, "用户ID不存在");
            }
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        // 参数校验
        if (StringUtils.isEmpty(title) || check == null || schedule == null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        System.out.println("当前上传的属性：" + title + " " + content + " " + pid + "  " + uid + " " + check + " " + schedule + " " + address + " " + lnglat);

//        reportService.addDailyReport();

        if (files != null && files.length > 0) {
            // 配置文件配置的上传文件根目录
            String configPath = mConfig.getFileUploadFolder();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;

            // 根据年月生成目录
            File uploadDir = new File(configPath + File.separator + year + File.separator + month);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            for (MultipartFile multipartFile : files) {
                if (multipartFile.isEmpty()) {
                    return "文件上传失败";
                }
                byte[] fileBytes = multipartFile.getBytes();

                //取得当前上传文件的文件名称
                String originalFilename = multipartFile.getOriginalFilename();
                System.out.println("当前上传的文件名：" + originalFilename);
                //生成文件名
                String fileName = UUID.randomUUID() + "&" + originalFilename;
            }
        }
        return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "上传成功");

    }

    @RequestMapping(value = "/getDailySchedule", method = RequestMethod.GET)
    public String getDailySchedule(Integer pid, HttpServletRequest request) {
        if (pid == null || pid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        String result = "";
        List<DaySchedule> daySchedules = reportService.getDailySchedule(pid);

        if (daySchedules != null) {
            for (DaySchedule daySchedule : daySchedules) {
                List<ReportMedia> mediaList = reportService.getDailyMedias(pid, daySchedule.getDate());
                if (mediaList != null && mediaList.size() > 0) {
                    ReportMedia media = mediaList.get(0);
                    ResponseMedia responseMedia = new ResponseMedia();
                    responseMedia.setName(media.getName());
                    responseMedia.setMd5(media.getMd5());
                    responseMedia.setType(media.getType());
                    // 重新封装返回的URL
                    String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + mConfig.getImageFolder() + media.getPath();
                    responseMedia.setUrl(url);
                    daySchedule.setLastMedia(responseMedia);
                }
            }
        }

        if (daySchedules != null) {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_OK, daySchedules);
        } else {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "没有找到结果");
        }
        return result;
    }

    @RequestMapping(value = "/uploadDailyReport", method = RequestMethod.POST)
    public String uploadDailyReport(ProjectDailyReport dailyReport, MultipartFile[] files, HttpServletRequest request) {
        // 判断项目ID是不是正确
        if (dailyReport.getPid() == 0 || !projectService.isProjectExist(dailyReport.getPid())) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }
        // 判断用户ID是不是正确
        if (dailyReport.getUid() == 0 || !userService.isUserExist(dailyReport.getUid())) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        //校验其他参数
        if (dailyReport.getCheck() < 0 || StringUtils.isEmpty(dailyReport.getTitle()) || dailyReport.getSchedule() < 0 || dailyReport.getSchedule() > 100) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        System.out.println("当前上传的对象：" + dailyReport.toString());

        System.out.println("当前：" + request.getScheme() + "  " + request.getServerName());

        try {
            int id = reportService.addDailyReport(dailyReport);
//            System.out.println("当前上传的对象ID：" + dailyReport.getId());

            projectService.updateProjectSchedule(dailyReport.getPid(), dailyReport.getSchedule());

            if (files != null && files.length > 0) {
                // 配置文件配置的上传文件根目录
                String configPath = mConfig.getFileUploadFolder() + mConfig.getImageFolder();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;

                String monthStr = month > 9 ? month + "" : "0" + month;

                // 根据年月生成目录
                String path = year + "/" + monthStr;
                String uploadPath = configPath + "/" + path;
                File uploadDir = new File(uploadPath);
                System.out.println("生成路径：" + uploadDir.getAbsolutePath());
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                for (MultipartFile multipartFile : files) {
                    if (multipartFile.isEmpty()) {
                        continue;
                    }
                    //取得当前上传文件的文件名称
                    String filename = multipartFile.getOriginalFilename();
                    System.out.println("当前上传的文件名：" + filename);

                    filename = getFilePath(uploadPath, filename);

                    // 保存文件
                    File savedFile = new File(uploadPath + "/" + filename);
                    multipartFile.transferTo(savedFile);

                    ReportMedia media = new ReportMedia();
                    media.setMd5(getMd5FromFile(savedFile));
                    media.setRootpath(configPath);
                    media.setName(multipartFile.getOriginalFilename());
                    media.setType(multipartFile.getContentType());
                    media.setPath(path + "/" + filename);
                    media.setRid(dailyReport.getId());

                    reportService.addReportMedias(media);
                }
            }

            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "上传失败");

    }

    /**
     * 获取文件的MD5加密数据
     *
     * @param file
     * @return
     * @throws IOException
     */
    private String getMd5FromFile(File file) throws IOException {
        String md5 = "";
        if (file == null)
            return md5;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            md5 = DigestUtils.md5Hex(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return md5;
    }

    /**
     * 获取文件路径名，如果重复 就重命名
     *
     * @param filePath
     * @return
     */
    private String getFilePath(String rootPath, String filePath) {
        // 保存文件
        File file = new File(rootPath + "/" + filePath);
        if (file.exists()) {
            int index = filePath.lastIndexOf(".");
            String start = filePath.substring(0, index);
            String end = filePath.substring(index);

            String path = start + "_" + System.currentTimeMillis() + end;
            return getFilePath(rootPath, path);
        }
        return filePath;
    }
}
