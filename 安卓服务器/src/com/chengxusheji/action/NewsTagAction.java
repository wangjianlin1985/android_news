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

    /*界面层需要查询的属性: 被标记新闻*/
    private News newsObj;
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }
    public News getNewsObj() {
        return this.newsObj;
    }

    /*界面层需要查询的属性: 标记的用户*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*当前第几页*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*一共多少页*/
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

    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource NewsDAO newsDAO;
    @Resource UserInfoDAO userInfoDAO;
    @Resource NewsTagDAO newsTagDAO;

    /*待操作的NewsTag对象*/
    private NewsTag newsTag;
    public void setNewsTag(NewsTag newsTag) {
        this.newsTag = newsTag;
    }
    public NewsTag getNewsTag() {
        return this.newsTag;
    }

    /*跳转到添加NewsTag视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的News信息*/
        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        /*查询所有的UserInfo信息*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*添加NewsTag信息*/
    @SuppressWarnings("deprecation")
    public String AddNewsTag() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(newsTag.getNewsObj().getNewsId());
            newsTag.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(newsTag.getUserObj().getUser_name());
            newsTag.setUserObj(userObj);
            newsTagDAO.AddNewsTag(newsTag);
            ctx.put("message",  java.net.URLEncoder.encode("NewsTag添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsTag添加失败!"));
            return "error";
        }
    }

    /*查询NewsTag信息*/
    public String QueryNewsTag() {
        if(currentPage == 0) currentPage = 1;
        List<NewsTag> newsTagList = newsTagDAO.QueryNewsTagInfo(newsObj, userObj, currentPage);
        /*计算总的页数和总的记录数*/
        newsTagDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*获取到总的页码数目*/
        totalPage = newsTagDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryNewsTagOutputToExcel() { 
        List<NewsTag> newsTagList = newsTagDAO.QueryNewsTagInfo(newsObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "NewsTag信息记录"; 
        String[] headers = { "标记id","被标记新闻","标记的用户","新闻状态","标记时间"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"NewsTag.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
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
    /*前台查询NewsTag信息*/
    public String FrontQueryNewsTag() {
        if(currentPage == 0) currentPage = 1;
        List<NewsTag> newsTagList = newsTagDAO.QueryNewsTagInfo(newsObj, userObj, currentPage);
        /*计算总的页数和总的记录数*/
        newsTagDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*获取到总的页码数目*/
        totalPage = newsTagDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的NewsTag信息*/
    public String ModifyNewsTagQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键tagId获取NewsTag对象*/
        NewsTag newsTag = newsTagDAO.GetNewsTagByTagId(tagId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("newsTag",  newsTag);
        return "modify_view";
    }

    /*查询要修改的NewsTag信息*/
    public String FrontShowNewsTagQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键tagId获取NewsTag对象*/
        NewsTag newsTag = newsTagDAO.GetNewsTagByTagId(tagId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("newsTag",  newsTag);
        return "front_show_view";
    }

    /*更新修改NewsTag信息*/
    public String ModifyNewsTag() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(newsTag.getNewsObj().getNewsId());
            newsTag.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(newsTag.getUserObj().getUser_name());
            newsTag.setUserObj(userObj);
            newsTagDAO.UpdateNewsTag(newsTag);
            ctx.put("message",  java.net.URLEncoder.encode("NewsTag信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsTag信息更新失败!"));
            return "error";
       }
   }

    /*删除NewsTag信息*/
    public String DeleteNewsTag() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            newsTagDAO.DeleteNewsTag(tagId);
            ctx.put("message",  java.net.URLEncoder.encode("NewsTag删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsTag删除失败!"));
            return "error";
        }
    }

}
