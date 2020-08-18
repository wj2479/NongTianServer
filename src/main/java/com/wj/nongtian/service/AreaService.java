package com.wj.nongtian.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wj.nongtian.entity.Area;
import com.wj.nongtian.mapper.AreaMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AreaService {

    @Resource
    AreaMapper areaMapper;

    /**
     * 根据区域ID获取区域
     *
     * @param id
     * @return
     */
    public Area getAreaById(int id) {
        return areaMapper.getAreaById(id);
    }

    /**
     * 递归获取上级的列表
     *
     * @param area
     */
    public void initParentAreas(Area area) {
        if (area != null && area.getParentId() > 0) {
            Area pArea = getAreaById(area.getParentId());
            area.setParent(pArea);
            initParentAreas(pArea);
        }
    }

    /**
     * 获取下级的地区列表
     *
     * @return
     */
    public List<Area> getSubAreaById(int parentId) {
        return areaMapper.getSubAreaById(parentId);
    }

    /**
     * 获取地区的级联列表Json
     *
     * @return
     */
    public JSONArray getAreaList() {
        // 1. 获取省级列表
        List<Area> provinceAreaList = areaMapper.getSubAreaById(0);
        JSONArray provinceArray = new JSONArray();
        for (Area provinceArea : provinceAreaList) {
            JSONObject provinceJson = new JSONObject();
            provinceJson.put("id", provinceArea.getId());
            provinceJson.put("name", provinceArea.getName());

            // 2. 获取市级列表
            List<Area> cityAreaList = areaMapper.getSubAreaById(provinceArea.getId());
            JSONArray cityArray = new JSONArray();
            for (Area cityArea : cityAreaList) {
                JSONObject cityJson = new JSONObject();
                cityJson.put("id", cityArea.getId());
                cityJson.put("name", cityArea.getName());

                // 3. 获取区县列表
                List<Area> districtAreaList = areaMapper.getSubAreaById(cityArea.getId());
                JSONArray districtArray = new JSONArray();
                for (Area districtArea : districtAreaList) {
                    JSONObject districtJson = new JSONObject();
                    districtJson.put("id", districtArea.getId());
                    districtJson.put("name", districtArea.getName());

                    // 4. 获取乡镇列表
                    List<Area> villageAreaList = areaMapper.getSubAreaById(districtArea.getId());
                    JSONArray villageArray = new JSONArray();
                    for (Area villagearea : villageAreaList) {
                        JSONObject villageJson = new JSONObject();
                        villageJson.put("id", villagearea.getId());
                        villageJson.put("name", villagearea.getName());
                        villageArray.add(villageJson);
                    }
                    districtJson.put("children", villageArray);
                    districtArray.add(districtJson);
                }

                cityJson.put("children", districtArray);
                cityArray.add(cityJson);
            }
            provinceJson.put("children", cityArray);
            provinceArray.add(provinceJson);
        }
        return provinceArray;
    }
}
