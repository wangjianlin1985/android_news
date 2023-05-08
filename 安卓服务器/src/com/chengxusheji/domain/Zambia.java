package com.chengxusheji.domain;

import java.sql.Timestamp;
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
    private News newsObj;
    public News getNewsObj() {
        return newsObj;
    }
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }

    /*用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
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