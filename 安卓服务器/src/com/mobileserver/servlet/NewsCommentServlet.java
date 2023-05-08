package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.NewsCommentDAO;
import com.mobileserver.domain.NewsComment;

import org.json.JSONStringer;

public class NewsCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*������������ҵ������*/
	private NewsCommentDAO newsCommentDAO = new NewsCommentDAO();

	/*Ĭ�Ϲ��캯��*/
	public NewsCommentServlet() {
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
			/*��ȡ��ѯ�������۵Ĳ�����Ϣ*/
			int newsObj = 0;
			if (request.getParameter("newsObj") != null)
				newsObj = Integer.parseInt(request.getParameter("newsObj"));
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");

			/*����ҵ���߼���ִ���������۲�ѯ*/
			List<NewsComment> newsCommentList = newsCommentDAO.QueryNewsComment(newsObj,userObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<NewsComments>").append("\r\n");
			for (int i = 0; i < newsCommentList.size(); i++) {
				sb.append("	<NewsComment>").append("\r\n")
				.append("		<commentId>")
				.append(newsCommentList.get(i).getCommentId())
				.append("</commentId>").append("\r\n")
				.append("		<newsObj>")
				.append(newsCommentList.get(i).getNewsObj())
				.append("</newsObj>").append("\r\n")
				.append("		<userObj>")
				.append(newsCommentList.get(i).getUserObj())
				.append("</userObj>").append("\r\n")
				.append("		<content>")
				.append(newsCommentList.get(i).getContent())
				.append("</content>").append("\r\n")
				.append("		<commentTime>")
				.append(newsCommentList.get(i).getCommentTime())
				.append("</commentTime>").append("\r\n")
				.append("	</NewsComment>").append("\r\n");
			}
			sb.append("</NewsComments>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(NewsComment newsComment: newsCommentList) {
				  stringer.object();
			  stringer.key("commentId").value(newsComment.getCommentId());
			  stringer.key("newsObj").value(newsComment.getNewsObj());
			  stringer.key("userObj").value(newsComment.getUserObj());
			  stringer.key("content").value(newsComment.getContent());
			  stringer.key("commentTime").value(newsComment.getCommentTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ����������ۣ���ȡ�������۲������������浽�½����������۶��� */ 
			NewsComment newsComment = new NewsComment();
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			newsComment.setCommentId(commentId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			newsComment.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			newsComment.setUserObj(userObj);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			newsComment.setContent(content);
			String commentTime = new String(request.getParameter("commentTime").getBytes("iso-8859-1"), "UTF-8");
			newsComment.setCommentTime(commentTime);

			/* ����ҵ���ִ����Ӳ��� */
			String result = newsCommentDAO.AddNewsComment(newsComment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ���������ۣ���ȡ�������۵�����id*/
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = newsCommentDAO.DeleteNewsComment(commentId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*������������֮ǰ�ȸ���commentId��ѯĳ����������*/
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			NewsComment newsComment = newsCommentDAO.GetNewsComment(commentId);

			// �ͻ��˲�ѯ���������۶��󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("commentId").value(newsComment.getCommentId());
			  stringer.key("newsObj").value(newsComment.getNewsObj());
			  stringer.key("userObj").value(newsComment.getUserObj());
			  stringer.key("content").value(newsComment.getContent());
			  stringer.key("commentTime").value(newsComment.getCommentTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* �����������ۣ���ȡ�������۲������������浽�½����������۶��� */ 
			NewsComment newsComment = new NewsComment();
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			newsComment.setCommentId(commentId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			newsComment.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			newsComment.setUserObj(userObj);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			newsComment.setContent(content);
			String commentTime = new String(request.getParameter("commentTime").getBytes("iso-8859-1"), "UTF-8");
			newsComment.setCommentTime(commentTime);

			/* ����ҵ���ִ�и��²��� */
			String result = newsCommentDAO.UpdateNewsComment(newsComment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
