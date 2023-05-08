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

	/*构造新闻收藏业务层对象*/
	private NewsCollectionDAO newsCollectionDAO = new NewsCollectionDAO();

	/*默认构造函数*/
	public NewsCollectionServlet() {
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
			/*获取查询新闻收藏的参数信息*/
			int newsObj = 0;
			if (request.getParameter("newsObj") != null)
				newsObj = Integer.parseInt(request.getParameter("newsObj"));
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");

			/*调用业务逻辑层执行新闻收藏查询*/
			List<NewsCollection> newsCollectionList = newsCollectionDAO.QueryNewsCollection(newsObj,userObj);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
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
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
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
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加新闻收藏：获取新闻收藏参数，参数保存到新建的新闻收藏对象 */ 
			NewsCollection newsCollection = new NewsCollection();
			int collectionId = Integer.parseInt(request.getParameter("collectionId"));
			newsCollection.setCollectionId(collectionId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			newsCollection.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			newsCollection.setUserObj(userObj);
			String collectTime = new String(request.getParameter("collectTime").getBytes("iso-8859-1"), "UTF-8");
			newsCollection.setCollectTime(collectTime);

			/* 调用业务层执行添加操作 */
			String result = newsCollectionDAO.AddNewsCollection(newsCollection);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除新闻收藏：获取新闻收藏的收藏id*/
			int collectionId = Integer.parseInt(request.getParameter("collectionId"));
			/*调用业务逻辑层执行删除操作*/
			String result = newsCollectionDAO.DeleteNewsCollection(collectionId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新新闻收藏之前先根据collectionId查询某个新闻收藏*/
			int collectionId = Integer.parseInt(request.getParameter("collectionId"));
			NewsCollection newsCollection = newsCollectionDAO.GetNewsCollection(collectionId);

			// 客户端查询的新闻收藏对象，返回json数据格式, 将List<Book>组织成JSON字符串
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
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新新闻收藏：获取新闻收藏参数，参数保存到新建的新闻收藏对象 */ 
			NewsCollection newsCollection = new NewsCollection();
			int collectionId = Integer.parseInt(request.getParameter("collectionId"));
			newsCollection.setCollectionId(collectionId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			newsCollection.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			newsCollection.setUserObj(userObj);
			String collectTime = new String(request.getParameter("collectTime").getBytes("iso-8859-1"), "UTF-8");
			newsCollection.setCollectTime(collectTime);

			/* 调用业务层执行更新操作 */
			String result = newsCollectionDAO.UpdateNewsCollection(newsCollection);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
