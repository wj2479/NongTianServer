package com.wj.nongtian.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wj.nongtian.ResultCode;

public class JsonUtils {

    public static String getJsonResult(ResultCode code) {
        return getJsonResult(code, "");
    }

    /**
     * 获取请求的返回结果
     *
     * @param code
     * @param data
     * @return
     */
    public static String getJsonResult(ResultCode code, Object data) {
        return getJsonResult(code, code.getDesc(), data);
    }

    /**
     * 获取请求的返回结果
     *
     * @param code
     * @param data
     * @return
     */
    public static String getJsonResult(ResultCode code, String message, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code.getType());
        jsonObject.put("msg", message);
        jsonObject.put("result", JSON.toJSON(data));
        return jsonObject.toJSONString();
    }
}
