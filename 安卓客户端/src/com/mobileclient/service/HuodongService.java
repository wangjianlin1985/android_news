package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Huodong;
import com.mobileclient.util.HttpUtil;

/*活动信息管理业务逻辑层*/
public class HuodongService {
	/* 添加活动信息 */
	public String AddHuodong(Huodong huodong) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("huodongId", huodong.getHuodongId() + "");
		params.put("title", huodong.getTitle());
		params.put("content", huodong.getContent());
		params.put("telephone", huodong.getTelephone());
		params.put("personList", huodong.getPersonList());
		params.put("userObj", huodong.getUserObj());
		params.put("addTime", huodong.getAddTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "HuodongServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询活动信息 */
	public List<Huodong> QueryHuodong(Huodong queryConditionHuodong) throws Exception {
		String urlString = HttpUtil.BASE_URL + "HuodongServlet?action=query";
		if(queryConditionHuodong != null) {
			urlString += "&title=" + URLEncoder.encode(queryConditionHuodong.getTitle(), "UTF-8") + "";
			urlString += "&telephone=" + URLEncoder.encode(queryConditionHuodong.getTelephone(), "UTF-8") + "";
			urlString += "&userObj=" + URLEncoder.encode(queryConditionHuodong.getUserObj(), "UTF-8") + "";
			urlString += "&addTime=" + URLEncoder.encode(queryConditionHuodong.getAddTime(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		HuodongListHandler huodongListHander = new HuodongListHandler();
		xr.setContentHandler(huodongListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Huodong> huodongList = huodongListHander.getHuodongList();
		return huodongList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Huodong> huodongList = new ArrayList<Huodong>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Huodong huodong = new Huodong();
				huodong.setHuodongId(object.getInt("huodongId"));
				huodong.setTitle(object.getString("title"));
				huodong.setContent(object.getString("content"));
				huodong.setTelephone(object.getString("telephone"));
				huodong.setPersonList(object.getString("personList"));
				huodong.setUserObj(object.getString("userObj"));
				huodong.setAddTime(object.getString("addTime"));
				huodongList.add(huodong);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return huodongList;
	}

	/* 更新活动信息 */
	public String UpdateHuodong(Huodong huodong) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("huodongId", huodong.getHuodongId() + "");
		params.put("title", huodong.getTitle());
		params.put("content", huodong.getContent());
		params.put("telephone", huodong.getTelephone());
		params.put("personList", huodong.getPersonList());
		params.put("userObj", huodong.getUserObj());
		params.put("addTime", huodong.getAddTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "HuodongServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除活动信息 */
	public String DeleteHuodong(int huodongId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("huodongId", huodongId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "HuodongServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "活动信息信息删除失败!";
		}
	}

	/* 根据活动id获取活动信息对象 */
	public Huodong GetHuodong(int huodongId)  {
		List<Huodong> huodongList = new ArrayList<Huodong>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("huodongId", huodongId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "HuodongServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Huodong huodong = new Huodong();
				huodong.setHuodongId(object.getInt("huodongId"));
				huodong.setTitle(object.getString("title"));
				huodong.setContent(object.getString("content"));
				huodong.setTelephone(object.getString("telephone"));
				huodong.setPersonList(object.getString("personList"));
				huodong.setUserObj(object.getString("userObj"));
				huodong.setAddTime(object.getString("addTime"));
				huodongList.add(huodong);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = huodongList.size();
		if(size>0) return huodongList.get(0); 
		else return null; 
	}
}
