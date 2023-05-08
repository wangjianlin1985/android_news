package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.HuodongDAO;
import com.mobileserver.domain.Huodong;

import org.json.JSONStringer;

public class HuodongServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造活动信息业务层对象*/
	private HuodongDAO huodongDAO = new HuodongDAO();

	/*默认构造函数*/
	public HuodongServlet() {
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
			/*获取查询活动信息的参数信息*/
			String title = request.getParameter("title");
			title = title == null ? "" : new String(request.getParameter(
					"title").getBytes("iso-8859-1"), "UTF-8");
			String telephone = request.getParameter("telephone");
			telephone = telephone == null ? "" : new String(request.getParameter(
					"telephone").getBytes("iso-8859-1"), "UTF-8");
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");
			String addTime = request.getParameter("addTime");
			addTime = addTime == null ? "" : new String(request.getParameter(
					"addTime").getBytes("iso-8859-1"), "UTF-8");

			/*调用业务逻辑层执行活动信息查询*/
			List<Huodong> huodongList = huodongDAO.QueryHuodong(title,telephone,userObj,addTime);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Huodongs>").append("\r\n");
			for (int i = 0; i < huodongList.size(); i++) {
				sb.append("	<Huodong>").append("\r\n")
				.append("		<huodongId>")
				.append(huodongList.get(i).getHuodongId())
				.append("</huodongId>").append("\r\n")
				.append("		<title>")
				.append(huodongList.get(i).getTitle())
				.append("</title>").append("\r\n")
				.append("		<content>")
				.append(huodongList.get(i).getContent())
				.append("</content>").append("\r\n")
				.append("		<telephone>")
				.append(huodongList.get(i).getTelephone())
				.append("</telephone>").append("\r\n")
				.append("		<personList>")
				.append(huodongList.get(i).getPersonList())
				.append("</personList>").append("\r\n")
				.append("		<userObj>")
				.append(huodongList.get(i).getUserObj())
				.append("</userObj>").append("\r\n")
				.append("		<addTime>")
				.append(huodongList.get(i).getAddTime())
				.append("</addTime>").append("\r\n")
				.append("	</Huodong>").append("\r\n");
			}
			sb.append("</Huodongs>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(Huodong huodong: huodongList) {
				  stringer.object();
			  stringer.key("huodongId").value(huodong.getHuodongId());
			  stringer.key("title").value(huodong.getTitle());
			  stringer.key("content").value(huodong.getContent());
			  stringer.key("telephone").value(huodong.getTelephone());
			  stringer.key("personList").value(huodong.getPersonList());
			  stringer.key("userObj").value(huodong.getUserObj());
			  stringer.key("addTime").value(huodong.getAddTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加活动信息：获取活动信息参数，参数保存到新建的活动信息对象 */ 
			Huodong huodong = new Huodong();
			int huodongId = Integer.parseInt(request.getParameter("huodongId"));
			huodong.setHuodongId(huodongId);
			String title = new String(request.getParameter("title").getBytes("iso-8859-1"), "UTF-8");
			huodong.setTitle(title);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			huodong.setContent(content);
			String telephone = new String(request.getParameter("telephone").getBytes("iso-8859-1"), "UTF-8");
			huodong.setTelephone(telephone);
			String personList = new String(request.getParameter("personList").getBytes("iso-8859-1"), "UTF-8");
			huodong.setPersonList(personList);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			huodong.setUserObj(userObj);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			huodong.setAddTime(addTime);

			/* 调用业务层执行添加操作 */
			String result = huodongDAO.AddHuodong(huodong);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除活动信息：获取活动信息的活动id*/
			int huodongId = Integer.parseInt(request.getParameter("huodongId"));
			/*调用业务逻辑层执行删除操作*/
			String result = huodongDAO.DeleteHuodong(huodongId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新活动信息之前先根据huodongId查询某个活动信息*/
			int huodongId = Integer.parseInt(request.getParameter("huodongId"));
			Huodong huodong = huodongDAO.GetHuodong(huodongId);

			// 客户端查询的活动信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("huodongId").value(huodong.getHuodongId());
			  stringer.key("title").value(huodong.getTitle());
			  stringer.key("content").value(huodong.getContent());
			  stringer.key("telephone").value(huodong.getTelephone());
			  stringer.key("personList").value(huodong.getPersonList());
			  stringer.key("userObj").value(huodong.getUserObj());
			  stringer.key("addTime").value(huodong.getAddTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新活动信息：获取活动信息参数，参数保存到新建的活动信息对象 */ 
			Huodong huodong = new Huodong();
			int huodongId = Integer.parseInt(request.getParameter("huodongId"));
			huodong.setHuodongId(huodongId);
			String title = new String(request.getParameter("title").getBytes("iso-8859-1"), "UTF-8");
			huodong.setTitle(title);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			huodong.setContent(content);
			String telephone = new String(request.getParameter("telephone").getBytes("iso-8859-1"), "UTF-8");
			huodong.setTelephone(telephone);
			String personList = new String(request.getParameter("personList").getBytes("iso-8859-1"), "UTF-8");
			huodong.setPersonList(personList);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			huodong.setUserObj(userObj);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			huodong.setAddTime(addTime);

			/* 调用业务层执行更新操作 */
			String result = huodongDAO.UpdateHuodong(huodong);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
