package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.NewsTagDAO;
import com.mobileserver.domain.NewsTag;

import org.json.JSONStringer;

public class NewsTagServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*�������ű��ҵ������*/
	private NewsTagDAO newsTagDAO = new NewsTagDAO();

	/*Ĭ�Ϲ��캯��*/
	public NewsTagServlet() {
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
			/*��ȡ��ѯ���ű�ǵĲ�����Ϣ*/
			int newsObj = 0;
			if (request.getParameter("newsObj") != null)
				newsObj = Integer.parseInt(request.getParameter("newsObj"));
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");

			/*����ҵ���߼���ִ�����ű�ǲ�ѯ*/
			List<NewsTag> newsTagList = newsTagDAO.QueryNewsTag(newsObj,userObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<NewsTags>").append("\r\n");
			for (int i = 0; i < newsTagList.size(); i++) {
				sb.append("	<NewsTag>").append("\r\n")
				.append("		<tagId>")
				.append(newsTagList.get(i).getTagId())
				.append("</tagId>").append("\r\n")
				.append("		<newsObj>")
				.append(newsTagList.get(i).getNewsObj())
				.append("</newsObj>").append("\r\n")
				.append("		<userObj>")
				.append(newsTagList.get(i).getUserObj())
				.append("</userObj>").append("\r\n")
				.append("		<newsState>")
				.append(newsTagList.get(i).getNewsState())
				.append("</newsState>").append("\r\n")
				.append("		<tagTime>")
				.append(newsTagList.get(i).getTagTime())
				.append("</tagTime>").append("\r\n")
				.append("	</NewsTag>").append("\r\n");
			}
			sb.append("</NewsTags>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(NewsTag newsTag: newsTagList) {
				  stringer.object();
			  stringer.key("tagId").value(newsTag.getTagId());
			  stringer.key("newsObj").value(newsTag.getNewsObj());
			  stringer.key("userObj").value(newsTag.getUserObj());
			  stringer.key("newsState").value(newsTag.getNewsState());
			  stringer.key("tagTime").value(newsTag.getTagTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ������ű�ǣ���ȡ���ű�ǲ������������浽�½������ű�Ƕ��� */ 
			NewsTag newsTag = new NewsTag();
			int tagId = Integer.parseInt(request.getParameter("tagId"));
			newsTag.setTagId(tagId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			newsTag.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			newsTag.setUserObj(userObj);
			int newsState = Integer.parseInt(request.getParameter("newsState"));
			newsTag.setNewsState(newsState);
			String tagTime = new String(request.getParameter("tagTime").getBytes("iso-8859-1"), "UTF-8");
			newsTag.setTagTime(tagTime);

			/* ����ҵ���ִ����Ӳ��� */
			String result = newsTagDAO.AddNewsTag(newsTag);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ�����ű�ǣ���ȡ���ű�ǵı��id*/
			int tagId = Integer.parseInt(request.getParameter("tagId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = newsTagDAO.DeleteNewsTag(tagId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*�������ű��֮ǰ�ȸ���tagId��ѯĳ�����ű��*/
			int tagId = Integer.parseInt(request.getParameter("tagId"));
			NewsTag newsTag = newsTagDAO.GetNewsTag(tagId);

			// �ͻ��˲�ѯ�����ű�Ƕ��󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("tagId").value(newsTag.getTagId());
			  stringer.key("newsObj").value(newsTag.getNewsObj());
			  stringer.key("userObj").value(newsTag.getUserObj());
			  stringer.key("newsState").value(newsTag.getNewsState());
			  stringer.key("tagTime").value(newsTag.getTagTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* �������ű�ǣ���ȡ���ű�ǲ������������浽�½������ű�Ƕ��� */ 
			NewsTag newsTag = new NewsTag();
			int tagId = Integer.parseInt(request.getParameter("tagId"));
			newsTag.setTagId(tagId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			newsTag.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			newsTag.setUserObj(userObj);
			int newsState = Integer.parseInt(request.getParameter("newsState"));
			newsTag.setNewsState(newsState);
			String tagTime = new String(request.getParameter("tagTime").getBytes("iso-8859-1"), "UTF-8");
			newsTag.setTagTime(tagTime);

			/* ����ҵ���ִ�и��²��� */
			String result = newsTagDAO.UpdateNewsTag(newsTag);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
