package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Zambia;
import com.mobileserver.util.DB;

public class ZambiaDAO {

	public List<Zambia> QueryZambia(int newsObj,String userObj) {
		List<Zambia> zambiaList = new ArrayList<Zambia>();
		DB db = new DB();
		String sql = "select * from Zambia where 1=1";
		if (newsObj != 0)
			sql += " and newsObj=" + newsObj;
		if (!userObj.equals(""))
			sql += " and userObj = '" + userObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Zambia zambia = new Zambia();
				zambia.setZambiaId(rs.getInt("zambiaId"));
				zambia.setNewsObj(rs.getInt("newsObj"));
				zambia.setUserObj(rs.getString("userObj"));
				zambia.setZambiaTime(rs.getString("zambiaTime"));
				zambiaList.add(zambia);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return zambiaList;
	}
	/* 传入新闻赞对象，进行新闻赞的添加业务 */
	public String AddZambia(Zambia zambia) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新新闻赞 */
			String sqlString = "insert into Zambia(newsObj,userObj,zambiaTime) values (";
			sqlString += zambia.getNewsObj() + ",";
			sqlString += "'" + zambia.getUserObj() + "',";
			sqlString += "'" + zambia.getZambiaTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "新闻赞添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻赞添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除新闻赞 */
	public String DeleteZambia(int zambiaId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Zambia where zambiaId=" + zambiaId;
			db.executeUpdate(sqlString);
			result = "新闻赞删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻赞删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据赞id获取到新闻赞 */
	public Zambia GetZambia(int zambiaId) {
		Zambia zambia = null;
		DB db = new DB();
		String sql = "select * from Zambia where zambiaId=" + zambiaId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				zambia = new Zambia();
				zambia.setZambiaId(rs.getInt("zambiaId"));
				zambia.setNewsObj(rs.getInt("newsObj"));
				zambia.setUserObj(rs.getString("userObj"));
				zambia.setZambiaTime(rs.getString("zambiaTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return zambia;
	}
	/* 更新新闻赞 */
	public String UpdateZambia(Zambia zambia) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Zambia set ";
			sql += "newsObj=" + zambia.getNewsObj() + ",";
			sql += "userObj='" + zambia.getUserObj() + "',";
			sql += "zambiaTime='" + zambia.getZambiaTime() + "'";
			sql += " where zambiaId=" + zambia.getZambiaId();
			db.executeUpdate(sql);
			result = "新闻赞更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "新闻赞更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
