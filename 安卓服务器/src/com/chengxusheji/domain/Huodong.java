package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Huodong {
    /*�id*/
    private int huodongId;
    public int getHuodongId() {
        return huodongId;
    }
    public void setHuodongId(int huodongId) {
        this.huodongId = huodongId;
    }

    /*����� */
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*�����*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*�����绰*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*��������*/
    private String personList;
    public String getPersonList() {
        return personList;
    }
    public void setPersonList(String personList) {
        this.personList = personList;
    }

    /*������*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*����ʱ��*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

}