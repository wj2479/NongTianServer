package com.wj.nongtian;

/**
 * 请求结果的异常
 */
public enum ResultCode {

    RESULT_OK("1000", "请求结果成功"),
    RESULT_FAILED("1001", "请求结果失败"),

    RESULT_USERNAME_EMPTY("2001", "请求用户名为空"),
    RESULT_PASSWORD_EMPTY("2002", "请求密码为空"),
    RESULT_USERNAME_NOT_FOUND("2003", "用户不存在"),
    RESULT_LOGIN_FAILED("2004", "登录失败"),

    RESULT_PROJECT_NOT_FOUND("3001", "没有匹配的项目"),

    RESULT_PARAMS_ERROR("4001", "参数不合法");

    private String type;    //类型
    private String desc;    //描述

    ResultCode(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
