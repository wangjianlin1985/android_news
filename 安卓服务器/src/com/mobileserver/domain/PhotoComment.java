package com.mobileserver.domain;

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
    private int photoObj;
    public int getPhotoObj() {
        return photoObj;
    }
    public void setPhotoObj(int photoObj) {
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
    private String userInfoObj;
    public String getUserInfoObj() {
        return userInfoObj;
    }
    public void setUserInfoObj(String userInfoObj) {
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