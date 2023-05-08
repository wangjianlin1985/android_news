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
import com.chengxusheji.dao.VideoShareDAO;
import com.chengxusheji.domain.VideoShare;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class VideoShareAction extends BaseAction {

	/*图片或文件字段videoFile参数接收*/
	private File videoFileFile;
	private String videoFileFileFileName;
	private String videoFileFileContentType;
	public File getVideoFileFile() {
		return videoFileFile;
	}
	public void setVideoFileFile(File videoFileFile) {
		this.videoFileFile = videoFileFile;
	}
	public String getVideoFileFileFileName() {
		return videoFileFileFileName;
	}
	public void setVideoFileFileFileName(String videoFileFileFileName) {
		this.videoFileFileFileName = videoFileFileFileName;
	}
	public String getVideoFileFileContentType() {
		return videoFileFileContentType;
	}
	public void setVideoFileFileContentType(String videoFileFileContentType) {
		this.videoFileFileContentType = videoFileFileContentType;
	}
    /*界面层需要查询的属性: 视频标题*/
    private String videoTitle;
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
    public String getVideoTitle() {
        return this.videoTitle;
    }

    /*界面层需要查询的属性: 用户*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
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

    private int videoShareId;
    public void setVideoShareId(int videoShareId) {
        this.videoShareId = videoShareId;
    }
    public int getVideoShareId() {
        return videoShareId;
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
    @Resource VideoShareDAO videoShareDAO;

    /*待操作的VideoShare对象*/
    private VideoShare videoShare;
    public void setVideoShare(VideoShare videoShare) {
        this.videoShare = videoShare;
    }
    public VideoShare getVideoShare() {
        return this.videoShare;
    }

    /*跳转到添加VideoShare视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的UserInfo信息*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*添加VideoShare信息*/
    @SuppressWarnings("deprecation")
    public String AddVideoShare() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(videoShare.getUserObj().getUser_name());
            videoShare.setUserObj(userObj);
            /*处理视频文件上传*/
            String videoFilePath = "upload/noimage.jpg"; 
       	 	if(videoFileFile != null)
       	 		videoFilePath = photoUpload(videoFileFile,videoFileFileContentType);
       	 	videoShare.setVideoFile(videoFilePath);
            videoShareDAO.AddVideoShare(videoShare);
            ctx.put("message",  java.net.URLEncoder.encode("VideoShare添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("VideoShare添加失败!"));
            return "error";
        }
    }

    /*查询VideoShare信息*/
    public String QueryVideoShare() {
        if(currentPage == 0) currentPage = 1;
        if(videoTitle == null) videoTitle = "";
        if(shareTime == null) shareTime = "";
        List<VideoShare> videoShareList = videoShareDAO.QueryVideoShareInfo(videoTitle, userObj, shareTime, currentPage);
        /*计算总的页数和总的记录数*/
        videoShareDAO.CalculateTotalPageAndRecordNumber(videoTitle, userObj, shareTime);
        /*获取到总的页码数目*/
        totalPage = videoShareDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = videoShareDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("videoShareList",  videoShareList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("videoTitle", videoTitle);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("shareTime", shareTime);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryVideoShareOutputToExcel() { 
        if(videoTitle == null) videoTitle = "";
        if(shareTime == null) shareTime = "";
        List<VideoShare> videoShareList = videoShareDAO.QueryVideoShareInfo(videoTitle,userObj,shareTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "VideoShare信息记录"; 
        String[] headers = { "分享id","视频标题","视频文件","用户","分享时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<videoShareList.size();i++) {
        	VideoShare videoShare = videoShareList.get(i); 
        	dataset.add(new String[]{videoShare.getVideoShareId() + "",videoShare.getVideoTitle(),videoShare.getVideoFile(),videoShare.getUserObj().getName(),
videoShare.getShareTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"VideoShare.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询VideoShare信息*/
    public String FrontQueryVideoShare() {
        if(currentPage == 0) currentPage = 1;
        if(videoTitle == null) videoTitle = "";
        if(shareTime == null) shareTime = "";
        List<VideoShare> videoShareList = videoShareDAO.QueryVideoShareInfo(videoTitle, userObj, shareTime, currentPage);
        /*计算总的页数和总的记录数*/
        videoShareDAO.CalculateTotalPageAndRecordNumber(videoTitle, userObj, shareTime);
        /*获取到总的页码数目*/
        totalPage = videoShareDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = videoShareDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("videoShareList",  videoShareList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("videoTitle", videoTitle);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("shareTime", shareTime);
        return "front_query_view";
    }

    /*查询要修改的VideoShare信息*/
    public String ModifyVideoShareQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键videoShareId获取VideoShare对象*/
        VideoShare videoShare = videoShareDAO.GetVideoShareByVideoShareId(videoShareId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("videoShare",  videoShare);
        return "modify_view";
    }

    /*查询要修改的VideoShare信息*/
    public String FrontShowVideoShareQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键videoShareId获取VideoShare对象*/
        VideoShare videoShare = videoShareDAO.GetVideoShareByVideoShareId(videoShareId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("videoShare",  videoShare);
        return "front_show_view";
    }

    /*更新修改VideoShare信息*/
    public String ModifyVideoShare() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(videoShare.getUserObj().getUser_name());
            videoShare.setUserObj(userObj);
            /*处理视频文件上传*/
            if(videoFileFile != null) {
            	String videoFilePath = photoUpload(videoFileFile,videoFileFileContentType);
            	videoShare.setVideoFile(videoFilePath);
            }
            videoShareDAO.UpdateVideoShare(videoShare);
            ctx.put("message",  java.net.URLEncoder.encode("VideoShare信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("VideoShare信息更新失败!"));
            return "error";
       }
   }

    /*删除VideoShare信息*/
    public String DeleteVideoShare() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            videoShareDAO.DeleteVideoShare(videoShareId);
            ctx.put("message",  java.net.URLEncoder.encode("VideoShare删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("VideoShare删除失败!"));
            return "error";
        }
    }

}
