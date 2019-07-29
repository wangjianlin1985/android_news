package com.chengxusheji.domain;

import java.sql.Timestamp;
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
    private News newsObj;
    public News getNewsObj() {
        return newsObj;
    }
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }

    /*标记的用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
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