package com.mobileserver.domain;

public class NewsTag {
    /*标记id*/
    private int tagId;
    public int getTagId() {
        return tagId;
    }
    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    /*被标记新闻*/
    private int newsObj;
    public int getNewsObj() {
        return newsObj;
    }
    public void setNewsObj(int newsObj) {
        this.newsObj = newsObj;
    }

    /*标记的用户*/
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
        this.userObj = userObj;
    }

    /*新闻状态*/
    private int newsState;
    public int getNewsState() {
        return newsState;
    }
    public void setNewsState(int newsState) {
        this.newsState = newsState;
    }

    /*标记时间*/
    private String tagTime;
    public String getTagTime() {
        return tagTime;
    }
    public void setTagTime(String tagTime) {
        this.tagTime = tagTime;
    }

}