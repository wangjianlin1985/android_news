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
import com.chengxusheji.domain.PhotoShare;

@Service @Transactional
public class PhotoShareDAO {

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
    public void AddPhotoShare(PhotoShare photoShare) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(photoShare);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<PhotoShare> QueryPhotoShareInfo(UserInfo userInfoObj,String shareTime,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From PhotoShare photoShare where 1=1";
    	if(null != userInfoObj && !userInfoObj.getUser_name().equals("")) hql += " and photoShare.userInfoObj.user_name='" + userInfoObj.getUser_name() + "'";
    	if(!shareTime.equals("")) hql = hql + " and photoShare.shareTime like '%" + shareTime + "%'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List photoShareList = q.list();
    	return (ArrayList<PhotoShare>) photoShareList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<PhotoShare> QueryPhotoShareInfo(UserInfo userInfoObj,String shareTime) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From PhotoShare photoShare where 1=1";
    	if(null != userInfoObj && !userInfoObj.getUser_name().equals("")) hql += " and photoShare.userInfoObj.user_name='" + userInfoObj.getUser_name() + "'";
    	if(!shareTime.equals("")) hql = hql + " and photoShare.shareTime like '%" + shareTime + "%'";
    	Query q = s.createQuery(hql);
    	List photoShareList = q.list();
    	return (ArrayList<PhotoShare>) photoShareList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<PhotoShare> QueryAllPhotoShareInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From PhotoShare";
        Query q = s.createQuery(hql);
        List photoShareList = q.list();
        return (ArrayList<PhotoShare>) photoShareList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(UserInfo userInfoObj,String shareTime) {
        Session s = factory.getCurrentSession();
        String hql = "From PhotoShare photoShare where 1=1";
        if(null != userInfoObj && !userInfoObj.getUser_name().equals("")) hql += " and photoShare.userInfoObj.user_name='" + userInfoObj.getUser_name() + "'";
        if(!shareTime.equals("")) hql = hql + " and photoShare.shareTime like '%" + shareTime + "%'";
        Query q = s.createQuery(hql);
        List photoShareList = q.list();
        recordNumber = photoShareList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public PhotoShare GetPhotoShareBySharePhotoId(int sharePhotoId) {
        Session s = factory.getCurrentSession();
        PhotoShare photoShare = (PhotoShare)s.get(PhotoShare.class, sharePhotoId);
        return photoShare;
    }

    /*更新PhotoShare信息*/
    public void UpdatePhotoShare(PhotoShare photoShare) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(photoShare);
    }

    /*删除PhotoShare信息*/
    public void DeletePhotoShare (int sharePhotoId) throws Exception {
        Session s = factory.getCurrentSession();
        Object photoShare = s.load(PhotoShare.class, sharePhotoId);
        s.delete(photoShare);
    }

}
