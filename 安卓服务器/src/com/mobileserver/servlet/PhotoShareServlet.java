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

	/*����ͼƬ����ҵ������*/
	private PhotoShareDAO photoShareDAO = new PhotoShareDAO();

	/*Ĭ�Ϲ��캯��*/
	public PhotoShareServlet() {
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
			/*��ȡ��ѯͼƬ����Ĳ�����Ϣ*/
			String userInfoObj = "";
			if (request.getParameter("userInfoObj") != null)
				userInfoObj = request.getParameter("userInfoObj");
			String shareTime = request.getParameter("shareTime");
			shareTime = shareTime == null ? "" : new String(request.getParameter(
					"shareTime").getBytes("iso-8859-1"), "UTF-8");

			/*����ҵ���߼���ִ��ͼƬ�����ѯ*/
			List<PhotoShare> photoShareList = photoShareDAO.QueryPhotoShare(userInfoObj,shareTime);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���ͼƬ������ȡͼƬ����������������浽�½���ͼƬ������� */ 
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

			/* ����ҵ���ִ����Ӳ��� */
			String result = photoShareDAO.AddPhotoShare(photoShare);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��ͼƬ������ȡͼƬ����ķ���id*/
			int sharePhotoId = Integer.parseInt(request.getParameter("sharePhotoId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = photoShareDAO.DeletePhotoShare(sharePhotoId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����ͼƬ����֮ǰ�ȸ���sharePhotoId��ѯĳ��ͼƬ����*/
			int sharePhotoId = Integer.parseInt(request.getParameter("sharePhotoId"));
			PhotoShare photoShare = photoShareDAO.GetPhotoShare(sharePhotoId);

			// �ͻ��˲�ѯ��ͼƬ������󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����ͼƬ������ȡͼƬ����������������浽�½���ͼƬ������� */ 
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

			/* ����ҵ���ִ�и��²��� */
			String result = photoShareDAO.UpdatePhotoShare(photoShare);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
