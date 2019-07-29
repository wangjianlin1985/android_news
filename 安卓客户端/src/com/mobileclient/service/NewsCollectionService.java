package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.NewsCollection;
import com.mobileclient.util.HttpUtil;

/*新闻收藏管理业务逻辑层*/
public class NewsCollectionService {
	/* 添加新闻收藏 */
	public String AddNewsCollection(NewsCollection newsCollection) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collectionId", newsCollection.getCollectionId() + "");
		params.put("newsObj", newsCollection.getNewsObj() + "");
		params.put("userObj", newsCollection.getUserObj());
		params.put("collectTime", newsCollection.getCollectTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsCollectionServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询新闻收藏 */
	public List<NewsCollection> QueryNewsCollection(NewsCollection queryConditionNewsCollection) throws Exception {
		String urlString = HttpUtil.BASE_URL + "NewsCollectionServlet?action=query";
		if(queryConditionNewsCollection != null) {
			urlString += "&newsObj=" + queryConditionNewsCollection.getNewsObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionNewsCollection.getUserObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		NewsCollectionListHandler newsCollectionListHander = new NewsCollectionListHandler();
		xr.setContentHandler(newsCollectionListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<NewsCollection> newsCollectionList = newsCollectionListHander.getNewsCollectionList();
		return newsCollectionList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<NewsCollection> newsCollectionList = new ArrayList<NewsCollection>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				NewsCollection newsCollection = new NewsCollection();
				newsCollection.setCollectionId(object.getInt("collectionId"));
				newsCollection.setNewsObj(object.getInt("newsObj"));
				newsCollection.setUserObj(object.getString("userObj"));
				newsCollection.setCollectTime(object.getString("collectTime"));
				newsCollectionList.add(newsCollection);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsCollectionList;
	}

	/* 更新新闻收藏 */
	public String UpdateNewsCollection(NewsCollection newsCollection) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collectionId", newsCollection.getCollectionId() + "");
		params.put("newsObj", newsCollection.getNewsObj() + "");
		params.put("userObj", newsCollection.getUserObj());
		params.put("collectTime", newsCollection.getCollectTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsCollectionServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除新闻收藏 */
	public String DeleteNewsCollection(int collectionId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collectionId", collectionId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsCollectionServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "新闻收藏信息删除失败!";
		}
	}

	/* 根据收藏id获取新闻收藏对象 */
	public NewsCollection GetNewsCollection(int collectionId)  {
		List<NewsCollection> newsCollectionList = new ArrayList<NewsCollection>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collectionId", collectionId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsCollectionServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				NewsCollection newsCollection = new NewsCollection();
				newsCollection.setCollectionId(object.getInt("collectionId"));
				newsCollection.setNewsObj(object.getInt("newsObj"));
				newsCollection.setUserObj(object.getString("userObj"));
				newsCollection.setCollectTime(object.getString("collectTime"));
				newsCollectionList.add(newsCollection);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = newsCollectionList.size();
		if(size>0) return newsCollectionList.get(0); 
		else return null; 
	}
}
