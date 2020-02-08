package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.Area;

public interface AreaMapper {

    /**
     * 根据区域ID获取 区域对象
     *
     * @param id
     * @return
     */
    Area getAreaById(int id);
}
