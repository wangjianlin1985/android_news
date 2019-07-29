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

	/*构造新闻评论业务层对象*/
	private NewsCommentDAO newsCommentDAO = new NewsCommentDAO();

	/*默认构造函数*/
	public NewsCommentServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*获取action参数，根据action的值执行不同的业务处理*/
		String action = request.getParameter("action");
		if (action.equals("query")) {
			/*获取查询新闻评论的参数信息*/
			int newsObj = 0;
			if (request.getParameter("newsObj") != null)
				newsObj = Integer.parseInt(request.getParameter("newsObj"));
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");

			/*调用业务逻辑层执行新闻评论查询*/
			List<NewsComment> newsCommentList = newsCommentDAO.QueryNewsComment(newsObj,userObj);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
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
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
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
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加新闻评论：获取新闻评论参数，参数保存到新建的新闻评论对象 */ 
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

			/* 调用业务层执行添加操作 */
			String result = newsCommentDAO.AddNewsComment(newsComment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除新闻评论：获取新闻评论的评论id*/
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			/*调用业务逻辑层执行删除操作*/
			String result = newsCommentDAO.DeleteNewsComment(commentId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新新闻评论之前先根据commentId查询某个新闻评论*/
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			NewsComment newsComment = newsCommentDAO.GetNewsComment(commentId);

			// 客户端查询的新闻评论对象，返回json数据格式, 将List<Book>组织成JSON字符串
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
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新新闻评论：获取新闻评论参数，参数保存到新建的新闻评论对象 */ 
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

			/* 调用业务层执行更新操作 */
			String result = newsCommentDAO.UpdateNewsComment(newsComment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
