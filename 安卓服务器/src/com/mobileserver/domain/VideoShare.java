package com.mobileserver.domain;

public class VideoShare {
    /*����id*/
    private int videoShareId;
    public int getVideoShareId() {
        return videoShareId;
    }
    public void setVideoShareId(int videoShareId) {
        this.videoShareId = videoShareId;
    }

    /*��Ƶ����*/
    private String videoTitle;
    public String getVideoTitle() {
        return videoTitle;
    }
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    /*��Ƶ�ļ�*/
    private String videoFile;
    public String getVideoFile() {
        return videoFile;
    }
    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    /*�û�*/
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
        this.userObj = userObj;
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