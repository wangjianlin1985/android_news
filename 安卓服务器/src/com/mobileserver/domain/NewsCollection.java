package com.mobileserver.domain;

public class NewsCollection {
    /*收藏id*/
    private int collectionId;
    public int getCollectionId() {
        return collectionId;
    }
    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    /*被收藏新闻*/
    private int newsObj;
    public int getNewsObj() {
        return newsObj;
    }
    public void setNewsObj(int newsObj) {
        this.newsObj = newsObj;
    }

    /*收藏人*/
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
        this.userObj = userObj;
    }

    /*收藏时间*/
    private String collectTime;
    public String getCollectTime() {
        return collectTime;
    }
    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

}