package com.wj.nongtian.controller;

import com.alibaba.druid.util.StringUtils;
import com.wj.nongtian.ResultCode;
import com.wj.nongtian.config.MyConfig;
import com.wj.nongtian.entity.*;
import com.wj.nongtian.service.ProjectService;
import com.wj.nongtian.service.ReportService;
import com.wj.nongtian.service.UserService;
import com.wj.nongtian.utils.FileUtils;
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
import java.util.*;

/**
 * 日报相关请求
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    private Logger logger = Logger.getLogger(getClass());

    private static final String DATE_FORMAT_Y_M_D = "%Y-%m-%d";
    private static final String DATE_FORMAT_Y_M = "%Y-%m";

    Calendar calendar = Calendar.getInstance();

    private final ReportService reportService;
    private final UserService userService;
    private final ProjectService projectService;
    private final MyConfig mConfig;

    public ReportController(ReportService reportService, UserService userService, ProjectService projectService, MyConfig mConfig) {
        this.reportService = reportService;
        this.userService = userService;
        this.projectService = projectService;
        this.mConfig = mConfig;
    }

    @RequestMapping(value = "/getDailyReports", method = RequestMethod.GET)
    public String getDailyReports(Integer pid, String date, HttpServletRequest request) {
        if (pid == null || pid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        if (StringUtils.isEmpty(date) || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        String result = "";
        List<ProjectDailyReport> dailyReports = reportService.getDailyReports(pid, date);
        fillReportMediaList(dailyReports, request);

        if (dailyReports != null) {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_OK, dailyReports);
        } else {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "没有找到结果");
        }
        return result;
    }

    /**
     * 填充日志的媒体数据
     *
     * @param dailyReports
     * @param request
     */
    private void fillReportMediaList(List<ProjectDailyReport> dailyReports, HttpServletRequest request) {
        if (dailyReports != null) {
            for (ProjectDailyReport dailyReport : dailyReports) {
                fillReportMedia(dailyReport, request);
            }
        }
    }

    /**
     * 填充日志的媒体数据
     *
     * @param dailyReport
     * @param request
     */
    private void fillReportMedia(ProjectDailyReport dailyReport, HttpServletRequest request) {
        List<ReportMedia> reportMedia = reportService.getReportMedias(dailyReport.getId());
        if (reportMedia != null && reportMedia.size() > 0) {
            List<ResponseMedia> list = new ArrayList<>();
            for (ReportMedia media : reportMedia) {
                ResponseMedia responseMedia = new ResponseMedia();
                responseMedia.setName(media.getName());
                responseMedia.setMd5(media.getMd5());
                responseMedia.setType(media.getType());

                String url = getMediaUrl(request, media);
                responseMedia.setUrl(url);
                list.add(responseMedia);
            }
            dailyReport.setMediaList(list);
        }
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
            calendar.setTime(new Date());
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

        logger.info("请求每日汇总数据:" + pid);

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

                    String url = getMediaUrl(request, media);
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

    /**
     * 获取媒体封装的地址
     *
     * @param request
     * @param media
     * @return
     */
    private String getMediaUrl(HttpServletRequest request, ReportMedia media) {
        if (StringUtils.isEmpty(media.getUrl())) {
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
            url.append(mConfig.getImageFolder()).append(media.getPath());
            return url.toString();
        } else {
            return media.getUrl();
        }
    }

    @RequestMapping(value = "/uploadDailyReport", method = RequestMethod.POST)
    public String uploadDailyReport(ProjectDailyReport dailyReport, MultipartFile[] files) {
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

        logger.info("日报上传请求：" + dailyReport.toString());

        try {
            int id = reportService.addDailyReport(dailyReport);
//            System.out.println("当前上传的对象ID：" + dailyReport.getId());

            projectService.updateProjectSchedule(dailyReport.getPid(), dailyReport.getSchedule());

            if (files != null && files.length > 0) {
                // 配置文件配置的上传文件根目录
                String configPath = mConfig.getFileUploadFolder() + mConfig.getImageFolder();
                calendar.setTime(new Date());
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

                    filename = FileUtils.getFilePath(uploadPath, filename);

                    // 保存文件
                    File savedFile = new File(uploadPath + "/" + filename);
                    multipartFile.transferTo(savedFile);

                    ReportMedia media = new ReportMedia();
                    media.setMd5(FileUtils.getMd5FromFile(savedFile));
                    media.setRootpath(configPath);
                    media.setName(multipartFile.getOriginalFilename());
                    media.setType(multipartFile.getContentType());
                    media.setPath(path + "/" + filename);
                    media.setRid(dailyReport.getId());

                    reportService.addReportMedias(media);
                }
            }
            logger.info("日报上传成功：" + dailyReport.getId());
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("日报上传失败：" + e.toString());
        }

        return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "上传失败");
    }

    @RequestMapping(value = "/getUnqualifiedReportList", method = RequestMethod.GET)
    public String getUnqualifiedReportList(Integer pid, HttpServletRequest request) {
        logger.info("查询不合格项目请求：" + pid);
        if (pid == null || pid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        Project project = projectService.getProjectById(pid);
        // 判断项目ID是不是正确
        if (project == null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR, "项目不存在");
        }

        List<Integer> ids = new ArrayList<>();
        fillEndProjectId(ids, project.getId());

        logger.info("查询的项目id:" + ids.toString());
        List<ProjectDailyReport> unqualifiedReports = reportService.getUnqualifiedReports(ids);

        List<Project> reportList = initProjectByReports(unqualifiedReports, request);
        return JsonUtils.getJsonResult(ResultCode.RESULT_OK, reportList);
    }

    /**
     * 查询所有子节点的最后一级项目ID
     *
     * @param ids
     * @param pid
     */
    private void fillEndProjectId(List<Integer> ids, int pid) {
        if (ids == null)
            return;

        // 先查看这个项目有没有子项目
        List<Project> projects = projectService.getSubProjectsByParentId(pid);
        if (projects == null || projects.size() == 0) {
            ids.add(pid);
        } else {
            projects.forEach(project -> {
                fillEndProjectId(ids, project.getId());
            });
        }
    }

    /************************************日报关注相关操作***********************************************/

    @RequestMapping(value = "/focusReport", method = RequestMethod.GET)
    public String focusReport(Integer uid, Integer rid, Boolean isFocus) {
        if (uid == null || rid == null || isFocus == null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }
        logger.info("日报关注/取消:" + uid + " " + rid + " " + isFocus);

        boolean isFocused = reportService.isReportFocused(uid, rid);
        try {
            // 如果当前已经关注了
            if (isFocused) {
                if (isFocus) {
                    return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "您已经关注该日志了");
                } else {
                    reportService.focusReport(uid, rid, isFocus);
                    return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "取消关注成功");
                }
            } else {
                if (isFocus) {
                    reportService.focusReport(uid, rid, isFocus);
                    return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "关注成功");
                } else {
                    return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "您还没有关注该日志");
                }
            }
        } catch (Exception e) {

        }
        return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED);
    }

    @RequestMapping(value = "/getFocusReports", method = RequestMethod.GET)
    public String getFocusReports(Integer uid, HttpServletRequest request) {
        if (uid == null) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        logger.info("获取关注日报请求:" + uid);

        List<Project> reportList = new ArrayList<>();
        //获取用户关注的日报ID列表
        List<Integer> ids = reportService.getFocusReportIds(uid);
        if (ids.size() > 0) {
            // 获取这些ID对应的列表
            List<ProjectDailyReport> reportsByIds = reportService.getReportsByIds(ids);
            // 封装数据
            reportList = initProjectByReports(reportsByIds, request);
        }
        return JsonUtils.getJsonResult(ResultCode.RESULT_OK, reportList);
    }

    /**
     * 按照项目将日报分组 并返回项目+日报的数据
     *
     * @param reports
     * @param request
     * @return
     */
    private List<Project> initProjectByReports(List<ProjectDailyReport> reports, HttpServletRequest request) {
        Map<Integer, List<ProjectDailyReport>> map = new HashMap<>();

        // 按照Pid 进行分组
        reports.forEach(report -> {
            // 日报的照片 视频数据
            fillReportMedia(report, request);

            if (map.containsKey(report.getPid())) {
                List<ProjectDailyReport> list = map.get(report.getPid());
                list.add(report);
            } else {
                List<ProjectDailyReport> list = new ArrayList<>();
                list.add(report);
                map.put(report.getPid(), list);
            }
        });

        // 查询项目详情 封装数据
        List<Project> reportList = new ArrayList<>();
        for (Map.Entry<Integer, List<ProjectDailyReport>> entry : map.entrySet()) {
            Project proj = projectService.getProjectById(entry.getKey());
            proj.setReports(entry.getValue());
            reportList.add(proj);
        }
        return reportList;
    }

    /************************************日报评论相关操作***********************************************/

    @RequestMapping(value = "/addReportComment", method = RequestMethod.POST)
    public String addReportComment(ReportComment reportComment, MultipartFile[] files) {
        // 判断ID是不是正确
        if (reportComment == null || reportComment.getRid() == 0 || reportComment.getSender() == 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        //校验其他参数
        if (reportComment.getContent().isEmpty() && (files == null || files.length == 0)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        logger.info("添加日报评论请求:" + reportComment);

        try {
            int id = reportService.addReportComment(reportComment);

            if (files != null && files.length > 0) {
                // 配置文件配置的上传文件根目录
                String configPath = mConfig.getFileUploadFolder() + mConfig.getImageFolder();
                calendar.setTime(new Date());
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

                    filename = FileUtils.getFilePath(uploadPath, filename);

                    // 保存文件
                    File savedFile = new File(uploadPath + "/" + filename);
                    multipartFile.transferTo(savedFile);

                    ReportMedia media = new ReportMedia();
                    media.setMd5(FileUtils.getMd5FromFile(savedFile));
                    media.setRootpath(configPath);
                    media.setName(multipartFile.getOriginalFilename());
                    media.setType(multipartFile.getContentType());
                    media.setPath(path + "/" + filename);
                    media.setRid(reportComment.getId());

                    reportService.addCommentMedias(media);
                }
            }
            logger.info("评论添加成功:" + reportComment.getId());
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, "评论成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("评论添加失败:" + e.toString());
        }
        return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "评论失败");
    }

    @RequestMapping(value = "/getReportComments", method = RequestMethod.GET)
    public String getReportComments(Integer rid, HttpServletRequest request) {
        if (rid == null || rid < 0) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        logger.info("获取日报评论请求:" + rid);

        String result = "";
        List<ReportComment> reportComments = reportService.getReportComments(rid);
        fillCommentMediaList(reportComments, request);

        if (reportComments != null) {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_OK, reportComments);
        } else {
            result = JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "没有找到结果");
        }
        return result;
    }

    /**
     * 填充日志的媒体数据
     *
     * @param reportComments
     * @param request
     */
    private void fillCommentMediaList(List<ReportComment> reportComments, HttpServletRequest request) {
        if (reportComments != null) {
            for (ReportComment reportComment : reportComments) {
                fillCommentMedia(reportComment, request);
            }
        }
    }

    /**
     * 填充日志的媒体数据
     *
     * @param reportComment
     * @param request
     */
    private void fillCommentMedia(ReportComment reportComment, HttpServletRequest request) {
        List<ReportMedia> reportMedia = reportService.getCommentMedias(reportComment.getId());
        if (reportMedia != null && reportMedia.size() > 0) {
            List<ResponseMedia> list = new ArrayList<>();
            for (ReportMedia media : reportMedia) {
                ResponseMedia responseMedia = new ResponseMedia();
                responseMedia.setName(media.getName());
                responseMedia.setMd5(media.getMd5());
                responseMedia.setType(media.getType());

                String url = getMediaUrl(request, media);
                responseMedia.setUrl(url);
                list.add(responseMedia);
            }
            reportComment.setMediaList(list);
        }
    }


    /************************************日报统计相关操作***********************************************/

    @RequestMapping(value = "/getReportCount", method = RequestMethod.GET)
    public String getReportCount(Integer uid, String date) {
        if (uid == null || !userService.isUserExist(uid)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        logger.info("日报统计查询日期:" + date);
        String dFormat = "";

        if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            dFormat = DATE_FORMAT_Y_M_D;
        } else if (date.matches("\\d{4}-\\d{2}")) {
            dFormat = DATE_FORMAT_Y_M;
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        User user = userService.getUser(uid);
        List<Integer> childUserIdsList = userService.getAllSupervisorIdsByAreaId(user.getArea());

        List<UserReportCount> reportCountList = reportService.getReportDayOrMonthCount(childUserIdsList, date, dFormat);

        return JsonUtils.getJsonResult(ResultCode.RESULT_OK, reportCountList);
    }

    @RequestMapping(value = "/getReportCountBetween", method = RequestMethod.GET)
    public String getReportCountBetween(Integer uid, String startDate, String endDate) {
        if (uid == null || !userService.isUserExist(uid)) {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        logger.info("日报统计查询日期:" + startDate + " - " + endDate);

        // 判断传入日期是否正确
        if (startDate.matches("\\d{4}-\\d{2}-\\d{2}") && endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            if (startDate.compareTo(endDate) > 0) {
                return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR, "起止时间设置错误");
            }
        } else {
            return JsonUtils.getJsonResult(ResultCode.RESULT_PARAMS_ERROR);
        }

        try {
            User user = userService.getUser(uid);
            List<Integer> childUserIdsList = userService.getAllSupervisorIdsByAreaId(user.getArea());

            List<UserReportCount> reportCountList = reportService.getReportCountBetween(childUserIdsList, startDate, endDate);
            return JsonUtils.getJsonResult(ResultCode.RESULT_OK, reportCountList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.getJsonResult(ResultCode.RESULT_FAILED, "获取统计信息失败");
    }

}
