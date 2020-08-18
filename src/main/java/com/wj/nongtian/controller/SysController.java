package com.wj.nongtian.controller;

import com.alibaba.fastjson.JSONArray;
import com.wj.nongtian.ResultCode;
import com.wj.nongtian.service.AreaService;
import com.wj.nongtian.utils.JsonUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class SysController {
    private final AreaService areaService;

    public SysController(AreaService areaService) {
        this.areaService = areaService;
    }

    @RequestMapping(value = "/getAreaList", method = RequestMethod.GET)
    public String getAreaList() {
        JSONArray jsonArray = areaService.getAreaList();
        return JsonUtils.getJsonResult(ResultCode.RESULT_OK, jsonArray);
    }
}
