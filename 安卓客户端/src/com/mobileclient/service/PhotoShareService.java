package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.PhotoShare;
import com.mobileclient.util.HttpUtil;

/*图片分享管理业务逻辑层*/
public class PhotoShareService {
	/* 添加图片分享 */
	public String AddPhotoShare(PhotoShare photoShare) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sharePhotoId", photoShare.getSharePhotoId() + "");
		params.put("photoTitle", photoShare.getPhotoTitle());
		params.put("sharePhoto", photoShare.getSharePhoto());
		params.put("userInfoObj", photoShare.getUserInfoObj());
		params.put("shareTime", photoShare.getShareTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PhotoShareServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询图片分享 */
	public List<PhotoShare> QueryPhotoShare(PhotoShare queryConditionPhotoShare) throws Exception {
		String urlString = HttpUtil.BASE_URL + "PhotoShareServlet?action=query";
		if(queryConditionPhotoShare != null) {
			urlString += "&userInfoObj=" + URLEncoder.encode(queryConditionPhotoShare.getUserInfoObj(), "UTF-8") + "";
			urlString += "&shareTime=" + URLEncoder.encode(queryConditionPhotoShare.getShareTime(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		PhotoShareListHandler photoShareListHander = new PhotoShareListHandler();
		xr.setContentHandler(photoShareListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<PhotoShare> photoShareList = photoShareListHander.getPhotoShareList();
		return photoShareList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<PhotoShare> photoShareList = new ArrayList<PhotoShare>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				PhotoShare photoShare = new PhotoShare();
				photoShare.setSharePhotoId(object.getInt("sharePhotoId"));
				photoShare.setPhotoTitle(object.getString("photoTitle"));
				photoShare.setSharePhoto(object.getString("sharePhoto"));
				photoShare.setUserInfoObj(object.getString("userInfoObj"));
				photoShare.setShareTime(object.getString("shareTime"));
				photoShareList.add(photoShare);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return photoShareList;
	}

	/* 更新图片分享 */
	public String UpdatePhotoShare(PhotoShare photoShare) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sharePhotoId", photoShare.getSharePhotoId() + "");
		params.put("photoTitle", photoShare.getPhotoTitle());
		params.put("sharePhoto", photoShare.getSharePhoto());
		params.put("userInfoObj", photoShare.getUserInfoObj());
		params.put("shareTime", photoShare.getShareTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PhotoShareServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除图片分享 */
	public String DeletePhotoShare(int sharePhotoId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sharePhotoId", sharePhotoId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PhotoShareServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "图片分享信息删除失败!";
		}
	}

	/* 根据分享id获取图片分享对象 */
	public PhotoShare GetPhotoShare(int sharePhotoId)  {
		List<PhotoShare> photoShareList = new ArrayList<PhotoShare>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sharePhotoId", sharePhotoId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PhotoShareServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				PhotoShare photoShare = new PhotoShare();
				photoShare.setSharePhotoId(object.getInt("sharePhotoId"));
				photoShare.setPhotoTitle(object.getString("photoTitle"));
				photoShare.setSharePhoto(object.getString("sharePhoto"));
				photoShare.setUserInfoObj(object.getString("userInfoObj"));
				photoShare.setShareTime(object.getString("shareTime"));
				photoShareList.add(photoShare);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = photoShareList.size();
		if(size>0) return photoShareList.get(0); 
		else return null; 
	}
}
