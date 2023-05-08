package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.PhotoShare;
import com.mobileserver.util.DB;

public class PhotoShareDAO {

	public List<PhotoShare> QueryPhotoShare(String userInfoObj,String shareTime) {
		List<PhotoShare> photoShareList = new ArrayList<PhotoShare>();
		DB db = new DB();
		String sql = "select * from PhotoShare where 1=1";
		if (!userInfoObj.equals(""))
			sql += " and userInfoObj = '" + userInfoObj + "'";
		if (!shareTime.equals(""))
			sql += " and shareTime like '%" + shareTime + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				PhotoShare photoShare = new PhotoShare();
				photoShare.setSharePhotoId(rs.getInt("sharePhotoId"));
				photoShare.setPhotoTitle(rs.getString("photoTitle"));
				photoShare.setSharePhoto(rs.getString("sharePhoto"));
				photoShare.setUserInfoObj(rs.getString("userInfoObj"));
				photoShare.setShareTime(rs.getString("shareTime"));
				photoShareList.add(photoShare);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return photoShareList;
	}
	/* 传入图片分享对象，进行图片分享的添加业务 */
	public String AddPhotoShare(PhotoShare photoShare) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新图片分享 */
			String sqlString = "insert into PhotoShare(photoTitle,sharePhoto,userInfoObj,shareTime) values (";
			sqlString += "'" + photoShare.getPhotoTitle() + "',";
			sqlString += "'" + photoShare.getSharePhoto() + "',";
			sqlString += "'" + photoShare.getUserInfoObj() + "',";
			sqlString += "'" + photoShare.getShareTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "图片分享添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "图片分享添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除图片分享 */
	public String DeletePhotoShare(int sharePhotoId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from PhotoShare where sharePhotoId=" + sharePhotoId;
			db.executeUpdate(sqlString);
			result = "图片分享删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "图片分享删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据分享id获取到图片分享 */
	public PhotoShare GetPhotoShare(int sharePhotoId) {
		PhotoShare photoShare = null;
		DB db = new DB();
		String sql = "select * from PhotoShare where sharePhotoId=" + sharePhotoId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				photoShare = new PhotoShare();
				photoShare.setSharePhotoId(rs.getInt("sharePhotoId"));
				photoShare.setPhotoTitle(rs.getString("photoTitle"));
				photoShare.setSharePhoto(rs.getString("sharePhoto"));
				photoShare.setUserInfoObj(rs.getString("userInfoObj"));
				photoShare.setShareTime(rs.getString("shareTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return photoShare;
	}
	/* 更新图片分享 */
	public String UpdatePhotoShare(PhotoShare photoShare) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update PhotoShare set ";
			sql += "photoTitle='" + photoShare.getPhotoTitle() + "',";
			sql += "sharePhoto='" + photoShare.getSharePhoto() + "',";
			sql += "userInfoObj='" + photoShare.getUserInfoObj() + "',";
			sql += "shareTime='" + photoShare.getShareTime() + "'";
			sql += " where sharePhotoId=" + photoShare.getSharePhotoId();
			db.executeUpdate(sql);
			result = "图片分享更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "图片分享更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
