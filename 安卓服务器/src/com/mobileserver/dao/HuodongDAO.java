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
	/* ������Ϣ���󣬽��л��Ϣ�����ҵ�� */
	public String AddHuodong(Huodong huodong) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в����»��Ϣ */
			String sqlString = "insert into Huodong(title,content,telephone,personList,userObj,addTime) values (";
			sqlString += "'" + huodong.getTitle() + "',";
			sqlString += "'" + huodong.getContent() + "',";
			sqlString += "'" + huodong.getTelephone() + "',";
			sqlString += "'" + huodong.getPersonList() + "',";
			sqlString += "'" + huodong.getUserObj() + "',";
			sqlString += "'" + huodong.getAddTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "���Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "���Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ�����Ϣ */
	public String DeleteHuodong(int huodongId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Huodong where huodongId=" + huodongId;
			db.executeUpdate(sqlString);
			result = "���Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "���Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݻid��ȡ�����Ϣ */
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
	/* ���»��Ϣ */
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
			result = "���Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "���Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
