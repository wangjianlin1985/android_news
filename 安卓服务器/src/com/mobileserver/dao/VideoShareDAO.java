package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.VideoShare;
import com.mobileserver.util.DB;

public class VideoShareDAO {

	public List<VideoShare> QueryVideoShare(String videoTitle,String userObj,String shareTime) {
		List<VideoShare> videoShareList = new ArrayList<VideoShare>();
		DB db = new DB();
		String sql = "select * from VideoShare where 1=1";
		if (!videoTitle.equals(""))
			sql += " and videoTitle like '%" + videoTitle + "%'";
		if (!userObj.equals(""))
			sql += " and userObj = '" + userObj + "'";
		if (!shareTime.equals(""))
			sql += " and shareTime like '%" + shareTime + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				VideoShare videoShare = new VideoShare();
				videoShare.setVideoShareId(rs.getInt("videoShareId"));
				videoShare.setVideoTitle(rs.getString("videoTitle"));
				videoShare.setVideoFile(rs.getString("videoFile"));
				videoShare.setUserObj(rs.getString("userObj"));
				videoShare.setShareTime(rs.getString("shareTime"));
				videoShareList.add(videoShare);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return videoShareList;
	}
	/* 传入视频分享对象，进行视频分享的添加业务 */
	public String AddVideoShare(VideoShare videoShare) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新视频分享 */
			String sqlString = "insert into VideoShare(videoTitle,videoFile,userObj,shareTime) values (";
			sqlString += "'" + videoShare.getVideoTitle() + "',";
			sqlString += "'" + videoShare.getVideoFile() + "',";
			sqlString += "'" + videoShare.getUserObj() + "',";
			sqlString += "'" + videoShare.getShareTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "视频分享添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "视频分享添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除视频分享 */
	public String DeleteVideoShare(int videoShareId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from VideoShare where videoShareId=" + videoShareId;
			db.executeUpdate(sqlString);
			result = "视频分享删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "视频分享删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据分享id获取到视频分享 */
	public VideoShare GetVideoShare(int videoShareId) {
		VideoShare videoShare = null;
		DB db = new DB();
		String sql = "select * from VideoShare where videoShareId=" + videoShareId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				videoShare = new VideoShare();
				videoShare.setVideoShareId(rs.getInt("videoShareId"));
				videoShare.setVideoTitle(rs.getString("videoTitle"));
				videoShare.setVideoFile(rs.getString("videoFile"));
				videoShare.setUserObj(rs.getString("userObj"));
				videoShare.setShareTime(rs.getString("shareTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return videoShare;
	}
	/* 更新视频分享 */
	public String UpdateVideoShare(VideoShare videoShare) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update VideoShare set ";
			sql += "videoTitle='" + videoShare.getVideoTitle() + "',";
			sql += "videoFile='" + videoShare.getVideoFile() + "',";
			sql += "userObj='" + videoShare.getUserObj() + "',";
			sql += "shareTime='" + videoShare.getShareTime() + "'";
			sql += " where videoShareId=" + videoShare.getVideoShareId();
			db.executeUpdate(sql);
			result = "视频分享更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "视频分享更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
