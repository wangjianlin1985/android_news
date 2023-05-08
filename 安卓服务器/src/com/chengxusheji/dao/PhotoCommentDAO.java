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
import com.chengxusheji.domain.PhotoShare;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.domain.PhotoComment;

@Service @Transactional
public class PhotoCommentDAO {

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
    public void AddPhotoComment(PhotoComment photoComment) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(photoComment);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<PhotoComment> QueryPhotoCommentInfo(PhotoShare photoObj,String content,UserInfo userInfoObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From PhotoComment photoComment where 1=1";
    	if(null != photoObj && photoObj.getSharePhotoId()!=0) hql += " and photoComment.photoObj.sharePhotoId=" + photoObj.getSharePhotoId();
    	if(!content.equals("")) hql = hql + " and photoComment.content like '%" + content + "%'";
    	if(null != userInfoObj && !userInfoObj.getUser_name().equals("")) hql += " and photoComment.userInfoObj.user_name='" + userInfoObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List photoCommentList = q.list();
    	return (ArrayList<PhotoComment>) photoCommentList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<PhotoComment> QueryPhotoCommentInfo(PhotoShare photoObj,String content,UserInfo userInfoObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From PhotoComment photoComment where 1=1";
    	if(null != photoObj && photoObj.getSharePhotoId()!=0) hql += " and photoComment.photoObj.sharePhotoId=" + photoObj.getSharePhotoId();
    	if(!content.equals("")) hql = hql + " and photoComment.content like '%" + content + "%'";
    	if(null != userInfoObj && !userInfoObj.getUser_name().equals("")) hql += " and photoComment.userInfoObj.user_name='" + userInfoObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	List photoCommentList = q.list();
    	return (ArrayList<PhotoComment>) photoCommentList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<PhotoComment> QueryAllPhotoCommentInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From PhotoComment";
        Query q = s.createQuery(hql);
        List photoCommentList = q.list();
        return (ArrayList<PhotoComment>) photoCommentList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(PhotoShare photoObj,String content,UserInfo userInfoObj) {
        Session s = factory.getCurrentSession();
        String hql = "From PhotoComment photoComment where 1=1";
        if(null != photoObj && photoObj.getSharePhotoId()!=0) hql += " and photoComment.photoObj.sharePhotoId=" + photoObj.getSharePhotoId();
        if(!content.equals("")) hql = hql + " and photoComment.content like '%" + content + "%'";
        if(null != userInfoObj && !userInfoObj.getUser_name().equals("")) hql += " and photoComment.userInfoObj.user_name='" + userInfoObj.getUser_name() + "'";
        Query q = s.createQuery(hql);
        List photoCommentList = q.list();
        recordNumber = photoCommentList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public PhotoComment GetPhotoCommentByPhotoCommentId(int photoCommentId) {
        Session s = factory.getCurrentSession();
        PhotoComment photoComment = (PhotoComment)s.get(PhotoComment.class, photoCommentId);
        return photoComment;
    }

    /*更新PhotoComment信息*/
    public void UpdatePhotoComment(PhotoComment photoComment) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(photoComment);
    }

    /*删除PhotoComment信息*/
    public void DeletePhotoComment (int photoCommentId) throws Exception {
        Session s = factory.getCurrentSession();
        Object photoComment = s.load(PhotoComment.class, photoCommentId);
        s.delete(photoComment);
    }

}
