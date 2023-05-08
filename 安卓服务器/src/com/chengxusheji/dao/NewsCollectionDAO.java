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
import com.chengxusheji.domain.NewsCollection;

@Service @Transactional
public class NewsCollectionDAO {

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
    public void AddNewsCollection(NewsCollection newsCollection) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(newsCollection);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<NewsCollection> QueryNewsCollectionInfo(News newsObj,UserInfo userObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From NewsCollection newsCollection where 1=1";
    	if(null != newsObj && newsObj.getNewsId()!=0) hql += " and newsCollection.newsObj.newsId=" + newsObj.getNewsId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and newsCollection.userObj.user_name='" + userObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List newsCollectionList = q.list();
    	return (ArrayList<NewsCollection>) newsCollectionList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<NewsCollection> QueryNewsCollectionInfo(News newsObj,UserInfo userObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From NewsCollection newsCollection where 1=1";
    	if(null != newsObj && newsObj.getNewsId()!=0) hql += " and newsCollection.newsObj.newsId=" + newsObj.getNewsId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and newsCollection.userObj.user_name='" + userObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	List newsCollectionList = q.list();
    	return (ArrayList<NewsCollection>) newsCollectionList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<NewsCollection> QueryAllNewsCollectionInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From NewsCollection";
        Query q = s.createQuery(hql);
        List newsCollectionList = q.list();
        return (ArrayList<NewsCollection>) newsCollectionList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(News newsObj,UserInfo userObj) {
        Session s = factory.getCurrentSession();
        String hql = "From NewsCollection newsCollection where 1=1";
        if(null != newsObj && newsObj.getNewsId()!=0) hql += " and newsCollection.newsObj.newsId=" + newsObj.getNewsId();
        if(null != userObj && !userObj.getUser_name().equals("")) hql += " and newsCollection.userObj.user_name='" + userObj.getUser_name() + "'";
        Query q = s.createQuery(hql);
        List newsCollectionList = q.list();
        recordNumber = newsCollectionList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public NewsCollection GetNewsCollectionByCollectionId(int collectionId) {
        Session s = factory.getCurrentSession();
        NewsCollection newsCollection = (NewsCollection)s.get(NewsCollection.class, collectionId);
        return newsCollection;
    }

    /*����NewsCollection��Ϣ*/
    public void UpdateNewsCollection(NewsCollection newsCollection) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(newsCollection);
    }

    /*ɾ��NewsCollection��Ϣ*/
    public void DeleteNewsCollection (int collectionId) throws Exception {
        Session s = factory.getCurrentSession();
        Object newsCollection = s.load(NewsCollection.class, collectionId);
        s.delete(newsCollection);
    }

}
