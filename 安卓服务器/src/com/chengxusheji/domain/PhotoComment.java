package com.chengxusheji.domain;

import java.sql.Timestamp;
public class PhotoComment {
    /*����id*/
    private int photoCommentId;
    public int getPhotoCommentId() {
        return photoCommentId;
    }
    public void setPhotoCommentId(int photoCommentId) {
        this.photoCommentId = photoCommentId;
    }

    /*����ͼƬ*/
    private PhotoShare photoObj;
    public PhotoShare getPhotoObj() {
        return photoObj;
    }
    public void setPhotoObj(PhotoShare photoObj) {
        this.photoObj = photoObj;
    }

    /*��������*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*�û�*/
    private UserInfo userInfoObj;
    public UserInfo getUserInfoObj() {
        return userInfoObj;
    }
    public void setUserInfoObj(UserInfo userInfoObj) {
        this.userInfoObj = userInfoObj;
    }

    /*����ʱ��*/
    private String commentTime;
    public String getCommentTime() {
        return commentTime;
    }
    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

}