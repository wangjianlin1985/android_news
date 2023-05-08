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
import com.chengxusheji.dao.PhotoCommentDAO;
import com.chengxusheji.domain.PhotoComment;
import com.chengxusheji.dao.PhotoShareDAO;
import com.chengxusheji.domain.PhotoShare;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class PhotoCommentAction extends BaseAction {

    /*界面层需要查询的属性: 被评图片*/
    private PhotoShare photoObj;
    public void setPhotoObj(PhotoShare photoObj) {
        this.photoObj = photoObj;
    }
    public PhotoShare getPhotoObj() {
        return this.photoObj;
    }

    /*界面层需要查询的属性: 评论内容*/
    private String content;
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return this.content;
    }

    /*界面层需要查询的属性: 用户*/
    private UserInfo userInfoObj;
    public void setUserInfoObj(UserInfo userInfoObj) {
        this.userInfoObj = userInfoObj;
    }
    public UserInfo getUserInfoObj() {
        return this.userInfoObj;
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

    private int photoCommentId;
    public void setPhotoCommentId(int photoCommentId) {
        this.photoCommentId = photoCommentId;
    }
    public int getPhotoCommentId() {
        return photoCommentId;
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
    @Resource PhotoShareDAO photoShareDAO;
    @Resource UserInfoDAO userInfoDAO;
    @Resource PhotoCommentDAO photoCommentDAO;

    /*待操作的PhotoComment对象*/
    private PhotoComment photoComment;
    public void setPhotoComment(PhotoComment photoComment) {
        this.photoComment = photoComment;
    }
    public PhotoComment getPhotoComment() {
        return this.photoComment;
    }

    /*跳转到添加PhotoComment视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的PhotoShare信息*/
        List<PhotoShare> photoShareList = photoShareDAO.QueryAllPhotoShareInfo();
        ctx.put("photoShareList", photoShareList);
        /*查询所有的UserInfo信息*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*添加PhotoComment信息*/
    @SuppressWarnings("deprecation")
    public String AddPhotoComment() {
        ActionContext ctx = ActionContext.getContext();
        try {
            PhotoShare photoObj = photoShareDAO.GetPhotoShareBySharePhotoId(photoComment.getPhotoObj().getSharePhotoId());
            photoComment.setPhotoObj(photoObj);
            UserInfo userInfoObj = userInfoDAO.GetUserInfoByUser_name(photoComment.getUserInfoObj().getUser_name());
            photoComment.setUserInfoObj(userInfoObj);
            photoCommentDAO.AddPhotoComment(photoComment);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoComment添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoComment添加失败!"));
            return "error";
        }
    }

    /*查询PhotoComment信息*/
    public String QueryPhotoComment() {
        if(currentPage == 0) currentPage = 1;
        if(content == null) content = "";
        List<PhotoComment> photoCommentList = photoCommentDAO.QueryPhotoCommentInfo(photoObj, content, userInfoObj, currentPage);
        /*计算总的页数和总的记录数*/
        photoCommentDAO.CalculateTotalPageAndRecordNumber(photoObj, content, userInfoObj);
        /*获取到总的页码数目*/
        totalPage = photoCommentDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = photoCommentDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("photoCommentList",  photoCommentList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("photoObj", photoObj);
        List<PhotoShare> photoShareList = photoShareDAO.QueryAllPhotoShareInfo();
        ctx.put("photoShareList", photoShareList);
        ctx.put("content", content);
        ctx.put("userInfoObj", userInfoObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryPhotoCommentOutputToExcel() { 
        if(content == null) content = "";
        List<PhotoComment> photoCommentList = photoCommentDAO.QueryPhotoCommentInfo(photoObj,content,userInfoObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "PhotoComment信息记录"; 
        String[] headers = { "评论id","被评图片","评论内容","用户","评论时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<photoCommentList.size();i++) {
        	PhotoComment photoComment = photoCommentList.get(i); 
        	dataset.add(new String[]{photoComment.getPhotoCommentId() + "",photoComment.getPhotoObj().getPhotoTitle(),
photoComment.getContent(),photoComment.getUserInfoObj().getName(),
photoComment.getCommentTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"PhotoComment.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询PhotoComment信息*/
    public String FrontQueryPhotoComment() {
        if(currentPage == 0) currentPage = 1;
        if(content == null) content = "";
        List<PhotoComment> photoCommentList = photoCommentDAO.QueryPhotoCommentInfo(photoObj, content, userInfoObj, currentPage);
        /*计算总的页数和总的记录数*/
        photoCommentDAO.CalculateTotalPageAndRecordNumber(photoObj, content, userInfoObj);
        /*获取到总的页码数目*/
        totalPage = photoCommentDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = photoCommentDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("photoCommentList",  photoCommentList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("photoObj", photoObj);
        List<PhotoShare> photoShareList = photoShareDAO.QueryAllPhotoShareInfo();
        ctx.put("photoShareList", photoShareList);
        ctx.put("content", content);
        ctx.put("userInfoObj", userInfoObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "front_query_view";
    }

    /*查询要修改的PhotoComment信息*/
    public String ModifyPhotoCommentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键photoCommentId获取PhotoComment对象*/
        PhotoComment photoComment = photoCommentDAO.GetPhotoCommentByPhotoCommentId(photoCommentId);

        List<PhotoShare> photoShareList = photoShareDAO.QueryAllPhotoShareInfo();
        ctx.put("photoShareList", photoShareList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("photoComment",  photoComment);
        return "modify_view";
    }

    /*查询要修改的PhotoComment信息*/
    public String FrontShowPhotoCommentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键photoCommentId获取PhotoComment对象*/
        PhotoComment photoComment = photoCommentDAO.GetPhotoCommentByPhotoCommentId(photoCommentId);

        List<PhotoShare> photoShareList = photoShareDAO.QueryAllPhotoShareInfo();
        ctx.put("photoShareList", photoShareList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("photoComment",  photoComment);
        return "front_show_view";
    }

    /*更新修改PhotoComment信息*/
    public String ModifyPhotoComment() {
        ActionContext ctx = ActionContext.getContext();
        try {
            PhotoShare photoObj = photoShareDAO.GetPhotoShareBySharePhotoId(photoComment.getPhotoObj().getSharePhotoId());
            photoComment.setPhotoObj(photoObj);
            UserInfo userInfoObj = userInfoDAO.GetUserInfoByUser_name(photoComment.getUserInfoObj().getUser_name());
            photoComment.setUserInfoObj(userInfoObj);
            photoCommentDAO.UpdatePhotoComment(photoComment);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoComment信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoComment信息更新失败!"));
            return "error";
       }
   }

    /*删除PhotoComment信息*/
    public String DeletePhotoComment() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            photoCommentDAO.DeletePhotoComment(photoCommentId);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoComment删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoComment删除失败!"));
            return "error";
        }
    }

}
