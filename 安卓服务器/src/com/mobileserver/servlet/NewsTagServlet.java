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

	/*构造新闻标记业务层对象*/
	private NewsTagDAO newsTagDAO = new NewsTagDAO();

	/*默认构造函数*/
	public NewsTagServlet() {
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
			/*获取查询新闻标记的参数信息*/
			int newsObj = 0;
			if (request.getParameter("newsObj") != null)
				newsObj = Integer.parseInt(request.getParameter("newsObj"));
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");

			/*调用业务逻辑层执行新闻标记查询*/
			List<NewsTag> newsTagList = newsTagDAO.QueryNewsTag(newsObj,userObj);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
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
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
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
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加新闻标记：获取新闻标记参数，参数保存到新建的新闻标记对象 */ 
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

			/* 调用业务层执行添加操作 */
			String result = newsTagDAO.AddNewsTag(newsTag);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除新闻标记：获取新闻标记的标记id*/
			int tagId = Integer.parseInt(request.getParameter("tagId"));
			/*调用业务逻辑层执行删除操作*/
			String result = newsTagDAO.DeleteNewsTag(tagId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新新闻标记之前先根据tagId查询某个新闻标记*/
			int tagId = Integer.parseInt(request.getParameter("tagId"));
			NewsTag newsTag = newsTagDAO.GetNewsTag(tagId);

			// 客户端查询的新闻标记对象，返回json数据格式, 将List<Book>组织成JSON字符串
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
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新新闻标记：获取新闻标记参数，参数保存到新建的新闻标记对象 */ 
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

			/* 调用业务层执行更新操作 */
			String result = newsTagDAO.UpdateNewsTag(newsTag);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
