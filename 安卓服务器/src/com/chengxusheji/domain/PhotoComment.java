package com.chengxusheji.domain;

import java.sql.Timestamp;
public class PhotoComment {
    /*评论id*/
    private int photoCommentId;
    public int getPhotoCommentId() {
        return photoCommentId;
    }
    public void setPhotoCommentId(int photoCommentId) {
        this.photoCommentId = photoCommentId;
    }

    /*被评图片*/
    private PhotoShare photoObj;
    public PhotoShare getPhotoObj() {
        return photoObj;
    }
    public void setPhotoObj(PhotoShare photoObj) {
        this.photoObj = photoObj;
    }

    /*评论内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*用户*/
    private UserInfo userInfoObj;
    public UserInfo getUserInfoObj() {
        return userInfoObj;
    }
    public void setUserInfoObj(UserInfo userInfoObj) {
        this.userInfoObj = userInfoObj;
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