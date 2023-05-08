package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.NewsCollectionDAO;
import com.mobileserver.domain.NewsCollection;

import org.json.JSONStringer;

public class NewsCollectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*���������ղ�ҵ������*/
	private NewsCollectionDAO newsCollectionDAO = new NewsCollectionDAO();

	/*Ĭ�Ϲ��캯��*/
	public NewsCollectionServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*��ȡaction����������action��ִֵ�в�ͬ��ҵ����*/
		String action = request.getParameter("action");
		if (action.equals("query")) {
			/*��ȡ��ѯ�����ղصĲ�����Ϣ*/
			int newsObj = 0;
			if (request.getParameter("newsObj") != null)
				newsObj = Integer.parseInt(request.getParameter("newsObj"));
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");

			/*����ҵ���߼���ִ�������ղز�ѯ*/
			List<NewsCollection> newsCollectionList = newsCollectionDAO.QueryNewsCollection(newsObj,userObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<NewsCollections>").append("\r\n");
			for (int i = 0; i < newsCollectionList.size(); i++) {
				sb.append("	<NewsCollection>").append("\r\n")
				.append("		<collectionId>")
				.append(newsCollectionList.get(i).getCollectionId())
				.append("</collectionId>").append("\r\n")
				.append("		<newsObj>")
				.append(newsCollectionList.get(i).getNewsObj())
				.append("</newsObj>").append("\r\n")
				.append("		<userObj>")
				.append(newsCollectionList.get(i).getUserObj())
				.append("</userObj>").append("\r\n")
				.append("		<collectTime>")
				.append(newsCollectionList.get(i).getCollectTime())
				.append("</collectTime>").append("\r\n")
				.append("	</NewsCollection>").append("\r\n");
			}
			sb.append("</NewsCollections>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(NewsCollection newsCollection: newsCollectionList) {
				  stringer.object();
			  stringer.key("collectionId").value(newsCollection.getCollectionId());
			  stringer.key("newsObj").value(newsCollection.getNewsObj());
			  stringer.key("userObj").value(newsCollection.getUserObj());
			  stringer.key("collectTime").value(newsCollection.getCollectTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��������ղأ���ȡ�����ղز������������浽�½��������ղض��� */ 
			NewsCollection newsCollection = new NewsCollection();
			int collectionId = Integer.parseInt(request.getParameter("collectionId"));
			newsCollection.setCollectionId(collectionId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			newsCollection.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			newsCollection.setUserObj(userObj);
			String collectTime = new String(request.getParameter("collectTime").getBytes("iso-8859-1"), "UTF-8");
			newsCollection.setCollectTime(collectTime);

			/* ����ҵ���ִ����Ӳ��� */
			String result = newsCollectionDAO.AddNewsCollection(newsCollection);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ�������ղأ���ȡ�����ղص��ղ�id*/
			int collectionId = Integer.parseInt(request.getParameter("collectionId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = newsCollectionDAO.DeleteNewsCollection(collectionId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���������ղ�֮ǰ�ȸ���collectionId��ѯĳ�������ղ�*/
			int collectionId = Integer.parseInt(request.getParameter("collectionId"));
			NewsCollection newsCollection = newsCollectionDAO.GetNewsCollection(collectionId);

			// �ͻ��˲�ѯ�������ղض��󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("collectionId").value(newsCollection.getCollectionId());
			  stringer.key("newsObj").value(newsCollection.getNewsObj());
			  stringer.key("userObj").value(newsCollection.getUserObj());
			  stringer.key("collectTime").value(newsCollection.getCollectTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���������ղأ���ȡ�����ղز������������浽�½��������ղض��� */ 
			NewsCollection newsCollection = new NewsCollection();
			int collectionId = Integer.parseInt(request.getParameter("collectionId"));
			newsCollection.setCollectionId(collectionId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			newsCollection.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			newsCollection.setUserObj(userObj);
			String collectTime = new String(request.getParameter("collectTime").getBytes("iso-8859-1"), "UTF-8");
			newsCollection.setCollectTime(collectTime);

			/* ����ҵ���ִ�и��²��� */
			String result = newsCollectionDAO.UpdateNewsCollection(newsCollection);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
