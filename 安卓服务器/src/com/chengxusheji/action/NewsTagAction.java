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
import com.chengxusheji.dao.NewsTagDAO;
import com.chengxusheji.domain.NewsTag;
import com.chengxusheji.dao.NewsDAO;
import com.chengxusheji.domain.News;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class NewsTagAction extends BaseAction {

    /*�������Ҫ��ѯ������: ���������*/
    private News newsObj;
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }
    public News getNewsObj() {
        return this.newsObj;
    }

    /*�������Ҫ��ѯ������: ��ǵ��û�*/
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

    private int tagId;
    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
    public int getTagId() {
        return tagId;
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
    @Resource NewsTagDAO newsTagDAO;

    /*��������NewsTag����*/
    private NewsTag newsTag;
    public void setNewsTag(NewsTag newsTag) {
        this.newsTag = newsTag;
    }
    public NewsTag getNewsTag() {
        return this.newsTag;
    }

    /*��ת�����NewsTag��ͼ*/
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

    /*���NewsTag��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddNewsTag() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(newsTag.getNewsObj().getNewsId());
            newsTag.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(newsTag.getUserObj().getUser_name());
            newsTag.setUserObj(userObj);
            newsTagDAO.AddNewsTag(newsTag);
            ctx.put("message",  java.net.URLEncoder.encode("NewsTag��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsTag���ʧ��!"));
            return "error";
        }
    }

    /*��ѯNewsTag��Ϣ*/
    public String QueryNewsTag() {
        if(currentPage == 0) currentPage = 1;
        List<NewsTag> newsTagList = newsTagDAO.QueryNewsTagInfo(newsObj, userObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        newsTagDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = newsTagDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = newsTagDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("newsTagList",  newsTagList);
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
    public String QueryNewsTagOutputToExcel() { 
        List<NewsTag> newsTagList = newsTagDAO.QueryNewsTagInfo(newsObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "NewsTag��Ϣ��¼"; 
        String[] headers = { "���id","���������","��ǵ��û�","����״̬","���ʱ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<newsTagList.size();i++) {
        	NewsTag newsTag = newsTagList.get(i); 
        	dataset.add(new String[]{newsTag.getTagId() + "",newsTag.getNewsObj().getNewsTitle(),
newsTag.getUserObj().getName(),
newsTag.getNewsState() + "",newsTag.getTagTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"NewsTag.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯNewsTag��Ϣ*/
    public String FrontQueryNewsTag() {
        if(currentPage == 0) currentPage = 1;
        List<NewsTag> newsTagList = newsTagDAO.QueryNewsTagInfo(newsObj, userObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        newsTagDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = newsTagDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = newsTagDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("newsTagList",  newsTagList);
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

    /*��ѯҪ�޸ĵ�NewsTag��Ϣ*/
    public String ModifyNewsTagQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������tagId��ȡNewsTag����*/
        NewsTag newsTag = newsTagDAO.GetNewsTagByTagId(tagId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("newsTag",  newsTag);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�NewsTag��Ϣ*/
    public String FrontShowNewsTagQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������tagId��ȡNewsTag����*/
        NewsTag newsTag = newsTagDAO.GetNewsTagByTagId(tagId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("newsTag",  newsTag);
        return "front_show_view";
    }

    /*�����޸�NewsTag��Ϣ*/
    public String ModifyNewsTag() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(newsTag.getNewsObj().getNewsId());
            newsTag.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(newsTag.getUserObj().getUser_name());
            newsTag.setUserObj(userObj);
            newsTagDAO.UpdateNewsTag(newsTag);
            ctx.put("message",  java.net.URLEncoder.encode("NewsTag��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsTag��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��NewsTag��Ϣ*/
    public String DeleteNewsTag() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            newsTagDAO.DeleteNewsTag(tagId);
            ctx.put("message",  java.net.URLEncoder.encode("NewsTagɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsTagɾ��ʧ��!"));
            return "error";
        }
    }

}
