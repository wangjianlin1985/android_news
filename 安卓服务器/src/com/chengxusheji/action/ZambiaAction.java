package com.chengxusheji.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionContext;
import com.chengxusheji.dao.ZambiaDAO;
import com.chengxusheji.domain.Zambia;
import com.chengxusheji.dao.NewsDAO;
import com.chengxusheji.domain.News;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class ZambiaAction extends BaseAction {

    /*�������Ҫ��ѯ������: ��������*/
    private News newsObj;
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }
    public News getNewsObj() {
        return this.newsObj;
    }

    /*�������Ҫ��ѯ������: �û�*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*��ǰ�ڼ�ҳ*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*һ������ҳ*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    private int zambiaId;
    public void setZambiaId(int zambiaId) {
        this.zambiaId = zambiaId;
    }
    public int getZambiaId() {
        return zambiaId;
    }

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource NewsDAO newsDAO;
    @Resource UserInfoDAO userInfoDAO;
    @Resource ZambiaDAO zambiaDAO;

    /*��������Zambia����*/
    private Zambia zambia;
    public void setZambia(Zambia zambia) {
        this.zambia = zambia;
    }
    public Zambia getZambia() {
        return this.zambia;
    }

    /*��ת�����Zambia��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�News��Ϣ*/
        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        /*��ѯ���е�UserInfo��Ϣ*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*���Zambia��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddZambia() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(zambia.getNewsObj().getNewsId());
            zambia.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(zambia.getUserObj().getUser_name());
            zambia.setUserObj(userObj);
            zambiaDAO.AddZambia(zambia);
            ctx.put("message",  java.net.URLEncoder.encode("Zambia��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Zambia���ʧ��!"));
            return "error";
        }
    }

    /*��ѯZambia��Ϣ*/
    public String QueryZambia() {
        if(currentPage == 0) currentPage = 1;
        List<Zambia> zambiaList = zambiaDAO.QueryZambiaInfo(newsObj, userObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        zambiaDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = zambiaDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = zambiaDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("zambiaList",  zambiaList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("newsObj", newsObj);
        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryZambiaOutputToExcel() { 
        List<Zambia> zambiaList = zambiaDAO.QueryZambiaInfo(newsObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Zambia��Ϣ��¼"; 
        String[] headers = { "��id","��������","�û�","����ʱ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<zambiaList.size();i++) {
        	Zambia zambia = zambiaList.get(i); 
        	dataset.add(new String[]{zambia.getZambiaId() + "",zambia.getNewsObj().getNewsTitle(),
zambia.getUserObj().getName(),
zambia.getZambiaTime()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Zambia.xls");//filename�����ص�xls���������������Ӣ�� 
			response.setContentType("application/msexcel;charset=UTF-8");//�������� 
			response.setHeader("Pragma","No-cache");//����ͷ 
			response.setHeader("Cache-Control","no-cache");//����ͷ 
			response.setDateHeader("Expires", 0);//��������ͷ  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
    /*ǰ̨��ѯZambia��Ϣ*/
    public String FrontQueryZambia() {
        if(currentPage == 0) currentPage = 1;
        List<Zambia> zambiaList = zambiaDAO.QueryZambiaInfo(newsObj, userObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        zambiaDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = zambiaDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = zambiaDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("zambiaList",  zambiaList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("newsObj", newsObj);
        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Zambia��Ϣ*/
    public String ModifyZambiaQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������zambiaId��ȡZambia����*/
        Zambia zambia = zambiaDAO.GetZambiaByZambiaId(zambiaId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("zambia",  zambia);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Zambia��Ϣ*/
    public String FrontShowZambiaQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������zambiaId��ȡZambia����*/
        Zambia zambia = zambiaDAO.GetZambiaByZambiaId(zambiaId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("zambia",  zambia);
        return "front_show_view";
    }

    /*�����޸�Zambia��Ϣ*/
    public String ModifyZambia() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(zambia.getNewsObj().getNewsId());
            zambia.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(zambia.getUserObj().getUser_name());
            zambia.setUserObj(userObj);
            zambiaDAO.UpdateZambia(zambia);
            ctx.put("message",  java.net.URLEncoder.encode("Zambia��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Zambia��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Zambia��Ϣ*/
    public String DeleteZambia() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            zambiaDAO.DeleteZambia(zambiaId);
            ctx.put("message",  java.net.URLEncoder.encode("Zambiaɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Zambiaɾ��ʧ��!"));
            return "error";
        }
    }

}
