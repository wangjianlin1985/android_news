package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.PhotoShareDAO;
import com.mobileserver.domain.PhotoShare;

import org.json.JSONStringer;

public class PhotoShareServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造图片分享业务层对象*/
	private PhotoShareDAO photoShareDAO = new PhotoShareDAO();

	/*默认构造函数*/
	public PhotoShareServlet() {
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
			/*获取查询图片分享的参数信息*/
			String userInfoObj = "";
			if (request.getParameter("userInfoObj") != null)
				userInfoObj = request.getParameter("userInfoObj");
			String shareTime = request.getParameter("shareTime");
			shareTime = shareTime == null ? "" : new String(request.getParameter(
					"shareTime").getBytes("iso-8859-1"), "UTF-8");

			/*调用业务逻辑层执行图片分享查询*/
			List<PhotoShare> photoShareList = photoShareDAO.QueryPhotoShare(userInfoObj,shareTime);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<PhotoShares>").append("\r\n");
			for (int i = 0; i < photoShareList.size(); i++) {
				sb.append("	<PhotoShare>").append("\r\n")
				.append("		<sharePhotoId>")
				.append(photoShareList.get(i).getSharePhotoId())
				.append("</sharePhotoId>").append("\r\n")
				.append("		<photoTitle>")
				.append(photoShareList.get(i).getPhotoTitle())
				.append("</photoTitle>").append("\r\n")
				.append("		<sharePhoto>")
				.append(photoShareList.get(i).getSharePhoto())
				.append("</sharePhoto>").append("\r\n")
				.append("		<userInfoObj>")
				.append(photoShareList.get(i).getUserInfoObj())
				.append("</userInfoObj>").append("\r\n")
				.append("		<shareTime>")
				.append(photoShareList.get(i).getShareTime())
				.append("</shareTime>").append("\r\n")
				.append("	</PhotoShare>").append("\r\n");
			}
			sb.append("</PhotoShares>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(PhotoShare photoShare: photoShareList) {
				  stringer.object();
			  stringer.key("sharePhotoId").value(photoShare.getSharePhotoId());
			  stringer.key("photoTitle").value(photoShare.getPhotoTitle());
			  stringer.key("sharePhoto").value(photoShare.getSharePhoto());
			  stringer.key("userInfoObj").value(photoShare.getUserInfoObj());
			  stringer.key("shareTime").value(photoShare.getShareTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加图片分享：获取图片分享参数，参数保存到新建的图片分享对象 */ 
			PhotoShare photoShare = new PhotoShare();
			int sharePhotoId = Integer.parseInt(request.getParameter("sharePhotoId"));
			photoShare.setSharePhotoId(sharePhotoId);
			String photoTitle = new String(request.getParameter("photoTitle").getBytes("iso-8859-1"), "UTF-8");
			photoShare.setPhotoTitle(photoTitle);
			String sharePhoto = new String(request.getParameter("sharePhoto").getBytes("iso-8859-1"), "UTF-8");
			photoShare.setSharePhoto(sharePhoto);
			String userInfoObj = new String(request.getParameter("userInfoObj").getBytes("iso-8859-1"), "UTF-8");
			photoShare.setUserInfoObj(userInfoObj);
			String shareTime = new String(request.getParameter("shareTime").getBytes("iso-8859-1"), "UTF-8");
			photoShare.setShareTime(shareTime);

			/* 调用业务层执行添加操作 */
			String result = photoShareDAO.AddPhotoShare(photoShare);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除图片分享：获取图片分享的分享id*/
			int sharePhotoId = Integer.parseInt(request.getParameter("sharePhotoId"));
			/*调用业务逻辑层执行删除操作*/
			String result = photoShareDAO.DeletePhotoShare(sharePhotoId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新图片分享之前先根据sharePhotoId查询某个图片分享*/
			int sharePhotoId = Integer.parseInt(request.getParameter("sharePhotoId"));
			PhotoShare photoShare = photoShareDAO.GetPhotoShare(sharePhotoId);

			// 客户端查询的图片分享对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("sharePhotoId").value(photoShare.getSharePhotoId());
			  stringer.key("photoTitle").value(photoShare.getPhotoTitle());
			  stringer.key("sharePhoto").value(photoShare.getSharePhoto());
			  stringer.key("userInfoObj").value(photoShare.getUserInfoObj());
			  stringer.key("shareTime").value(photoShare.getShareTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新图片分享：获取图片分享参数，参数保存到新建的图片分享对象 */ 
			PhotoShare photoShare = new PhotoShare();
			int sharePhotoId = Integer.parseInt(request.getParameter("sharePhotoId"));
			photoShare.setSharePhotoId(sharePhotoId);
			String photoTitle = new String(request.getParameter("photoTitle").getBytes("iso-8859-1"), "UTF-8");
			photoShare.setPhotoTitle(photoTitle);
			String sharePhoto = new String(request.getParameter("sharePhoto").getBytes("iso-8859-1"), "UTF-8");
			photoShare.setSharePhoto(sharePhoto);
			String userInfoObj = new String(request.getParameter("userInfoObj").getBytes("iso-8859-1"), "UTF-8");
			photoShare.setUserInfoObj(userInfoObj);
			String shareTime = new String(request.getParameter("shareTime").getBytes("iso-8859-1"), "UTF-8");
			photoShare.setShareTime(shareTime);

			/* 调用业务层执行更新操作 */
			String result = photoShareDAO.UpdatePhotoShare(photoShare);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
