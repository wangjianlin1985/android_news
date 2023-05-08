package com.chengxusheji.domain;

import java.sql.Timestamp;
public class NewsComment {
    /*评论id*/
    private int commentId;
    public int getCommentId() {
        return commentId;
    }
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    /*被评新闻*/
    private News newsObj;
    public News getNewsObj() {
        return newsObj;
    }
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }

    /*评论人*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*评论内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*评论时间*/
    private String commentTime;
    public String getCommentTime() {
        return commentTime;
    }
    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

}