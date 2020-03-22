package com.wj.nongtian.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 日报评论对象
 */
public class ReportComment {
    /**
     * 日报的ID
     */
    private int id;

    /**
     * 日报ID
     */
    private int rid;

    /**
     * 用户ID
     */
    private int sender;

    /**
     * 评论内容
     */
    private String content = "";

    /**
     * 创建时间
     */
    private String createTime = "";

    /**
     * 评论的多媒体信息
     */
    private List<ResponseMedia> mediaList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ResponseMedia> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<ResponseMedia> mediaList) {
        this.mediaList = mediaList;
    }
}
