package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.NewsCollection;
import com.mobileserver.util.DB;

public class NewsCollectionDAO {

	public List<NewsCollection> QueryNewsCollection(int newsObj,String userObj) {
		List<NewsCollection> newsCollectionList = new ArrayList<NewsCollection>();
		DB db = new DB();
		String sql = "select * from NewsCollection where 1=1";
		if (newsObj != 0)
			sql += " and newsObj=" + newsObj;
		if (!userObj.equals(""))
			sql += " and userObj = '" + userObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				NewsCollection newsCollection = new NewsCollection();
				newsCollection.setCollectionId(rs.getInt("collectionId"));
				newsCollection.setNewsObj(rs.getInt("newsObj"));
				newsCollection.setUserObj(rs.getString("userObj"));
				newsCollection.setCollectTime(rs.getString("collectTime"));
				newsCollectionList.add(newsCollection);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return newsCollectionList;
	}
	/* 传入新闻收藏对象，进行新闻收藏的添加业务 */
	public String AddNewsCollection(NewsCollection newsCollection) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新新闻收藏 */
			String sqlString = "insert into NewsCollection(newsObj,userObj,collectTime) values (";
			sqlString += newsCollection.getNewsObj() + ",";
			sqlString += "'" + newsCollection.getUserObj() + "',";
			sqlString += "'" + newsCollection.getCollectTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "新闻收藏添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻收藏添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除新闻收藏 */
	public String DeleteNewsCollection(int collectionId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from NewsCollection where collectionId=" + collectionId;
			db.executeUpdate(sqlString);
			result = "新闻收藏删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻收藏删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据收藏id获取到新闻收藏 */
	public NewsCollection GetNewsCollection(int collectionId) {
		NewsCollection newsCollection = null;
		DB db = new DB();
		String sql = "select * from NewsCollection where collectionId=" + collectionId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				newsCollection = new NewsCollection();
				newsCollection.setCollectionId(rs.getInt("collectionId"));
				newsCollection.setNewsObj(rs.getInt("newsObj"));
				newsCollection.setUserObj(rs.getString("userObj"));
				newsCollection.setCollectTime(rs.getString("collectTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return newsCollection;
	}
	/* 更新新闻收藏 */
	public String UpdateNewsCollection(NewsCollection newsCollection) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update NewsCollection set ";
			sql += "newsObj=" + newsCollection.getNewsObj() + ",";
			sql += "userObj='" + newsCollection.getUserObj() + "',";
			sql += "collectTime='" + newsCollection.getCollectTime() + "'";
			sql += " where collectionId=" + newsCollection.getCollectionId();
			db.executeUpdate(sql);
			result = "新闻收藏更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻收藏更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
