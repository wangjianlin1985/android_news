package com.mobileclient.domain;

import java.io.Serializable;

public class News implements Serializable {
    /*新闻id*/
    private int newsId;
    public int getNewsId() {
        return newsId;
    }
    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    /*新闻类别*/
    private int newsClassObj;
    public int getNewsClassObj() {
        return newsClassObj;
    }
    public void setNewsClassObj(int newsClassObj) {
        this.newsClassObj = newsClassObj;
    }

    /*新闻标题*/
    private String newsTitle;
    public String getNewsTitle() {
        return newsTitle;
    }
    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    /*新闻图片*/
    private String newsPhoto;
    public String getNewsPhoto() {
        return newsPhoto;
    }
    public void setNewsPhoto(String newsPhoto) {
        this.newsPhoto = newsPhoto;
    }

    /*新闻内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*新闻来源*/
    private String comFrom;
    public String getComFrom() {
        return comFrom;
    }
    public void setComFrom(String comFrom) {
        this.comFrom = comFrom;
    }

    /*浏览次数*/
    private int hitNum;
    public int getHitNum() {
        return hitNum;
    }
    public void setHitNum(int hitNum) {
        this.hitNum = hitNum;
    }

    /*添加时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

}