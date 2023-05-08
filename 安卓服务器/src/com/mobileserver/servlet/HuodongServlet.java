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

	/*������Ϣҵ������*/
	private HuodongDAO huodongDAO = new HuodongDAO();

	/*Ĭ�Ϲ��캯��*/
	public HuodongServlet() {
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
			/*��ȡ��ѯ���Ϣ�Ĳ�����Ϣ*/
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

			/*����ҵ���߼���ִ�л��Ϣ��ѯ*/
			List<Huodong> huodongList = huodongDAO.QueryHuodong(title,telephone,userObj,addTime);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��ӻ��Ϣ����ȡ���Ϣ�������������浽�½��Ļ��Ϣ���� */ 
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

			/* ����ҵ���ִ����Ӳ��� */
			String result = huodongDAO.AddHuodong(huodong);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ�����Ϣ����ȡ���Ϣ�Ļid*/
			int huodongId = Integer.parseInt(request.getParameter("huodongId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = huodongDAO.DeleteHuodong(huodongId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���»��Ϣ֮ǰ�ȸ���huodongId��ѯĳ�����Ϣ*/
			int huodongId = Integer.parseInt(request.getParameter("huodongId"));
			Huodong huodong = huodongDAO.GetHuodong(huodongId);

			// �ͻ��˲�ѯ�Ļ��Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���»��Ϣ����ȡ���Ϣ�������������浽�½��Ļ��Ϣ���� */ 
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

			/* ����ҵ���ִ�и��²��� */
			String result = huodongDAO.UpdateHuodong(huodong);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
