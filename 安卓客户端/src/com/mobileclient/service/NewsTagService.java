package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.NewsTag;
import com.mobileclient.util.HttpUtil;

/*新闻标记管理业务逻辑层*/
public class NewsTagService {
	/* 添加新闻标记 */
	public String AddNewsTag(NewsTag newsTag) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("tagId", newsTag.getTagId() + "");
		params.put("newsObj", newsTag.getNewsObj() + "");
		params.put("userObj", newsTag.getUserObj());
		params.put("newsState", newsTag.getNewsState() + "");
		params.put("tagTime", newsTag.getTagTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsTagServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询新闻标记 */
	public List<NewsTag> QueryNewsTag(NewsTag queryConditionNewsTag) throws Exception {
		String urlString = HttpUtil.BASE_URL + "NewsTagServlet?action=query";
		if(queryConditionNewsTag != null) {
			urlString += "&newsObj=" + queryConditionNewsTag.getNewsObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionNewsTag.getUserObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		NewsTagListHandler newsTagListHander = new NewsTagListHandler();
		xr.setContentHandler(newsTagListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<NewsTag> newsTagList = newsTagListHander.getNewsTagList();
		return newsTagList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<NewsTag> newsTagList = new ArrayList<NewsTag>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				NewsTag newsTag = new NewsTag();
				newsTag.setTagId(object.getInt("tagId"));
				newsTag.setNewsObj(object.getInt("newsObj"));
				newsTag.setUserObj(object.getString("userObj"));
				newsTag.setNewsState(object.getInt("newsState"));
				newsTag.setTagTime(object.getString("tagTime"));
				newsTagList.add(newsTag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsTagList;
	}

	/* 更新新闻标记 */
	public String UpdateNewsTag(NewsTag newsTag) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("tagId", newsTag.getTagId() + "");
		params.put("newsObj", newsTag.getNewsObj() + "");
		params.put("userObj", newsTag.getUserObj());
		params.put("newsState", newsTag.getNewsState() + "");
		params.put("tagTime", newsTag.getTagTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsTagServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除新闻标记 */
	public String DeleteNewsTag(int tagId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("tagId", tagId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsTagServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "新闻标记信息删除失败!";
		}
	}

	/* 根据标记id获取新闻标记对象 */
	public NewsTag GetNewsTag(int tagId)  {
		List<NewsTag> newsTagList = new ArrayList<NewsTag>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("tagId", tagId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsTagServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				NewsTag newsTag = new NewsTag();
				newsTag.setTagId(object.getInt("tagId"));
				newsTag.setNewsObj(object.getInt("newsObj"));
				newsTag.setUserObj(object.getString("userObj"));
				newsTag.setNewsState(object.getInt("newsState"));
				newsTag.setTagTime(object.getString("tagTime"));
				newsTagList.add(newsTag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = newsTagList.size();
		if(size>0) return newsTagList.get(0); 
		else return null; 
	}
}
