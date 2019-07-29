package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.domain.VideoShare;

@Service @Transactional
public class VideoShareDAO {

	@Resource SessionFactory factory;
    /*每页显示记录数目*/
    private final int PAGE_SIZE = 10;

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加图书信息*/
    public void AddVideoShare(VideoShare videoShare) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(videoShare);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<VideoShare> QueryVideoShareInfo(String videoTitle,UserInfo userObj,String shareTime,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From VideoShare videoShare where 1=1";
    	if(!videoTitle.equals("")) hql = hql + " and videoShare.videoTitle like '%" + videoTitle + "%'";
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and videoShare.userObj.user_name='" + userObj.getUser_name() + "'";
    	if(!shareTime.equals("")) hql = hql + " and videoShare.shareTime like '%" + shareTime + "%'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List videoShareList = q.list();
    	return (ArrayList<VideoShare>) videoShareList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<VideoShare> QueryVideoShareInfo(String videoTitle,UserInfo userObj,String shareTime) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From VideoShare videoShare where 1=1";
    	if(!videoTitle.equals("")) hql = hql + " and videoShare.videoTitle like '%" + videoTitle + "%'";
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and videoShare.userObj.user_name='" + userObj.getUser_name() + "'";
    	if(!shareTime.equals("")) hql = hql + " and videoShare.shareTime like '%" + shareTime + "%'";
    	Query q = s.createQuery(hql);
    	List videoShareList = q.list();
    	return (ArrayList<VideoShare>) videoShareList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<VideoShare> QueryAllVideoShareInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From VideoShare";
        Query q = s.createQuery(hql);
        List videoShareList = q.list();
        return (ArrayList<VideoShare>) videoShareList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String videoTitle,UserInfo userObj,String shareTime) {
        Session s = factory.getCurrentSession();
        String hql = "From VideoShare videoShare where 1=1";
        if(!videoTitle.equals("")) hql = hql + " and videoShare.videoTitle like '%" + videoTitle + "%'";
        if(null != userObj && !userObj.getUser_name().equals("")) hql += " and videoShare.userObj.user_name='" + userObj.getUser_name() + "'";
        if(!shareTime.equals("")) hql = hql + " and videoShare.shareTime like '%" + shareTime + "%'";
        Query q = s.createQuery(hql);
        List videoShareList = q.list();
        recordNumber = videoShareList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public VideoShare GetVideoShareByVideoShareId(int videoShareId) {
        Session s = factory.getCurrentSession();
        VideoShare videoShare = (VideoShare)s.get(VideoShare.class, videoShareId);
        return videoShare;
    }

    /*更新VideoShare信息*/
    public void UpdateVideoShare(VideoShare videoShare) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(videoShare);
    }

    /*删除VideoShare信息*/
    public void DeleteVideoShare (int videoShareId) throws Exception {
        Session s = factory.getCurrentSession();
        Object videoShare = s.load(VideoShare.class, videoShareId);
        s.delete(videoShare);
    }

}
