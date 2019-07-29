package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Huodong {
    /*活动id*/
    private int huodongId;
    public int getHuodongId() {
        return huodongId;
    }
    public void setHuodongId(int huodongId) {
        this.huodongId = huodongId;
    }

    /*活动主题 */
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*活动内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*报名电话*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*参与名单*/
    private String personList;
    public String getPersonList() {
        return personList;
    }
    public void setPersonList(String personList) {
        this.personList = personList;
    }

    /*发起人*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*发布时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

}