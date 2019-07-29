package com.mobileserver.domain;

public class Zambia {
    /*赞id*/
    private int zambiaId;
    public int getZambiaId() {
        return zambiaId;
    }
    public void setZambiaId(int zambiaId) {
        this.zambiaId = zambiaId;
    }

    /*被赞新闻*/
    private int newsObj;
    public int getNewsObj() {
        return newsObj;
    }
    public void setNewsObj(int newsObj) {
        this.newsObj = newsObj;
    }

    /*用户*/
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
        this.userObj = userObj;
    }

    /*被赞时间*/
    private String zambiaTime;
    public String getZambiaTime() {
        return zambiaTime;
    }
    public void setZambiaTime(String zambiaTime) {
        this.zambiaTime = zambiaTime;
    }

}