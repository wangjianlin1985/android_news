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

    /*界面层需要查询的属性: 被收藏新闻*/
    private News newsObj;
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }
    public News getNewsObj() {
        return this.newsObj;
    }

    /*界面层需要查询的属性: 收藏人*/
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

    private int collectionId;
    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }
    public int getCollectionId() {
        return collectionId;
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
    @Resource NewsCollectionDAO newsCollectionDAO;

    /*待操作的NewsCollection对象*/
    private NewsCollection newsCollection;
    public void setNewsCollection(NewsCollection newsCollection) {
        this.newsCollection = newsCollection;
    }
    public NewsCollection getNewsCollection() {
        return this.newsCollection;
    }

    /*跳转到添加NewsCollection视图*/
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

    /*添加NewsCollection信息*/
    @SuppressWarnings("deprecation")
    public String AddNewsCollection() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(newsCollection.getNewsObj().getNewsId());
            newsCollection.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(newsCollection.getUserObj().getUser_name());
            newsCollection.setUserObj(userObj);
            newsCollectionDAO.AddNewsCollection(newsCollection);
            ctx.put("message",  java.net.URLEncoder.encode("NewsCollection添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsCollection添加失败!"));
            return "error";
        }
    }

    /*查询NewsCollection信息*/
    public String QueryNewsCollection() {
        if(currentPage == 0) currentPage = 1;
        List<NewsCollection> newsCollectionList = newsCollectionDAO.QueryNewsCollectionInfo(newsObj, userObj, currentPage);
        /*计算总的页数和总的记录数*/
        newsCollectionDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*获取到总的页码数目*/
        totalPage = newsCollectionDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryNewsCollectionOutputToExcel() { 
        List<NewsCollection> newsCollectionList = newsCollectionDAO.QueryNewsCollectionInfo(newsObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "NewsCollection信息记录"; 
        String[] headers = { "收藏id","被收藏新闻","收藏人","收藏时间"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"NewsCollection.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询NewsCollection信息*/
    public String FrontQueryNewsCollection() {
        if(currentPage == 0) currentPage = 1;
        List<NewsCollection> newsCollectionList = newsCollectionDAO.QueryNewsCollectionInfo(newsObj, userObj, currentPage);
        /*计算总的页数和总的记录数*/
        newsCollectionDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*获取到总的页码数目*/
        totalPage = newsCollectionDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的NewsCollection信息*/
    public String ModifyNewsCollectionQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键collectionId获取NewsCollection对象*/
        NewsCollection newsCollection = newsCollectionDAO.GetNewsCollectionByCollectionId(collectionId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("newsCollection",  newsCollection);
        return "modify_view";
    }

    /*查询要修改的NewsCollection信息*/
    public String FrontShowNewsCollectionQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键collectionId获取NewsCollection对象*/
        NewsCollection newsCollection = newsCollectionDAO.GetNewsCollectionByCollectionId(collectionId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("newsCollection",  newsCollection);
        return "front_show_view";
    }

    /*更新修改NewsCollection信息*/
    public String ModifyNewsCollection() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(newsCollection.getNewsObj().getNewsId());
            newsCollection.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(newsCollection.getUserObj().getUser_name());
            newsCollection.setUserObj(userObj);
            newsCollectionDAO.UpdateNewsCollection(newsCollection);
            ctx.put("message",  java.net.URLEncoder.encode("NewsCollection信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsCollection信息更新失败!"));
            return "error";
       }
   }

    /*删除NewsCollection信息*/
    public String DeleteNewsCollection() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            newsCollectionDAO.DeleteNewsCollection(collectionId);
            ctx.put("message",  java.net.URLEncoder.encode("NewsCollection删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("NewsCollection删除失败!"));
            return "error";
        }
    }

}
