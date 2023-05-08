package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.NewsComment;
import com.mobileclient.util.HttpUtil;

/*新闻评论管理业务逻辑层*/
public class NewsCommentService {
	/* 添加新闻评论 */
	public String AddNewsComment(NewsComment newsComment) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("commentId", newsComment.getCommentId() + "");
		params.put("newsObj", newsComment.getNewsObj() + "");
		params.put("userObj", newsComment.getUserObj());
		params.put("content", newsComment.getContent());
		params.put("commentTime", newsComment.getCommentTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsCommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询新闻评论 */
	public List<NewsComment> QueryNewsComment(NewsComment queryConditionNewsComment) throws Exception {
		String urlString = HttpUtil.BASE_URL + "NewsCommentServlet?action=query";
		if(queryConditionNewsComment != null) {
			urlString += "&newsObj=" + queryConditionNewsComment.getNewsObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionNewsComment.getUserObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		NewsCommentListHandler newsCommentListHander = new NewsCommentListHandler();
		xr.setContentHandler(newsCommentListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<NewsComment> newsCommentList = newsCommentListHander.getNewsCommentList();
		return newsCommentList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<NewsComment> newsCommentList = new ArrayList<NewsComment>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				NewsComment newsComment = new NewsComment();
				newsComment.setCommentId(object.getInt("commentId"));
				newsComment.setNewsObj(object.getInt("newsObj"));
				newsComment.setUserObj(object.getString("userObj"));
				newsComment.setContent(object.getString("content"));
				newsComment.setCommentTime(object.getString("commentTime"));
				newsCommentList.add(newsComment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsCommentList;
	}

	/* 更新新闻评论 */
	public String UpdateNewsComment(NewsComment newsComment) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("commentId", newsComment.getCommentId() + "");
		params.put("newsObj", newsComment.getNewsObj() + "");
		params.put("userObj", newsComment.getUserObj());
		params.put("content", newsComment.getContent());
		params.put("commentTime", newsComment.getCommentTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsCommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除新闻评论 */
	public String DeleteNewsComment(int commentId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("commentId", commentId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsCommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "新闻评论信息删除失败!";
		}
	}

	/* 根据评论id获取新闻评论对象 */
	public NewsComment GetNewsComment(int commentId)  {
		List<NewsComment> newsCommentList = new ArrayList<NewsComment>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("commentId", commentId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsCommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				NewsComment newsComment = new NewsComment();
				newsComment.setCommentId(object.getInt("commentId"));
				newsComment.setNewsObj(object.getInt("newsObj"));
				newsComment.setUserObj(object.getString("userObj"));
				newsComment.setContent(object.getString("content"));
				newsComment.setCommentTime(object.getString("commentTime"));
				newsCommentList.add(newsComment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = newsCommentList.size();
		if(size>0) return newsCommentList.get(0); 
		else return null; 
	}
}
