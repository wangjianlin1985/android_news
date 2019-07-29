package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.ZambiaDAO;
import com.mobileserver.domain.Zambia;

import org.json.JSONStringer;

public class ZambiaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造新闻赞业务层对象*/
	private ZambiaDAO zambiaDAO = new ZambiaDAO();

	/*默认构造函数*/
	public ZambiaServlet() {
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
			/*获取查询新闻赞的参数信息*/
			int newsObj = 0;
			if (request.getParameter("newsObj") != null)
				newsObj = Integer.parseInt(request.getParameter("newsObj"));
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");

			/*调用业务逻辑层执行新闻赞查询*/
			List<Zambia> zambiaList = zambiaDAO.QueryZambia(newsObj,userObj);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Zambias>").append("\r\n");
			for (int i = 0; i < zambiaList.size(); i++) {
				sb.append("	<Zambia>").append("\r\n")
				.append("		<zambiaId>")
				.append(zambiaList.get(i).getZambiaId())
				.append("</zambiaId>").append("\r\n")
				.append("		<newsObj>")
				.append(zambiaList.get(i).getNewsObj())
				.append("</newsObj>").append("\r\n")
				.append("		<userObj>")
				.append(zambiaList.get(i).getUserObj())
				.append("</userObj>").append("\r\n")
				.append("		<zambiaTime>")
				.append(zambiaList.get(i).getZambiaTime())
				.append("</zambiaTime>").append("\r\n")
				.append("	</Zambia>").append("\r\n");
			}
			sb.append("</Zambias>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(Zambia zambia: zambiaList) {
				  stringer.object();
			  stringer.key("zambiaId").value(zambia.getZambiaId());
			  stringer.key("newsObj").value(zambia.getNewsObj());
			  stringer.key("userObj").value(zambia.getUserObj());
			  stringer.key("zambiaTime").value(zambia.getZambiaTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加新闻赞：获取新闻赞参数，参数保存到新建的新闻赞对象 */ 
			Zambia zambia = new Zambia();
			int zambiaId = Integer.parseInt(request.getParameter("zambiaId"));
			zambia.setZambiaId(zambiaId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			zambia.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			zambia.setUserObj(userObj);
			String zambiaTime = new String(request.getParameter("zambiaTime").getBytes("iso-8859-1"), "UTF-8");
			zambia.setZambiaTime(zambiaTime);

			/* 调用业务层执行添加操作 */
			String result = zambiaDAO.AddZambia(zambia);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除新闻赞：获取新闻赞的赞id*/
			int zambiaId = Integer.parseInt(request.getParameter("zambiaId"));
			/*调用业务逻辑层执行删除操作*/
			String result = zambiaDAO.DeleteZambia(zambiaId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新新闻赞之前先根据zambiaId查询某个新闻赞*/
			int zambiaId = Integer.parseInt(request.getParameter("zambiaId"));
			Zambia zambia = zambiaDAO.GetZambia(zambiaId);

			// 客户端查询的新闻赞对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("zambiaId").value(zambia.getZambiaId());
			  stringer.key("newsObj").value(zambia.getNewsObj());
			  stringer.key("userObj").value(zambia.getUserObj());
			  stringer.key("zambiaTime").value(zambia.getZambiaTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新新闻赞：获取新闻赞参数，参数保存到新建的新闻赞对象 */ 
			Zambia zambia = new Zambia();
			int zambiaId = Integer.parseInt(request.getParameter("zambiaId"));
			zambia.setZambiaId(zambiaId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			zambia.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			zambia.setUserObj(userObj);
			String zambiaTime = new String(request.getParameter("zambiaTime").getBytes("iso-8859-1"), "UTF-8");
			zambia.setZambiaTime(zambiaTime);

			/* 调用业务层执行更新操作 */
			String result = zambiaDAO.UpdateZambia(zambia);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
