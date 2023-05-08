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

	/*����������ҵ������*/
	private ZambiaDAO zambiaDAO = new ZambiaDAO();

	/*Ĭ�Ϲ��캯��*/
	public ZambiaServlet() {
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
			/*��ȡ��ѯ�����޵Ĳ�����Ϣ*/
			int newsObj = 0;
			if (request.getParameter("newsObj") != null)
				newsObj = Integer.parseInt(request.getParameter("newsObj"));
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");

			/*����ҵ���߼���ִ�������޲�ѯ*/
			List<Zambia> zambiaList = zambiaDAO.QueryZambia(newsObj,userObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��������ޣ���ȡ�����޲������������浽�½��������޶��� */ 
			Zambia zambia = new Zambia();
			int zambiaId = Integer.parseInt(request.getParameter("zambiaId"));
			zambia.setZambiaId(zambiaId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			zambia.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			zambia.setUserObj(userObj);
			String zambiaTime = new String(request.getParameter("zambiaTime").getBytes("iso-8859-1"), "UTF-8");
			zambia.setZambiaTime(zambiaTime);

			/* ����ҵ���ִ����Ӳ��� */
			String result = zambiaDAO.AddZambia(zambia);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ�������ޣ���ȡ�����޵���id*/
			int zambiaId = Integer.parseInt(request.getParameter("zambiaId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = zambiaDAO.DeleteZambia(zambiaId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����������֮ǰ�ȸ���zambiaId��ѯĳ��������*/
			int zambiaId = Integer.parseInt(request.getParameter("zambiaId"));
			Zambia zambia = zambiaDAO.GetZambia(zambiaId);

			// �ͻ��˲�ѯ�������޶��󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���������ޣ���ȡ�����޲������������浽�½��������޶��� */ 
			Zambia zambia = new Zambia();
			int zambiaId = Integer.parseInt(request.getParameter("zambiaId"));
			zambia.setZambiaId(zambiaId);
			int newsObj = Integer.parseInt(request.getParameter("newsObj"));
			zambia.setNewsObj(newsObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			zambia.setUserObj(userObj);
			String zambiaTime = new String(request.getParameter("zambiaTime").getBytes("iso-8859-1"), "UTF-8");
			zambia.setZambiaTime(zambiaTime);

			/* ����ҵ���ִ�и��²��� */
			String result = zambiaDAO.UpdateZambia(zambia);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
