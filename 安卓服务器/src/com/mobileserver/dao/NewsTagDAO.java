package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.NewsTag;
import com.mobileserver.util.DB;

public class NewsTagDAO {

	public List<NewsTag> QueryNewsTag(int newsObj,String userObj) {
		List<NewsTag> newsTagList = new ArrayList<NewsTag>();
		DB db = new DB();
		String sql = "select * from NewsTag where 1=1";
		if (newsObj != 0)
			sql += " and newsObj=" + newsObj;
		if (!userObj.equals(""))
			sql += " and userObj = '" + userObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				NewsTag newsTag = new NewsTag();
				newsTag.setTagId(rs.getInt("tagId"));
				newsTag.setNewsObj(rs.getInt("newsObj"));
				newsTag.setUserObj(rs.getString("userObj"));
				newsTag.setNewsState(rs.getInt("newsState"));
				newsTag.setTagTime(rs.getString("tagTime"));
				newsTagList.add(newsTag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return newsTagList;
	}
	/* �������ű�Ƕ��󣬽������ű�ǵ����ҵ�� */
	public String AddNewsTag(NewsTag newsTag) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в��������ű�� */
			String sqlString = "insert into NewsTag(newsObj,userObj,newsState,tagTime) values (";
			sqlString += newsTag.getNewsObj() + ",";
			sqlString += "'" + newsTag.getUserObj() + "',";
			sqlString += newsTag.getNewsState() + ",";
			sqlString += "'" + newsTag.getTagTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "���ű����ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "���ű�����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ�����ű�� */
	public String DeleteNewsTag(int tagId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from NewsTag where tagId=" + tagId;
			db.executeUpdate(sqlString);
			result = "���ű��ɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "���ű��ɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݱ��id��ȡ�����ű�� */
	public NewsTag GetNewsTag(int tagId) {
		NewsTag newsTag = null;
		DB db = new DB();
		String sql = "select * from NewsTag where tagId=" + tagId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				newsTag = new NewsTag();
				newsTag.setTagId(rs.getInt("tagId"));
				newsTag.setNewsObj(rs.getInt("newsObj"));
				newsTag.setUserObj(rs.getString("userObj"));
				newsTag.setNewsState(rs.getInt("newsState"));
				newsTag.setTagTime(rs.getString("tagTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return newsTag;
	}
	/* �������ű�� */
	public String UpdateNewsTag(NewsTag newsTag) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update NewsTag set ";
			sql += "newsObj=" + newsTag.getNewsObj() + ",";
			sql += "userObj='" + newsTag.getUserObj() + "',";
			sql += "newsState=" + newsTag.getNewsState() + ",";
			sql += "tagTime='" + newsTag.getTagTime() + "'";
			sql += " where tagId=" + newsTag.getTagId();
			db.executeUpdate(sql);
			result = "���ű�Ǹ��³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "���ű�Ǹ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
