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
	/* 传入新闻信息对象，进行新闻信息的添加业务 */
	public String AddNews(News news) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新新闻信息 */
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
			result = "新闻信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除新闻信息 */
	public String DeleteNews(int newsId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from News where newsId=" + newsId;
			db.executeUpdate(sqlString);
			result = "新闻信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据新闻id获取到新闻信息 */
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
	/* 更新新闻信息 */
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
			result = "新闻信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
