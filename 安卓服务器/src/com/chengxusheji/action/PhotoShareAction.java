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
import com.chengxusheji.dao.PhotoShareDAO;
import com.chengxusheji.domain.PhotoShare;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class PhotoShareAction extends BaseAction {

	/*图片或文件字段sharePhoto参数接收*/
	private File sharePhotoFile;
	private String sharePhotoFileFileName;
	private String sharePhotoFileContentType;
	public File getSharePhotoFile() {
		return sharePhotoFile;
	}
	public void setSharePhotoFile(File sharePhotoFile) {
		this.sharePhotoFile = sharePhotoFile;
	}
	public String getSharePhotoFileFileName() {
		return sharePhotoFileFileName;
	}
	public void setSharePhotoFileFileName(String sharePhotoFileFileName) {
		this.sharePhotoFileFileName = sharePhotoFileFileName;
	}
	public String getSharePhotoFileContentType() {
		return sharePhotoFileContentType;
	}
	public void setSharePhotoFileContentType(String sharePhotoFileContentType) {
		this.sharePhotoFileContentType = sharePhotoFileContentType;
	}
    /*界面层需要查询的属性: 上传用户*/
    private UserInfo userInfoObj;
    public void setUserInfoObj(UserInfo userInfoObj) {
        this.userInfoObj = userInfoObj;
    }
    public UserInfo getUserInfoObj() {
        return this.userInfoObj;
    }

    /*界面层需要查询的属性: 分享时间*/
    private String shareTime;
    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }
    public String getShareTime() {
        return this.shareTime;
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

    private int sharePhotoId;
    public void setSharePhotoId(int sharePhotoId) {
        this.sharePhotoId = sharePhotoId;
    }
    public int getSharePhotoId() {
        return sharePhotoId;
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
    @Resource UserInfoDAO userInfoDAO;
    @Resource PhotoShareDAO photoShareDAO;

    /*待操作的PhotoShare对象*/
    private PhotoShare photoShare;
    public void setPhotoShare(PhotoShare photoShare) {
        this.photoShare = photoShare;
    }
    public PhotoShare getPhotoShare() {
        return this.photoShare;
    }

    /*跳转到添加PhotoShare视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的UserInfo信息*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*添加PhotoShare信息*/
    @SuppressWarnings("deprecation")
    public String AddPhotoShare() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userInfoObj = userInfoDAO.GetUserInfoByUser_name(photoShare.getUserInfoObj().getUser_name());
            photoShare.setUserInfoObj(userInfoObj);
            /*处理图片上传*/
            String sharePhotoPath = "upload/noimage.jpg"; 
       	 	if(sharePhotoFile != null)
       	 		sharePhotoPath = photoUpload(sharePhotoFile,sharePhotoFileContentType);
       	 	photoShare.setSharePhoto(sharePhotoPath);
            photoShareDAO.AddPhotoShare(photoShare);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoShare添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoShare添加失败!"));
            return "error";
        }
    }

    /*查询PhotoShare信息*/
    public String QueryPhotoShare() {
        if(currentPage == 0) currentPage = 1;
        if(shareTime == null) shareTime = "";
        List<PhotoShare> photoShareList = photoShareDAO.QueryPhotoShareInfo(userInfoObj, shareTime, currentPage);
        /*计算总的页数和总的记录数*/
        photoShareDAO.CalculateTotalPageAndRecordNumber(userInfoObj, shareTime);
        /*获取到总的页码数目*/
        totalPage = photoShareDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = photoShareDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("photoShareList",  photoShareList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("userInfoObj", userInfoObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("shareTime", shareTime);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryPhotoShareOutputToExcel() { 
        if(shareTime == null) shareTime = "";
        List<PhotoShare> photoShareList = photoShareDAO.QueryPhotoShareInfo(userInfoObj,shareTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "PhotoShare信息记录"; 
        String[] headers = { "分享id","图片标题","图片","上传用户","分享时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<photoShareList.size();i++) {
        	PhotoShare photoShare = photoShareList.get(i); 
        	dataset.add(new String[]{photoShare.getSharePhotoId() + "",photoShare.getPhotoTitle(),photoShare.getSharePhoto(),photoShare.getUserInfoObj().getName(),
photoShare.getShareTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"PhotoShare.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询PhotoShare信息*/
    public String FrontQueryPhotoShare() {
        if(currentPage == 0) currentPage = 1;
        if(shareTime == null) shareTime = "";
        List<PhotoShare> photoShareList = photoShareDAO.QueryPhotoShareInfo(userInfoObj, shareTime, currentPage);
        /*计算总的页数和总的记录数*/
        photoShareDAO.CalculateTotalPageAndRecordNumber(userInfoObj, shareTime);
        /*获取到总的页码数目*/
        totalPage = photoShareDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = photoShareDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("photoShareList",  photoShareList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("userInfoObj", userInfoObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("shareTime", shareTime);
        return "front_query_view";
    }

    /*查询要修改的PhotoShare信息*/
    public String ModifyPhotoShareQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键sharePhotoId获取PhotoShare对象*/
        PhotoShare photoShare = photoShareDAO.GetPhotoShareBySharePhotoId(sharePhotoId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("photoShare",  photoShare);
        return "modify_view";
    }

    /*查询要修改的PhotoShare信息*/
    public String FrontShowPhotoShareQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键sharePhotoId获取PhotoShare对象*/
        PhotoShare photoShare = photoShareDAO.GetPhotoShareBySharePhotoId(sharePhotoId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("photoShare",  photoShare);
        return "front_show_view";
    }

    /*更新修改PhotoShare信息*/
    public String ModifyPhotoShare() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userInfoObj = userInfoDAO.GetUserInfoByUser_name(photoShare.getUserInfoObj().getUser_name());
            photoShare.setUserInfoObj(userInfoObj);
            /*处理图片上传*/
            if(sharePhotoFile != null) {
            	String sharePhotoPath = photoUpload(sharePhotoFile,sharePhotoFileContentType);
            	photoShare.setSharePhoto(sharePhotoPath);
            }
            photoShareDAO.UpdatePhotoShare(photoShare);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoShare信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoShare信息更新失败!"));
            return "error";
       }
   }

    /*删除PhotoShare信息*/
    public String DeletePhotoShare() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            photoShareDAO.DeletePhotoShare(sharePhotoId);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoShare删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoShare删除失败!"));
            return "error";
        }
    }

}
