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
	/* 传入新闻评论对象，进行新闻评论的添加业务 */
	public String AddNewsComment(NewsComment newsComment) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新新闻评论 */
			String sqlString = "insert into NewsComment(newsObj,userObj,content,commentTime) values (";
			sqlString += newsComment.getNewsObj() + ",";
			sqlString += "'" + newsComment.getUserObj() + "',";
			sqlString += "'" + newsComment.getContent() + "',";
			sqlString += "'" + newsComment.getCommentTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "新闻评论添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻评论添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除新闻评论 */
	public String DeleteNewsComment(int commentId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from NewsComment where commentId=" + commentId;
			db.executeUpdate(sqlString);
			result = "新闻评论删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻评论删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据评论id获取到新闻评论 */
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
	/* 更新新闻评论 */
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
			result = "新闻评论更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻评论更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
