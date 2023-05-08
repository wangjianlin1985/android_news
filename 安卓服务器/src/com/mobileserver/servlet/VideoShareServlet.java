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

	/*������Ƶ����ҵ������*/
	private VideoShareDAO videoShareDAO = new VideoShareDAO();

	/*Ĭ�Ϲ��캯��*/
	public VideoShareServlet() {
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
			/*��ȡ��ѯ��Ƶ����Ĳ�����Ϣ*/
			String videoTitle = request.getParameter("videoTitle");
			videoTitle = videoTitle == null ? "" : new String(request.getParameter(
					"videoTitle").getBytes("iso-8859-1"), "UTF-8");
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");
			String shareTime = request.getParameter("shareTime");
			shareTime = shareTime == null ? "" : new String(request.getParameter(
					"shareTime").getBytes("iso-8859-1"), "UTF-8");

			/*����ҵ���߼���ִ����Ƶ�����ѯ*/
			List<VideoShare> videoShareList = videoShareDAO.QueryVideoShare(videoTitle,userObj,shareTime);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* �����Ƶ������ȡ��Ƶ����������������浽�½�����Ƶ������� */ 
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

			/* ����ҵ���ִ����Ӳ��� */
			String result = videoShareDAO.AddVideoShare(videoShare);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ����Ƶ������ȡ��Ƶ����ķ���id*/
			int videoShareId = Integer.parseInt(request.getParameter("videoShareId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = videoShareDAO.DeleteVideoShare(videoShareId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*������Ƶ����֮ǰ�ȸ���videoShareId��ѯĳ����Ƶ����*/
			int videoShareId = Integer.parseInt(request.getParameter("videoShareId"));
			VideoShare videoShare = videoShareDAO.GetVideoShare(videoShareId);

			// �ͻ��˲�ѯ����Ƶ������󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ������Ƶ������ȡ��Ƶ����������������浽�½�����Ƶ������� */ 
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

			/* ����ҵ���ִ�и��²��� */
			String result = videoShareDAO.UpdateVideoShare(videoShare);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
