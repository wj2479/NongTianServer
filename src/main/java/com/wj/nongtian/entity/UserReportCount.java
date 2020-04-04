package com.wj.nongtian.entity;

/**
 * 用户日报统计对象
 */
public class UserReportCount {

    /**
     * 用户ID
     */
    int uid;
    /**
     * 用户昵称
     */
    String nickName;
    /**
     * 日报数量
     */
    int count;
    /**
     * 照片视频总数量
     */
    int sum;
    /**
     * 日期对象
     */
    String date;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
