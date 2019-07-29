package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.VideoShare;
import com.mobileclient.util.HttpUtil;

/*视频分享管理业务逻辑层*/
public class VideoShareService {
	/* 添加视频分享 */
	public String AddVideoShare(VideoShare videoShare) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("videoShareId", videoShare.getVideoShareId() + "");
		params.put("videoTitle", videoShare.getVideoTitle());
		params.put("videoFile", videoShare.getVideoFile());
		params.put("userObj", videoShare.getUserObj());
		params.put("shareTime", videoShare.getShareTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "VideoShareServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询视频分享 */
	public List<VideoShare> QueryVideoShare(VideoShare queryConditionVideoShare) throws Exception {
		String urlString = HttpUtil.BASE_URL + "VideoShareServlet?action=query";
		if(queryConditionVideoShare != null) {
			urlString += "&videoTitle=" + URLEncoder.encode(queryConditionVideoShare.getVideoTitle(), "UTF-8") + "";
			urlString += "&userObj=" + URLEncoder.encode(queryConditionVideoShare.getUserObj(), "UTF-8") + "";
			urlString += "&shareTime=" + URLEncoder.encode(queryConditionVideoShare.getShareTime(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		VideoShareListHandler videoShareListHander = new VideoShareListHandler();
		xr.setContentHandler(videoShareListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<VideoShare> videoShareList = videoShareListHander.getVideoShareList();
		return videoShareList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<VideoShare> videoShareList = new ArrayList<VideoShare>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				VideoShare videoShare = new VideoShare();
				videoShare.setVideoShareId(object.getInt("videoShareId"));
				videoShare.setVideoTitle(object.getString("videoTitle"));
				videoShare.setVideoFile(object.getString("videoFile"));
				videoShare.setUserObj(object.getString("userObj"));
				videoShare.setShareTime(object.getString("shareTime"));
				videoShareList.add(videoShare);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return videoShareList;
	}

	/* 更新视频分享 */
	public String UpdateVideoShare(VideoShare videoShare) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("videoShareId", videoShare.getVideoShareId() + "");
		params.put("videoTitle", videoShare.getVideoTitle());
		params.put("videoFile", videoShare.getVideoFile());
		params.put("userObj", videoShare.getUserObj());
		params.put("shareTime", videoShare.getShareTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "VideoShareServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除视频分享 */
	public String DeleteVideoShare(int videoShareId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("videoShareId", videoShareId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "VideoShareServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "视频分享信息删除失败!";
		}
	}

	/* 根据分享id获取视频分享对象 */
	public VideoShare GetVideoShare(int videoShareId)  {
		List<VideoShare> videoShareList = new ArrayList<VideoShare>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("videoShareId", videoShareId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "VideoShareServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				VideoShare videoShare = new VideoShare();
				videoShare.setVideoShareId(object.getInt("videoShareId"));
				videoShare.setVideoTitle(object.getString("videoTitle"));
				videoShare.setVideoFile(object.getString("videoFile"));
				videoShare.setUserObj(object.getString("userObj"));
				videoShare.setShareTime(object.getString("shareTime"));
				videoShareList.add(videoShare);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = videoShareList.size();
		if(size>0) return videoShareList.get(0); 
		else return null; 
	}
}
