package com.mobileserver.domain;

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
    private int newsObj;
    public int getNewsObj() {
        return newsObj;
    }
    public void setNewsObj(int newsObj) {
        this.newsObj = newsObj;
    }

    /*��ǵ��û�*/
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
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