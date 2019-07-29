package com.mobileserver.domain;

public class PhotoShare {
    /*分享id*/
    private int sharePhotoId;
    public int getSharePhotoId() {
        return sharePhotoId;
    }
    public void setSharePhotoId(int sharePhotoId) {
        this.sharePhotoId = sharePhotoId;
    }

    /*图片标题*/
    private String photoTitle;
    public String getPhotoTitle() {
        return photoTitle;
    }
    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }

    /*图片*/
    private String sharePhoto;
    public String getSharePhoto() {
        return sharePhoto;
    }
    public void setSharePhoto(String sharePhoto) {
        this.sharePhoto = sharePhoto;
    }

    /*上传用户*/
    private String userInfoObj;
    public String getUserInfoObj() {
        return userInfoObj;
    }
    public void setUserInfoObj(String userInfoObj) {
        this.userInfoObj = userInfoObj;
    }

    /*分享时间*/
    private String shareTime;
    public String getShareTime() {
        return shareTime;
    }
    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

}