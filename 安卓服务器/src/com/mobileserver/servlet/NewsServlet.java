package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.NewsDAO;
import com.mobileserver.domain.News;

import org.json.JSONStringer;

public class NewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*����������Ϣҵ������*/
	private NewsDAO newsDAO = new NewsDAO();

	/*Ĭ�Ϲ��캯��*/
	public NewsServlet() {
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
			/*��ȡ��ѯ������Ϣ�Ĳ�����Ϣ*/
			int newsClassObj = 0;
			if (request.getParameter("newsClassObj") != null)
				newsClassObj = Integer.parseInt(request.getParameter("newsClassObj"));
			String newsTitle = request.getParameter("newsTitle");
			newsTitle = newsTitle == null ? "" : new String(request.getParameter(
					"newsTitle").getBytes("iso-8859-1"), "UTF-8");
			String comFrom = request.getParameter("comFrom");
			comFrom = comFrom == null ? "" : new String(request.getParameter(
					"comFrom").getBytes("iso-8859-1"), "UTF-8");
			String addTime = request.getParameter("addTime");
			addTime = addTime == null ? "" : new String(request.getParameter(
					"addTime").getBytes("iso-8859-1"), "UTF-8");

			/*����ҵ���߼���ִ��������Ϣ��ѯ*/
			List<News> newsList = newsDAO.QueryNews(newsClassObj,newsTitle,comFrom,addTime);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Newss>").append("\r\n");
			for (int i = 0; i < newsList.size(); i++) {
				sb.append("	<News>").append("\r\n")
				.append("		<newsId>")
				.append(newsList.get(i).getNewsId())
				.append("</newsId>").append("\r\n")
				.append("		<newsClassObj>")
				.append(newsList.get(i).getNewsClassObj())
				.append("</newsClassObj>").append("\r\n")
				.append("		<newsTitle>")
				.append(newsList.get(i).getNewsTitle())
				.append("</newsTitle>").append("\r\n")
				.append("		<newsPhoto>")
				.append(newsList.get(i).getNewsPhoto())
				.append("</newsPhoto>").append("\r\n")
				.append("		<content>")
				.append(newsList.get(i).getContent())
				.append("</content>").append("\r\n")
				.append("		<comFrom>")
				.append(newsList.get(i).getComFrom())
				.append("</comFrom>").append("\r\n")
				.append("		<hitNum>")
				.append(newsList.get(i).getHitNum())
				.append("</hitNum>").append("\r\n")
				.append("		<addTime>")
				.append(newsList.get(i).getAddTime())
				.append("</addTime>").append("\r\n")
				.append("	</News>").append("\r\n");
			}
			sb.append("</Newss>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(News news: newsList) {
				  stringer.object();
			  stringer.key("newsId").value(news.getNewsId());
			  stringer.key("newsClassObj").value(news.getNewsClassObj());
			  stringer.key("newsTitle").value(news.getNewsTitle());
			  stringer.key("newsPhoto").value(news.getNewsPhoto());
			  stringer.key("content").value(news.getContent());
			  stringer.key("comFrom").value(news.getComFrom());
			  stringer.key("hitNum").value(news.getHitNum());
			  stringer.key("addTime").value(news.getAddTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���������Ϣ����ȡ������Ϣ�������������浽�½���������Ϣ���� */ 
			News news = new News();
			int newsId = Integer.parseInt(request.getParameter("newsId"));
			news.setNewsId(newsId);
			int newsClassObj = Integer.parseInt(request.getParameter("newsClassObj"));
			news.setNewsClassObj(newsClassObj);
			String newsTitle = new String(request.getParameter("newsTitle").getBytes("iso-8859-1"), "UTF-8");
			news.setNewsTitle(newsTitle);
			String newsPhoto = new String(request.getParameter("newsPhoto").getBytes("iso-8859-1"), "UTF-8");
			news.setNewsPhoto(newsPhoto);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			news.setContent(content);
			String comFrom = new String(request.getParameter("comFrom").getBytes("iso-8859-1"), "UTF-8");
			news.setComFrom(comFrom);
			int hitNum = Integer.parseInt(request.getParameter("hitNum"));
			news.setHitNum(hitNum);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			news.setAddTime(addTime);

			/* ����ҵ���ִ����Ӳ��� */
			String result = newsDAO.AddNews(news);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��������Ϣ����ȡ������Ϣ������id*/
			int newsId = Integer.parseInt(request.getParameter("newsId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = newsDAO.DeleteNews(newsId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����������Ϣ֮ǰ�ȸ���newsId��ѯĳ��������Ϣ*/
			int newsId = Integer.parseInt(request.getParameter("newsId"));
			News news = newsDAO.GetNews(newsId);

			// �ͻ��˲�ѯ��������Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("newsId").value(news.getNewsId());
			  stringer.key("newsClassObj").value(news.getNewsClassObj());
			  stringer.key("newsTitle").value(news.getNewsTitle());
			  stringer.key("newsPhoto").value(news.getNewsPhoto());
			  stringer.key("content").value(news.getContent());
			  stringer.key("comFrom").value(news.getComFrom());
			  stringer.key("hitNum").value(news.getHitNum());
			  stringer.key("addTime").value(news.getAddTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����������Ϣ����ȡ������Ϣ�������������浽�½���������Ϣ���� */ 
			News news = new News();
			int newsId = Integer.parseInt(request.getParameter("newsId"));
			news.setNewsId(newsId);
			int newsClassObj = Integer.parseInt(request.getParameter("newsClassObj"));
			news.setNewsClassObj(newsClassObj);
			String newsTitle = new String(request.getParameter("newsTitle").getBytes("iso-8859-1"), "UTF-8");
			news.setNewsTitle(newsTitle);
			String newsPhoto = new String(request.getParameter("newsPhoto").getBytes("iso-8859-1"), "UTF-8");
			news.setNewsPhoto(newsPhoto);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			news.setContent(content);
			String comFrom = new String(request.getParameter("comFrom").getBytes("iso-8859-1"), "UTF-8");
			news.setComFrom(comFrom);
			int hitNum = Integer.parseInt(request.getParameter("hitNum"));
			news.setHitNum(hitNum);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			news.setAddTime(addTime);

			/* ����ҵ���ִ�и��²��� */
			String result = newsDAO.UpdateNews(news);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
