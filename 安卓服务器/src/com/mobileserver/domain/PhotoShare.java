package com.mobileserver.domain;

public class PhotoShare {
    /*����id*/
    private int sharePhotoId;
    public int getSharePhotoId() {
        return sharePhotoId;
    }
    public void setSharePhotoId(int sharePhotoId) {
        this.sharePhotoId = sharePhotoId;
    }

    /*ͼƬ����*/
    private String photoTitle;
    public String getPhotoTitle() {
        return photoTitle;
    }
    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }

    /*ͼƬ*/
    private String sharePhoto;
    public String getSharePhoto() {
        return sharePhoto;
    }
    public void setSharePhoto(String sharePhoto) {
        this.sharePhoto = sharePhoto;
    }

    /*�ϴ��û�*/
    private String userInfoObj;
    public String getUserInfoObj() {
        return userInfoObj;
    }
    public void setUserInfoObj(String userInfoObj) {
        this.userInfoObj = userInfoObj;
    }

    /*����ʱ��*/
    private String shareTime;
    public String getShareTime() {
        return shareTime;
    }
    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

}