package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.VideoShareDAO;
import com.mobileserver.domain.VideoShare;

import org.json.JSONStringer;

public class VideoShareServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造视频分享业务层对象*/
	private VideoShareDAO videoShareDAO = new VideoShareDAO();

	/*默认构造函数*/
	public VideoShareServlet() {
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
			/*获取查询视频分享的参数信息*/
			String videoTitle = request.getParameter("videoTitle");
			videoTitle = videoTitle == null ? "" : new String(request.getParameter(
					"videoTitle").getBytes("iso-8859-1"), "UTF-8");
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");
			String shareTime = request.getParameter("shareTime");
			shareTime = shareTime == null ? "" : new String(request.getParameter(
					"shareTime").getBytes("iso-8859-1"), "UTF-8");

			/*调用业务逻辑层执行视频分享查询*/
			List<VideoShare> videoShareList = videoShareDAO.QueryVideoShare(videoTitle,userObj,shareTime);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<VideoShares>").append("\r\n");
			for (int i = 0; i < videoShareList.size(); i++) {
				sb.append("	<VideoShare>").append("\r\n")
				.append("		<videoShareId>")
				.append(videoShareList.get(i).getVideoShareId())
				.append("</videoShareId>").append("\r\n")
				.append("		<videoTitle>")
				.append(videoShareList.get(i).getVideoTitle())
				.append("</videoTitle>").append("\r\n")
				.append("		<videoFile>")
				.append(videoShareList.get(i).getVideoFile())
				.append("</videoFile>").append("\r\n")
				.append("		<userObj>")
				.append(videoShareList.get(i).getUserObj())
				.append("</userObj>").append("\r\n")
				.append("		<shareTime>")
				.append(videoShareList.get(i).getShareTime())
				.append("</shareTime>").append("\r\n")
				.append("	</VideoShare>").append("\r\n");
			}
			sb.append("</VideoShares>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(VideoShare videoShare: videoShareList) {
				  stringer.object();
			  stringer.key("videoShareId").value(videoShare.getVideoShareId());
			  stringer.key("videoTitle").value(videoShare.getVideoTitle());
			  stringer.key("videoFile").value(videoShare.getVideoFile());
			  stringer.key("userObj").value(videoShare.getUserObj());
			  stringer.key("shareTime").value(videoShare.getShareTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加视频分享：获取视频分享参数，参数保存到新建的视频分享对象 */ 
			VideoShare videoShare = new VideoShare();
			int videoShareId = Integer.parseInt(request.getParameter("videoShareId"));
			videoShare.setVideoShareId(videoShareId);
			String videoTitle = new String(request.getParameter("videoTitle").getBytes("iso-8859-1"), "UTF-8");
			videoShare.setVideoTitle(videoTitle);
			String videoFile = new String(request.getParameter("videoFile").getBytes("iso-8859-1"), "UTF-8");
			videoShare.setVideoFile(videoFile);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			videoShare.setUserObj(userObj);
			String shareTime = new String(request.getParameter("shareTime").getBytes("iso-8859-1"), "UTF-8");
			videoShare.setShareTime(shareTime);

			/* 调用业务层执行添加操作 */
			String result = videoShareDAO.AddVideoShare(videoShare);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除视频分享：获取视频分享的分享id*/
			int videoShareId = Integer.parseInt(request.getParameter("videoShareId"));
			/*调用业务逻辑层执行删除操作*/
			String result = videoShareDAO.DeleteVideoShare(videoShareId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新视频分享之前先根据videoShareId查询某个视频分享*/
			int videoShareId = Integer.parseInt(request.getParameter("videoShareId"));
			VideoShare videoShare = videoShareDAO.GetVideoShare(videoShareId);

			// 客户端查询的视频分享对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("videoShareId").value(videoShare.getVideoShareId());
			  stringer.key("videoTitle").value(videoShare.getVideoTitle());
			  stringer.key("videoFile").value(videoShare.getVideoFile());
			  stringer.key("userObj").value(videoShare.getUserObj());
			  stringer.key("shareTime").value(videoShare.getShareTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新视频分享：获取视频分享参数，参数保存到新建的视频分享对象 */ 
			VideoShare videoShare = new VideoShare();
			int videoShareId = Integer.parseInt(request.getParameter("videoShareId"));
			videoShare.setVideoShareId(videoShareId);
			String videoTitle = new String(request.getParameter("videoTitle").getBytes("iso-8859-1"), "UTF-8");
			videoShare.setVideoTitle(videoTitle);
			String videoFile = new String(request.getParameter("videoFile").getBytes("iso-8859-1"), "UTF-8");
			videoShare.setVideoFile(videoFile);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			videoShare.setUserObj(userObj);
			String shareTime = new String(request.getParameter("shareTime").getBytes("iso-8859-1"), "UTF-8");
			videoShare.setShareTime(shareTime);

			/* 调用业务层执行更新操作 */
			String result = videoShareDAO.UpdateVideoShare(videoShare);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
