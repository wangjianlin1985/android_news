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
import com.chengxusheji.dao.NewsCollectionDAO;
import com.chengxusheji.domain.NewsCollection;
import com.chengxusheji.dao.NewsDAO;
import com.chengxusheji.domain.News;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class NewsCollectionAction extends BaseAction {

    /*�������Ҫ��ѯ������: ���ղ�����*/
    private News newsObj;
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }
    public News getNewsObj() {
        return this.newsObj;
    }

    /*�������Ҫ��ѯ������: �ղ���*/
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

    private int collectionId;
    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }
    public int getCollectionId() {
        return collectionId;
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
    @Resource NewsCollectionDAO newsCollectionDAO;

    /*��������NewsCollection����*/
    private NewsCollection newsCollection;
    public void setNewsCollection(NewsCollection newsCollection) {
        this.newsCollection = newsCollection;
    }
    public NewsCollection getNewsCollection() {
        return this.newsCollection;
    }

    /*��ת�����NewsCollection��ͼ*/
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

    /*���NewsCollection��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddNewsCollection() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(newsCollection.getNewsObj().getNewsId());
            newsCollection.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(newsCollection.getUserObj().getUser_name());
            newsCollection.setUserObj(userObj);
            newsCollectionDAO.AddNewsCollection(newsCollection);
            ctx.put("message",  java.net.URLEncoder.encode("NewsCollection��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsCollection���ʧ��!"));
            return "error";
        }
    }

    /*��ѯNewsCollection��Ϣ*/
    public String QueryNewsCollection() {
        if(currentPage == 0) currentPage = 1;
        List<NewsCollection> newsCollectionList = newsCollectionDAO.QueryNewsCollectionInfo(newsObj, userObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        newsCollectionDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = newsCollectionDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = newsCollectionDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("newsCollectionList",  newsCollectionList);
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
    public String QueryNewsCollectionOutputToExcel() { 
        List<NewsCollection> newsCollectionList = newsCollectionDAO.QueryNewsCollectionInfo(newsObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "NewsCollection��Ϣ��¼"; 
        String[] headers = { "�ղ�id","���ղ�����","�ղ���","�ղ�ʱ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<newsCollectionList.size();i++) {
        	NewsCollection newsCollection = newsCollectionList.get(i); 
        	dataset.add(new String[]{newsCollection.getCollectionId() + "",newsCollection.getNewsObj().getNewsTitle(),
newsCollection.getUserObj().getName(),
newsCollection.getCollectTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"NewsCollection.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯNewsCollection��Ϣ*/
    public String FrontQueryNewsCollection() {
        if(currentPage == 0) currentPage = 1;
        List<NewsCollection> newsCollectionList = newsCollectionDAO.QueryNewsCollectionInfo(newsObj, userObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        newsCollectionDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = newsCollectionDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = newsCollectionDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("newsCollectionList",  newsCollectionList);
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

    /*��ѯҪ�޸ĵ�NewsCollection��Ϣ*/
    public String ModifyNewsCollectionQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������collectionId��ȡNewsCollection����*/
        NewsCollection newsCollection = newsCollectionDAO.GetNewsCollectionByCollectionId(collectionId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("newsCollection",  newsCollection);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�NewsCollection��Ϣ*/
    public String FrontShowNewsCollectionQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������collectionId��ȡNewsCollection����*/
        NewsCollection newsCollection = newsCollectionDAO.GetNewsCollectionByCollectionId(collectionId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("newsCollection",  newsCollection);
        return "front_show_view";
    }

    /*�����޸�NewsCollection��Ϣ*/
    public String ModifyNewsCollection() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(newsCollection.getNewsObj().getNewsId());
            newsCollection.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(newsCollection.getUserObj().getUser_name());
            newsCollection.setUserObj(userObj);
            newsCollectionDAO.UpdateNewsCollection(newsCollection);
            ctx.put("message",  java.net.URLEncoder.encode("NewsCollection��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsCollection��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��NewsCollection��Ϣ*/
    public String DeleteNewsCollection() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            newsCollectionDAO.DeleteNewsCollection(collectionId);
            ctx.put("message",  java.net.URLEncoder.encode("NewsCollectionɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsCollectionɾ��ʧ��!"));
            return "error";
        }
    }

}
