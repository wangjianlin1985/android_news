package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Huodong;
import com.mobileserver.util.DB;

public class HuodongDAO {

	public List<Huodong> QueryHuodong(String title,String telephone,String userObj,String addTime) {
		List<Huodong> huodongList = new ArrayList<Huodong>();
		DB db = new DB();
		String sql = "select * from Huodong where 1=1";
		if (!title.equals(""))
			sql += " and title like '%" + title + "%'";
		if (!telephone.equals(""))
			sql += " and telephone like '%" + telephone + "%'";
		if (!userObj.equals(""))
			sql += " and userObj = '" + userObj + "'";
		if (!addTime.equals(""))
			sql += " and addTime like '%" + addTime + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Huodong huodong = new Huodong();
				huodong.setHuodongId(rs.getInt("huodongId"));
				huodong.setTitle(rs.getString("title"));
				huodong.setContent(rs.getString("content"));
				huodong.setTelephone(rs.getString("telephone"));
				huodong.setPersonList(rs.getString("personList"));
				huodong.setUserObj(rs.getString("userObj"));
				huodong.setAddTime(rs.getString("addTime"));
				huodongList.add(huodong);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return huodongList;
	}
	/* 传入活动信息对象，进行活动信息的添加业务 */
	public String AddHuodong(Huodong huodong) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新活动信息 */
			String sqlString = "insert into Huodong(title,content,telephone,personList,userObj,addTime) values (";
			sqlString += "'" + huodong.getTitle() + "',";
			sqlString += "'" + huodong.getContent() + "',";
			sqlString += "'" + huodong.getTelephone() + "',";
			sqlString += "'" + huodong.getPersonList() + "',";
			sqlString += "'" + huodong.getUserObj() + "',";
			sqlString += "'" + huodong.getAddTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "活动信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "活动信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除活动信息 */
	public String DeleteHuodong(int huodongId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Huodong where huodongId=" + huodongId;
			db.executeUpdate(sqlString);
			result = "活动信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "活动信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据活动id获取到活动信息 */
	public Huodong GetHuodong(int huodongId) {
		Huodong huodong = null;
		DB db = new DB();
		String sql = "select * from Huodong where huodongId=" + huodongId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				huodong = new Huodong();
				huodong.setHuodongId(rs.getInt("huodongId"));
				huodong.setTitle(rs.getString("title"));
				huodong.setContent(rs.getString("content"));
				huodong.setTelephone(rs.getString("telephone"));
				huodong.setPersonList(rs.getString("personList"));
				huodong.setUserObj(rs.getString("userObj"));
				huodong.setAddTime(rs.getString("addTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return huodong;
	}
	/* 更新活动信息 */
	public String UpdateHuodong(Huodong huodong) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Huodong set ";
			sql += "title='" + huodong.getTitle() + "',";
			sql += "content='" + huodong.getContent() + "',";
			sql += "telephone='" + huodong.getTelephone() + "',";
			sql += "personList='" + huodong.getPersonList() + "',";
			sql += "userObj='" + huodong.getUserObj() + "',";
			sql += "addTime='" + huodong.getAddTime() + "'";
			sql += " where huodongId=" + huodong.getHuodongId();
			db.executeUpdate(sql);
			result = "活动信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "活动信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
