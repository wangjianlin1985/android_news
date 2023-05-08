package com.mobileserver.domain;

public class VideoShare {
    /*分享id*/
    private int videoShareId;
    public int getVideoShareId() {
        return videoShareId;
    }
    public void setVideoShareId(int videoShareId) {
        this.videoShareId = videoShareId;
    }

    /*视频标题*/
    private String videoTitle;
    public String getVideoTitle() {
        return videoTitle;
    }
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    /*视频文件*/
    private String videoFile;
    public String getVideoFile() {
        return videoFile;
    }
    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    /*用户*/
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
        this.userObj = userObj;
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