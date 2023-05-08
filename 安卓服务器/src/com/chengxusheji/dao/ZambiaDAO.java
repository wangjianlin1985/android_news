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
import com.chengxusheji.domain.Zambia;

@Service @Transactional
public class ZambiaDAO {

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
    public void AddZambia(Zambia zambia) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(zambia);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Zambia> QueryZambiaInfo(News newsObj,UserInfo userObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Zambia zambia where 1=1";
    	if(null != newsObj && newsObj.getNewsId()!=0) hql += " and zambia.newsObj.newsId=" + newsObj.getNewsId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and zambia.userObj.user_name='" + userObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List zambiaList = q.list();
    	return (ArrayList<Zambia>) zambiaList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Zambia> QueryZambiaInfo(News newsObj,UserInfo userObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Zambia zambia where 1=1";
    	if(null != newsObj && newsObj.getNewsId()!=0) hql += " and zambia.newsObj.newsId=" + newsObj.getNewsId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and zambia.userObj.user_name='" + userObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	List zambiaList = q.list();
    	return (ArrayList<Zambia>) zambiaList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Zambia> QueryAllZambiaInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Zambia";
        Query q = s.createQuery(hql);
        List zambiaList = q.list();
        return (ArrayList<Zambia>) zambiaList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(News newsObj,UserInfo userObj) {
        Session s = factory.getCurrentSession();
        String hql = "From Zambia zambia where 1=1";
        if(null != newsObj && newsObj.getNewsId()!=0) hql += " and zambia.newsObj.newsId=" + newsObj.getNewsId();
        if(null != userObj && !userObj.getUser_name().equals("")) hql += " and zambia.userObj.user_name='" + userObj.getUser_name() + "'";
        Query q = s.createQuery(hql);
        List zambiaList = q.list();
        recordNumber = zambiaList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Zambia GetZambiaByZambiaId(int zambiaId) {
        Session s = factory.getCurrentSession();
        Zambia zambia = (Zambia)s.get(Zambia.class, zambiaId);
        return zambia;
    }

    /*����Zambia��Ϣ*/
    public void UpdateZambia(Zambia zambia) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(zambia);
    }

    /*ɾ��Zambia��Ϣ*/
    public void DeleteZambia (int zambiaId) throws Exception {
        Session s = factory.getCurrentSession();
        Object zambia = s.load(Zambia.class, zambiaId);
        s.delete(zambia);
    }

}
