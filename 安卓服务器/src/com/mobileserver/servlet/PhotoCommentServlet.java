package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.PhotoCommentDAO;
import com.mobileserver.domain.PhotoComment;

import org.json.JSONStringer;

public class PhotoCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造图片评论业务层对象*/
	private PhotoCommentDAO photoCommentDAO = new PhotoCommentDAO();

	/*默认构造函数*/
	public PhotoCommentServlet() {
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
			/*获取查询图片评论的参数信息*/
			int photoObj = 0;
			if (request.getParameter("photoObj") != null)
				photoObj = Integer.parseInt(request.getParameter("photoObj"));
			String content = request.getParameter("content");
			content = content == null ? "" : new String(request.getParameter(
					"content").getBytes("iso-8859-1"), "UTF-8");
			String userInfoObj = "";
			if (request.getParameter("userInfoObj") != null)
				userInfoObj = request.getParameter("userInfoObj");

			/*调用业务逻辑层执行图片评论查询*/
			List<PhotoComment> photoCommentList = photoCommentDAO.QueryPhotoComment(photoObj,content,userInfoObj);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<PhotoComments>").append("\r\n");
			for (int i = 0; i < photoCommentList.size(); i++) {
				sb.append("	<PhotoComment>").append("\r\n")
				.append("		<photoCommentId>")
				.append(photoCommentList.get(i).getPhotoCommentId())
				.append("</photoCommentId>").append("\r\n")
				.append("		<photoObj>")
				.append(photoCommentList.get(i).getPhotoObj())
				.append("</photoObj>").append("\r\n")
				.append("		<content>")
				.append(photoCommentList.get(i).getContent())
				.append("</content>").append("\r\n")
				.append("		<userInfoObj>")
				.append(photoCommentList.get(i).getUserInfoObj())
				.append("</userInfoObj>").append("\r\n")
				.append("		<commentTime>")
				.append(photoCommentList.get(i).getCommentTime())
				.append("</commentTime>").append("\r\n")
				.append("	</PhotoComment>").append("\r\n");
			}
			sb.append("</PhotoComments>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(PhotoComment photoComment: photoCommentList) {
				  stringer.object();
			  stringer.key("photoCommentId").value(photoComment.getPhotoCommentId());
			  stringer.key("photoObj").value(photoComment.getPhotoObj());
			  stringer.key("content").value(photoComment.getContent());
			  stringer.key("userInfoObj").value(photoComment.getUserInfoObj());
			  stringer.key("commentTime").value(photoComment.getCommentTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加图片评论：获取图片评论参数，参数保存到新建的图片评论对象 */ 
			PhotoComment photoComment = new PhotoComment();
			int photoCommentId = Integer.parseInt(request.getParameter("photoCommentId"));
			photoComment.setPhotoCommentId(photoCommentId);
			int photoObj = Integer.parseInt(request.getParameter("photoObj"));
			photoComment.setPhotoObj(photoObj);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			photoComment.setContent(content);
			String userInfoObj = new String(request.getParameter("userInfoObj").getBytes("iso-8859-1"), "UTF-8");
			photoComment.setUserInfoObj(userInfoObj);
			String commentTime = new String(request.getParameter("commentTime").getBytes("iso-8859-1"), "UTF-8");
			photoComment.setCommentTime(commentTime);

			/* 调用业务层执行添加操作 */
			String result = photoCommentDAO.AddPhotoComment(photoComment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除图片评论：获取图片评论的评论id*/
			int photoCommentId = Integer.parseInt(request.getParameter("photoCommentId"));
			/*调用业务逻辑层执行删除操作*/
			String result = photoCommentDAO.DeletePhotoComment(photoCommentId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新图片评论之前先根据photoCommentId查询某个图片评论*/
			int photoCommentId = Integer.parseInt(request.getParameter("photoCommentId"));
			PhotoComment photoComment = photoCommentDAO.GetPhotoComment(photoCommentId);

			// 客户端查询的图片评论对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("photoCommentId").value(photoComment.getPhotoCommentId());
			  stringer.key("photoObj").value(photoComment.getPhotoObj());
			  stringer.key("content").value(photoComment.getContent());
			  stringer.key("userInfoObj").value(photoComment.getUserInfoObj());
			  stringer.key("commentTime").value(photoComment.getCommentTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新图片评论：获取图片评论参数，参数保存到新建的图片评论对象 */ 
			PhotoComment photoComment = new PhotoComment();
			int photoCommentId = Integer.parseInt(request.getParameter("photoCommentId"));
			photoComment.setPhotoCommentId(photoCommentId);
			int photoObj = Integer.parseInt(request.getParameter("photoObj"));
			photoComment.setPhotoObj(photoObj);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			photoComment.setContent(content);
			String userInfoObj = new String(request.getParameter("userInfoObj").getBytes("iso-8859-1"), "UTF-8");
			photoComment.setUserInfoObj(userInfoObj);
			String commentTime = new String(request.getParameter("commentTime").getBytes("iso-8859-1"), "UTF-8");
			photoComment.setCommentTime(commentTime);

			/* 调用业务层执行更新操作 */
			String result = photoCommentDAO.UpdatePhotoComment(photoComment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
