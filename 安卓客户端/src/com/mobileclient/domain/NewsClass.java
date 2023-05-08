package com.mobileclient.domain;

import java.io.Serializable;

public class NewsClass implements Serializable {
    /*分类id*/
    private int newsClassId;
    public int getNewsClassId() {
        return newsClassId;
    }
    public void setNewsClassId(int newsClassId) {
        this.newsClassId = newsClassId;
    }

    /*分类名称*/
    private String newsClassName;
    public String getNewsClassName() {
        return newsClassName;
    }
    public void setNewsClassName(String newsClassName) {
        this.newsClassName = newsClassName;
    }

}