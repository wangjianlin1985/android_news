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
import com.chengxusheji.domain.News;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.domain.NewsTag;

@Service @Transactional
public class NewsTagDAO {

	@Resource SessionFactory factory;
    /*ÿҳ��ʾ��¼��Ŀ*/
    private final int PAGE_SIZE = 10;

    /*�����ѯ���ܵ�ҳ��*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*�����ѯ�����ܼ�¼��*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*���ͼ����Ϣ*/
    public void AddNewsTag(NewsTag newsTag) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(newsTag);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<NewsTag> QueryNewsTagInfo(News newsObj,UserInfo userObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From NewsTag newsTag where 1=1";
    	if(null != newsObj && newsObj.getNewsId()!=0) hql += " and newsTag.newsObj.newsId=" + newsObj.getNewsId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and newsTag.userObj.user_name='" + userObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List newsTagList = q.list();
    	return (ArrayList<NewsTag>) newsTagList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<NewsTag> QueryNewsTagInfo(News newsObj,UserInfo userObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From NewsTag newsTag where 1=1";
    	if(null != newsObj && newsObj.getNewsId()!=0) hql += " and newsTag.newsObj.newsId=" + newsObj.getNewsId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and newsTag.userObj.user_name='" + userObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	List newsTagList = q.list();
    	return (ArrayList<NewsTag>) newsTagList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<NewsTag> QueryAllNewsTagInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From NewsTag";
        Query q = s.createQuery(hql);
        List newsTagList = q.list();
        return (ArrayList<NewsTag>) newsTagList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(News newsObj,UserInfo userObj) {
        Session s = factory.getCurrentSession();
        String hql = "From NewsTag newsTag where 1=1";
        if(null != newsObj && newsObj.getNewsId()!=0) hql += " and newsTag.newsObj.newsId=" + newsObj.getNewsId();
        if(null != userObj && !userObj.getUser_name().equals("")) hql += " and newsTag.userObj.user_name='" + userObj.getUser_name() + "'";
        Query q = s.createQuery(hql);
        List newsTagList = q.list();
        recordNumber = newsTagList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public NewsTag GetNewsTagByTagId(int tagId) {
        Session s = factory.getCurrentSession();
        NewsTag newsTag = (NewsTag)s.get(NewsTag.class, tagId);
        return newsTag;
    }

    /*����NewsTag��Ϣ*/
    public void UpdateNewsTag(NewsTag newsTag) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(newsTag);
    }

    /*ɾ��NewsTag��Ϣ*/
    public void DeleteNewsTag (int tagId) throws Exception {
        Session s = factory.getCurrentSession();
        Object newsTag = s.load(NewsTag.class, tagId);
        s.delete(newsTag);
    }

}
