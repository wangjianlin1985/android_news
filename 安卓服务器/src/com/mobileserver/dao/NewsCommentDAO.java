package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.NewsComment;
import com.mobileserver.util.DB;

public class NewsCommentDAO {

	public List<NewsComment> QueryNewsComment(int newsObj,String userObj) {
		List<NewsComment> newsCommentList = new ArrayList<NewsComment>();
		DB db = new DB();
		String sql = "select * from NewsComment where 1=1";
		if (newsObj != 0)
			sql += " and newsObj=" + newsObj;
		if (!userObj.equals(""))
			sql += " and userObj = '" + userObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				NewsComment newsComment = new NewsComment();
				newsComment.setCommentId(rs.getInt("commentId"));
				newsComment.setNewsObj(rs.getInt("newsObj"));
				newsComment.setUserObj(rs.getString("userObj"));
				newsComment.setContent(rs.getString("content"));
				newsComment.setCommentTime(rs.getString("commentTime"));
				newsCommentList.add(newsComment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return newsCommentList;
	}
	/* �����������۶��󣬽����������۵����ҵ�� */
	public String AddNewsComment(NewsComment newsComment) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в������������� */
			String sqlString = "insert into NewsComment(newsObj,userObj,content,commentTime) values (";
			sqlString += newsComment.getNewsObj() + ",";
			sqlString += "'" + newsComment.getUserObj() + "',";
			sqlString += "'" + newsComment.getContent() + "',";
			sqlString += "'" + newsComment.getCommentTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "����������ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�����������ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ���������� */
	public String DeleteNewsComment(int commentId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from NewsComment where commentId=" + commentId;
			db.executeUpdate(sqlString);
			result = "��������ɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "��������ɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ��������id��ȡ���������� */
	public NewsComment GetNewsComment(int commentId) {
		NewsComment newsComment = null;
		DB db = new DB();
		String sql = "select * from NewsComment where commentId=" + commentId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				newsComment = new NewsComment();
				newsComment.setCommentId(rs.getInt("commentId"));
				newsComment.setNewsObj(rs.getInt("newsObj"));
				newsComment.setUserObj(rs.getString("userObj"));
				newsComment.setContent(rs.getString("content"));
				newsComment.setCommentTime(rs.getString("commentTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return newsComment;
	}
	/* ������������ */
	public String UpdateNewsComment(NewsComment newsComment) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update NewsComment set ";
			sql += "newsObj=" + newsComment.getNewsObj() + ",";
			sql += "userObj='" + newsComment.getUserObj() + "',";
			sql += "content='" + newsComment.getContent() + "',";
			sql += "commentTime='" + newsComment.getCommentTime() + "'";
			sql += " where commentId=" + newsComment.getCommentId();
			db.executeUpdate(sql);
			result = "�������۸��³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�������۸���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
