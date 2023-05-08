package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.PhotoComment;
import com.mobileclient.util.HttpUtil;

/*图片评论管理业务逻辑层*/
public class PhotoCommentService {
	/* 添加图片评论 */
	public String AddPhotoComment(PhotoComment photoComment) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("photoCommentId", photoComment.getPhotoCommentId() + "");
		params.put("photoObj", photoComment.getPhotoObj() + "");
		params.put("content", photoComment.getContent());
		params.put("userInfoObj", photoComment.getUserInfoObj());
		params.put("commentTime", photoComment.getCommentTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PhotoCommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询图片评论 */
	public List<PhotoComment> QueryPhotoComment(PhotoComment queryConditionPhotoComment) throws Exception {
		String urlString = HttpUtil.BASE_URL + "PhotoCommentServlet?action=query";
		if(queryConditionPhotoComment != null) {
			urlString += "&photoObj=" + queryConditionPhotoComment.getPhotoObj();
			urlString += "&content=" + URLEncoder.encode(queryConditionPhotoComment.getContent(), "UTF-8") + "";
			urlString += "&userInfoObj=" + URLEncoder.encode(queryConditionPhotoComment.getUserInfoObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		PhotoCommentListHandler photoCommentListHander = new PhotoCommentListHandler();
		xr.setContentHandler(photoCommentListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<PhotoComment> photoCommentList = photoCommentListHander.getPhotoCommentList();
		return photoCommentList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<PhotoComment> photoCommentList = new ArrayList<PhotoComment>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				PhotoComment photoComment = new PhotoComment();
				photoComment.setPhotoCommentId(object.getInt("photoCommentId"));
				photoComment.setPhotoObj(object.getInt("photoObj"));
				photoComment.setContent(object.getString("content"));
				photoComment.setUserInfoObj(object.getString("userInfoObj"));
				photoComment.setCommentTime(object.getString("commentTime"));
				photoCommentList.add(photoComment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return photoCommentList;
	}

	/* 更新图片评论 */
	public String UpdatePhotoComment(PhotoComment photoComment) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("photoCommentId", photoComment.getPhotoCommentId() + "");
		params.put("photoObj", photoComment.getPhotoObj() + "");
		params.put("content", photoComment.getContent());
		params.put("userInfoObj", photoComment.getUserInfoObj());
		params.put("commentTime", photoComment.getCommentTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PhotoCommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除图片评论 */
	public String DeletePhotoComment(int photoCommentId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("photoCommentId", photoCommentId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PhotoCommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "图片评论信息删除失败!";
		}
	}

	/* 根据评论id获取图片评论对象 */
	public PhotoComment GetPhotoComment(int photoCommentId)  {
		List<PhotoComment> photoCommentList = new ArrayList<PhotoComment>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("photoCommentId", photoCommentId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PhotoCommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				PhotoComment photoComment = new PhotoComment();
				photoComment.setPhotoCommentId(object.getInt("photoCommentId"));
				photoComment.setPhotoObj(object.getInt("photoObj"));
				photoComment.setContent(object.getString("content"));
				photoComment.setUserInfoObj(object.getString("userInfoObj"));
				photoComment.setCommentTime(object.getString("commentTime"));
				photoCommentList.add(photoComment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = photoCommentList.size();
		if(size>0) return photoCommentList.get(0); 
		else return null; 
	}
}
