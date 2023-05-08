package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.PhotoComment;
import com.mobileserver.util.DB;

public class PhotoCommentDAO {

	public List<PhotoComment> QueryPhotoComment(int photoObj,String content,String userInfoObj) {
		List<PhotoComment> photoCommentList = new ArrayList<PhotoComment>();
		DB db = new DB();
		String sql = "select * from PhotoComment where 1=1";
		if (photoObj != 0)
			sql += " and photoObj=" + photoObj;
		if (!content.equals(""))
			sql += " and content like '%" + content + "%'";
		if (!userInfoObj.equals(""))
			sql += " and userInfoObj = '" + userInfoObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				PhotoComment photoComment = new PhotoComment();
				photoComment.setPhotoCommentId(rs.getInt("photoCommentId"));
				photoComment.setPhotoObj(rs.getInt("photoObj"));
				photoComment.setContent(rs.getString("content"));
				photoComment.setUserInfoObj(rs.getString("userInfoObj"));
				photoComment.setCommentTime(rs.getString("commentTime"));
				photoCommentList.add(photoComment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return photoCommentList;
	}
	/* 传入图片评论对象，进行图片评论的添加业务 */
	public String AddPhotoComment(PhotoComment photoComment) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新图片评论 */
			String sqlString = "insert into PhotoComment(photoObj,content,userInfoObj,commentTime) values (";
			sqlString += photoComment.getPhotoObj() + ",";
			sqlString += "'" + photoComment.getContent() + "',";
			sqlString += "'" + photoComment.getUserInfoObj() + "',";
			sqlString += "'" + photoComment.getCommentTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "图片评论添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "图片评论添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除图片评论 */
	public String DeletePhotoComment(int photoCommentId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from PhotoComment where photoCommentId=" + photoCommentId;
			db.executeUpdate(sqlString);
			result = "图片评论删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "图片评论删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据评论id获取到图片评论 */
	public PhotoComment GetPhotoComment(int photoCommentId) {
		PhotoComment photoComment = null;
		DB db = new DB();
		String sql = "select * from PhotoComment where photoCommentId=" + photoCommentId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				photoComment = new PhotoComment();
				photoComment.setPhotoCommentId(rs.getInt("photoCommentId"));
				photoComment.setPhotoObj(rs.getInt("photoObj"));
				photoComment.setContent(rs.getString("content"));
				photoComment.setUserInfoObj(rs.getString("userInfoObj"));
				photoComment.setCommentTime(rs.getString("commentTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return photoComment;
	}
	/* 更新图片评论 */
	public String UpdatePhotoComment(PhotoComment photoComment) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update PhotoComment set ";
			sql += "photoObj=" + photoComment.getPhotoObj() + ",";
			sql += "content='" + photoComment.getContent() + "',";
			sql += "userInfoObj='" + photoComment.getUserInfoObj() + "',";
			sql += "commentTime='" + photoComment.getCommentTime() + "'";
			sql += " where photoCommentId=" + photoComment.getPhotoCommentId();
			db.executeUpdate(sql);
			result = "图片评论更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "图片评论更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
