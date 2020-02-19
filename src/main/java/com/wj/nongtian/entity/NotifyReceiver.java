package com.wj.nongtian.entity;

/**
 * 通知阅读对象
 */
public class NotifyReceiver extends Notify {

    /**
     * 是否已读
     */
    private boolean isRead = false;

    /**
     * 阅读时间
     */
    private String readTime = "";

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }
}
