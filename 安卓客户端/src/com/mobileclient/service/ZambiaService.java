package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Zambia;
import com.mobileclient.util.HttpUtil;

/*新闻赞管理业务逻辑层*/
public class ZambiaService {
	/* 添加新闻赞 */
	public String AddZambia(Zambia zambia) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("zambiaId", zambia.getZambiaId() + "");
		params.put("newsObj", zambia.getNewsObj() + "");
		params.put("userObj", zambia.getUserObj());
		params.put("zambiaTime", zambia.getZambiaTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ZambiaServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询新闻赞 */
	public List<Zambia> QueryZambia(Zambia queryConditionZambia) throws Exception {
		String urlString = HttpUtil.BASE_URL + "ZambiaServlet?action=query";
		if(queryConditionZambia != null) {
			urlString += "&newsObj=" + queryConditionZambia.getNewsObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionZambia.getUserObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		ZambiaListHandler zambiaListHander = new ZambiaListHandler();
		xr.setContentHandler(zambiaListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Zambia> zambiaList = zambiaListHander.getZambiaList();
		return zambiaList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Zambia> zambiaList = new ArrayList<Zambia>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Zambia zambia = new Zambia();
				zambia.setZambiaId(object.getInt("zambiaId"));
				zambia.setNewsObj(object.getInt("newsObj"));
				zambia.setUserObj(object.getString("userObj"));
				zambia.setZambiaTime(object.getString("zambiaTime"));
				zambiaList.add(zambia);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zambiaList;
	}

	/* 更新新闻赞 */
	public String UpdateZambia(Zambia zambia) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("zambiaId", zambia.getZambiaId() + "");
		params.put("newsObj", zambia.getNewsObj() + "");
		params.put("userObj", zambia.getUserObj());
		params.put("zambiaTime", zambia.getZambiaTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ZambiaServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除新闻赞 */
	public String DeleteZambia(int zambiaId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("zambiaId", zambiaId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ZambiaServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "新闻赞信息删除失败!";
		}
	}

	/* 根据赞id获取新闻赞对象 */
	public Zambia GetZambia(int zambiaId)  {
		List<Zambia> zambiaList = new ArrayList<Zambia>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("zambiaId", zambiaId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ZambiaServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Zambia zambia = new Zambia();
				zambia.setZambiaId(object.getInt("zambiaId"));
				zambia.setNewsObj(object.getInt("newsObj"));
				zambia.setUserObj(object.getString("userObj"));
				zambia.setZambiaTime(object.getString("zambiaTime"));
				zambiaList.add(zambia);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = zambiaList.size();
		if(size>0) return zambiaList.get(0); 
		else return null; 
	}
}
