package com.wj.nongtian.service;

import com.wj.nongtian.entity.Area;
import com.wj.nongtian.mapper.AreaMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AreaService {

    @Resource
    AreaMapper areaMapper;

    /**
     * 递归获取上级的列表
     *
     * @param area
     */
    public void initParentAreas(Area area) {
        if (area != null && area.getParentId() > 0) {
            Area pArea = areaMapper.getAreaById(area.getParentId());
            area.setParent(pArea);
            initParentAreas(pArea);
        }
    }
}