package com.chengxusheji.domain;

import java.sql.Timestamp;
public class NewsTag {
    /*���id*/
    private int tagId;
    public int getTagId() {
        return tagId;
    }
    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    /*���������*/
    private News newsObj;
    public News getNewsObj() {
        return newsObj;
    }
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }

    /*��ǵ��û�*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*����״̬*/
    private int newsState;
    public int getNewsState() {
        return newsState;
    }
    public void setNewsState(int newsState) {
        this.newsState = newsState;
    }

    /*���ʱ��*/
    private String tagTime;
    public String getTagTime() {
        return tagTime;
    }
    public void setTagTime(String tagTime) {
        this.tagTime = tagTime;
    }

}