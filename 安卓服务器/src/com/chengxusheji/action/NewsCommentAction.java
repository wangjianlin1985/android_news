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
import com.chengxusheji.dao.NewsCommentDAO;
import com.chengxusheji.domain.NewsComment;
import com.chengxusheji.dao.NewsDAO;
import com.chengxusheji.domain.News;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class NewsCommentAction extends BaseAction {

    /*�������Ҫ��ѯ������: ��������*/
    private News newsObj;
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }
    public News getNewsObj() {
        return this.newsObj;
    }

    /*�������Ҫ��ѯ������: ������*/
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

    private int commentId;
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
    public int getCommentId() {
        return commentId;
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
    @Resource NewsCommentDAO newsCommentDAO;

    /*��������NewsComment����*/
    private NewsComment newsComment;
    public void setNewsComment(NewsComment newsComment) {
        this.newsComment = newsComment;
    }
    public NewsComment getNewsComment() {
        return this.newsComment;
    }

    /*��ת�����NewsComment��ͼ*/
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

    /*���NewsComment��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddNewsComment() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(newsComment.getNewsObj().getNewsId());
            newsComment.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(newsComment.getUserObj().getUser_name());
            newsComment.setUserObj(userObj);
            newsCommentDAO.AddNewsComment(newsComment);
            ctx.put("message",  java.net.URLEncoder.encode("NewsComment��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsComment���ʧ��!"));
            return "error";
        }
    }

    /*��ѯNewsComment��Ϣ*/
    public String QueryNewsComment() {
        if(currentPage == 0) currentPage = 1;
        List<NewsComment> newsCommentList = newsCommentDAO.QueryNewsCommentInfo(newsObj, userObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        newsCommentDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = newsCommentDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = newsCommentDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("newsCommentList",  newsCommentList);
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
    public String QueryNewsCommentOutputToExcel() { 
        List<NewsComment> newsCommentList = newsCommentDAO.QueryNewsCommentInfo(newsObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "NewsComment��Ϣ��¼"; 
        String[] headers = { "����id","��������","������","��������","����ʱ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<newsCommentList.size();i++) {
        	NewsComment newsComment = newsCommentList.get(i); 
        	dataset.add(new String[]{newsComment.getCommentId() + "",newsComment.getNewsObj().getNewsTitle(),
newsComment.getUserObj().getName(),
newsComment.getContent(),newsComment.getCommentTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"NewsComment.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯNewsComment��Ϣ*/
    public String FrontQueryNewsComment() {
        if(currentPage == 0) currentPage = 1;
        List<NewsComment> newsCommentList = newsCommentDAO.QueryNewsCommentInfo(newsObj, userObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        newsCommentDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = newsCommentDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = newsCommentDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("newsCommentList",  newsCommentList);
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

    /*��ѯҪ�޸ĵ�NewsComment��Ϣ*/
    public String ModifyNewsCommentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������commentId��ȡNewsComment����*/
        NewsComment newsComment = newsCommentDAO.GetNewsCommentByCommentId(commentId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("newsComment",  newsComment);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�NewsComment��Ϣ*/
    public String FrontShowNewsCommentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������commentId��ȡNewsComment����*/
        NewsComment newsComment = newsCommentDAO.GetNewsCommentByCommentId(commentId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("newsComment",  newsComment);
        return "front_show_view";
    }

    /*�����޸�NewsComment��Ϣ*/
    public String ModifyNewsComment() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(newsComment.getNewsObj().getNewsId());
            newsComment.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(newsComment.getUserObj().getUser_name());
            newsComment.setUserObj(userObj);
            newsCommentDAO.UpdateNewsComment(newsComment);
            ctx.put("message",  java.net.URLEncoder.encode("NewsComment��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsComment��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��NewsComment��Ϣ*/
    public String DeleteNewsComment() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            newsCommentDAO.DeleteNewsComment(commentId);
            ctx.put("message",  java.net.URLEncoder.encode("NewsCommentɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsCommentɾ��ʧ��!"));
            return "error";
        }
    }

}
