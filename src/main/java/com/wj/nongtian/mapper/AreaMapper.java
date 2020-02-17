package com.wj.nongtian.mapper;

import com.wj.nongtian.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaMapper {

    /**
     * 根据区域ID获取 区域对象
     *
     * @param id
     * @return
     */
    Area getAreaById(@Param("id") int id);

    /**
     * 获取下级的地区列表
     *
     * @param parentId
     * @return
     */
    List<Area> getSubAreaById(@Param("pid") int parentId);
}
