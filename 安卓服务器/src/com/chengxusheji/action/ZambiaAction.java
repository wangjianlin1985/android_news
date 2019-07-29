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

    /*界面层需要查询的属性: 被赞新闻*/
    private News newsObj;
    public void setNewsObj(News newsObj) {
        this.newsObj = newsObj;
    }
    public News getNewsObj() {
        return this.newsObj;
    }

    /*界面层需要查询的属性: 用户*/
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

    private int zambiaId;
    public void setZambiaId(int zambiaId) {
        this.zambiaId = zambiaId;
    }
    public int getZambiaId() {
        return zambiaId;
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
    @Resource ZambiaDAO zambiaDAO;

    /*待操作的Zambia对象*/
    private Zambia zambia;
    public void setZambia(Zambia zambia) {
        this.zambia = zambia;
    }
    public Zambia getZambia() {
        return this.zambia;
    }

    /*跳转到添加Zambia视图*/
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

    /*添加Zambia信息*/
    @SuppressWarnings("deprecation")
    public String AddZambia() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(zambia.getNewsObj().getNewsId());
            zambia.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(zambia.getUserObj().getUser_name());
            zambia.setUserObj(userObj);
            zambiaDAO.AddZambia(zambia);
            ctx.put("message",  java.net.URLEncoder.encode("Zambia添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Zambia添加失败!"));
            return "error";
        }
    }

    /*查询Zambia信息*/
    public String QueryZambia() {
        if(currentPage == 0) currentPage = 1;
        List<Zambia> zambiaList = zambiaDAO.QueryZambiaInfo(newsObj, userObj, currentPage);
        /*计算总的页数和总的记录数*/
        zambiaDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*获取到总的页码数目*/
        totalPage = zambiaDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryZambiaOutputToExcel() { 
        List<Zambia> zambiaList = zambiaDAO.QueryZambiaInfo(newsObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Zambia信息记录"; 
        String[] headers = { "赞id","被赞新闻","用户","被赞时间"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Zambia.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询Zambia信息*/
    public String FrontQueryZambia() {
        if(currentPage == 0) currentPage = 1;
        List<Zambia> zambiaList = zambiaDAO.QueryZambiaInfo(newsObj, userObj, currentPage);
        /*计算总的页数和总的记录数*/
        zambiaDAO.CalculateTotalPageAndRecordNumber(newsObj, userObj);
        /*获取到总的页码数目*/
        totalPage = zambiaDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的Zambia信息*/
    public String ModifyZambiaQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键zambiaId获取Zambia对象*/
        Zambia zambia = zambiaDAO.GetZambiaByZambiaId(zambiaId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("zambia",  zambia);
        return "modify_view";
    }

    /*查询要修改的Zambia信息*/
    public String FrontShowZambiaQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键zambiaId获取Zambia对象*/
        Zambia zambia = zambiaDAO.GetZambiaByZambiaId(zambiaId);

        List<News> newsList = newsDAO.QueryAllNewsInfo();
        ctx.put("newsList", newsList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("zambia",  zambia);
        return "front_show_view";
    }

    /*更新修改Zambia信息*/
    public String ModifyZambia() {
        ActionContext ctx = ActionContext.getContext();
        try {
            News newsObj = newsDAO.GetNewsByNewsId(zambia.getNewsObj().getNewsId());
            zambia.setNewsObj(newsObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(zambia.getUserObj().getUser_name());
            zambia.setUserObj(userObj);
            zambiaDAO.UpdateZambia(zambia);
            ctx.put("message",  java.net.URLEncoder.encode("Zambia信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Zambia信息更新失败!"));
            return "error";
       }
   }

    /*删除Zambia信息*/
    public String DeleteZambia() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            zambiaDAO.DeleteZambia(zambiaId);
            ctx.put("message",  java.net.URLEncoder.encode("Zambia删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Zambia删除失败!"));
            return "error";
        }
    }

}
