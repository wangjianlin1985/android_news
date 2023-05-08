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
	/* ����ͼƬ���۶��󣬽���ͼƬ���۵����ҵ�� */
	public String AddPhotoComment(PhotoComment photoComment) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�����ͼƬ���� */
			String sqlString = "insert into PhotoComment(photoObj,content,userInfoObj,commentTime) values (";
			sqlString += photoComment.getPhotoObj() + ",";
			sqlString += "'" + photoComment.getContent() + "',";
			sqlString += "'" + photoComment.getUserInfoObj() + "',";
			sqlString += "'" + photoComment.getCommentTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "ͼƬ������ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ͼƬ�������ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��ͼƬ���� */
	public String DeletePhotoComment(int photoCommentId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from PhotoComment where photoCommentId=" + photoCommentId;
			db.executeUpdate(sqlString);
			result = "ͼƬ����ɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ͼƬ����ɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ��������id��ȡ��ͼƬ���� */
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
	/* ����ͼƬ���� */
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
			result = "ͼƬ���۸��³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ͼƬ���۸���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
