package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.News;
import com.mobileserver.util.DB;

public class NewsDAO {

	public List<News> QueryNews(int newsClassObj,String newsTitle,String comFrom,String addTime) {
		List<News> newsList = new ArrayList<News>();
		DB db = new DB();
		String sql = "select * from News where 1=1";
		if (newsClassObj != 0)
			sql += " and newsClassObj=" + newsClassObj;
		if (!newsTitle.equals(""))
			sql += " and newsTitle like '%" + newsTitle + "%'";
		if (!comFrom.equals(""))
			sql += " and comFrom like '%" + comFrom + "%'";
		if (!addTime.equals(""))
			sql += " and addTime like '%" + addTime + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				News news = new News();
				news.setNewsId(rs.getInt("newsId"));
				news.setNewsClassObj(rs.getInt("newsClassObj"));
				news.setNewsTitle(rs.getString("newsTitle"));
				news.setNewsPhoto(rs.getString("newsPhoto"));
				news.setContent(rs.getString("content"));
				news.setComFrom(rs.getString("comFrom"));
				news.setHitNum(rs.getInt("hitNum"));
				news.setAddTime(rs.getString("addTime"));
				newsList.add(news);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return newsList;
	}
	/* ����������Ϣ���󣬽���������Ϣ�����ҵ�� */
	public String AddNews(News news) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�����������Ϣ */
			String sqlString = "insert into News(newsClassObj,newsTitle,newsPhoto,content,comFrom,hitNum,addTime) values (";
			sqlString += news.getNewsClassObj() + ",";
			sqlString += "'" + news.getNewsTitle() + "',";
			sqlString += "'" + news.getNewsPhoto() + "',";
			sqlString += "'" + news.getContent() + "',";
			sqlString += "'" + news.getComFrom() + "',";
			sqlString += news.getHitNum() + ",";
			sqlString += "'" + news.getAddTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "������Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��������Ϣ */
	public String DeleteNews(int newsId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from News where newsId=" + newsId;
			db.executeUpdate(sqlString);
			result = "������Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ��������id��ȡ��������Ϣ */
	public News GetNews(int newsId) {
		News news = null;
		DB db = new DB();
		String sql = "select * from News where newsId=" + newsId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				news = new News();
				news.setNewsId(rs.getInt("newsId"));
				news.setNewsClassObj(rs.getInt("newsClassObj"));
				news.setNewsTitle(rs.getString("newsTitle"));
				news.setNewsPhoto(rs.getString("newsPhoto"));
				news.setContent(rs.getString("content"));
				news.setComFrom(rs.getString("comFrom"));
				news.setHitNum(rs.getInt("hitNum"));
				news.setAddTime(rs.getString("addTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return news;
	}
	/* ����������Ϣ */
	public String UpdateNews(News news) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update News set ";
			sql += "newsClassObj=" + news.getNewsClassObj() + ",";
			sql += "newsTitle='" + news.getNewsTitle() + "',";
			sql += "newsPhoto='" + news.getNewsPhoto() + "',";
			sql += "content='" + news.getContent() + "',";
			sql += "comFrom='" + news.getComFrom() + "',";
			sql += "hitNum=" + news.getHitNum() + ",";
			sql += "addTime='" + news.getAddTime() + "'";
			sql += " where newsId=" + news.getNewsId();
			db.executeUpdate(sql);
			result = "������Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
