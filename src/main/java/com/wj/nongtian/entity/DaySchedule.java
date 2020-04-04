package com.wj.nongtian.entity;

/**
 * 日报汇总对象，记录每天日报的结果
 */
public class DaySchedule {

    /**
     * 日期
     */
    private String date;

    /**
     * 当前日期的最新进度
     */
    private int schedule = 0;

    /**
     * 当前日期的标题
     */
    private String title;

    /**
     * 最后一条媒体数据
     */
    private ResponseMedia lastMedia;

    /**
     * 更新时间
     */
    private String updateTime = "";

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSchedule() {
        return schedule;
    }

    public void setSchedule(int schedule) {
        this.schedule = schedule;
    }

    public ResponseMedia getLastMedia() {
        return lastMedia;
    }

    public void setLastMedia(ResponseMedia lastMedia) {
        this.lastMedia = lastMedia;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "DaySchedule{" +
                "date='" + date + '\'' +
                ", schedule=" + schedule +
                ", lastMedia=" + lastMedia +
                '}';
    }
}
