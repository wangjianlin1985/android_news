package com.mobileserver.domain;

public class News {
    /*����id*/
    private int newsId;
    public int getNewsId() {
        return newsId;
    }
    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    /*�������*/
    private int newsClassObj;
    public int getNewsClassObj() {
        return newsClassObj;
    }
    public void setNewsClassObj(int newsClassObj) {
        this.newsClassObj = newsClassObj;
    }

    /*���ű���*/
    private String newsTitle;
    public String getNewsTitle() {
        return newsTitle;
    }
    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    /*����ͼƬ*/
    private String newsPhoto;
    public String getNewsPhoto() {
        return newsPhoto;
    }
    public void setNewsPhoto(String newsPhoto) {
        this.newsPhoto = newsPhoto;
    }

    /*��������*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*������Դ*/
    private String comFrom;
    public String getComFrom() {
        return comFrom;
    }
    public void setComFrom(String comFrom) {
        this.comFrom = comFrom;
    }

    /*�������*/
    private int hitNum;
    public int getHitNum() {
        return hitNum;
    }
    public void setHitNum(int hitNum) {
        this.hitNum = hitNum;
    }

    /*���ʱ��*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

}