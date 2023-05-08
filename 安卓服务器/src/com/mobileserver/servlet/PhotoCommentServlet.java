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

	/*����ͼƬ����ҵ������*/
	private PhotoCommentDAO photoCommentDAO = new PhotoCommentDAO();

	/*Ĭ�Ϲ��캯��*/
	public PhotoCommentServlet() {
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
			/*��ȡ��ѯͼƬ���۵Ĳ�����Ϣ*/
			int photoObj = 0;
			if (request.getParameter("photoObj") != null)
				photoObj = Integer.parseInt(request.getParameter("photoObj"));
			String content = request.getParameter("content");
			content = content == null ? "" : new String(request.getParameter(
					"content").getBytes("iso-8859-1"), "UTF-8");
			String userInfoObj = "";
			if (request.getParameter("userInfoObj") != null)
				userInfoObj = request.getParameter("userInfoObj");

			/*����ҵ���߼���ִ��ͼƬ���۲�ѯ*/
			List<PhotoComment> photoCommentList = photoCommentDAO.QueryPhotoComment(photoObj,content,userInfoObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���ͼƬ���ۣ���ȡͼƬ���۲������������浽�½���ͼƬ���۶��� */ 
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

			/* ����ҵ���ִ����Ӳ��� */
			String result = photoCommentDAO.AddPhotoComment(photoComment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��ͼƬ���ۣ���ȡͼƬ���۵�����id*/
			int photoCommentId = Integer.parseInt(request.getParameter("photoCommentId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = photoCommentDAO.DeletePhotoComment(photoCommentId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����ͼƬ����֮ǰ�ȸ���photoCommentId��ѯĳ��ͼƬ����*/
			int photoCommentId = Integer.parseInt(request.getParameter("photoCommentId"));
			PhotoComment photoComment = photoCommentDAO.GetPhotoComment(photoCommentId);

			// �ͻ��˲�ѯ��ͼƬ���۶��󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����ͼƬ���ۣ���ȡͼƬ���۲������������浽�½���ͼƬ���۶��� */ 
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

			/* ����ҵ���ִ�и��²��� */
			String result = photoCommentDAO.UpdatePhotoComment(photoComment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
