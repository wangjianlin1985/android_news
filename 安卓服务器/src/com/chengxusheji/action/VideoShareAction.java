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

	/*ͼƬ���ļ��ֶ�videoFile��������*/
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
    /*�������Ҫ��ѯ������: ��Ƶ����*/
    private String videoTitle;
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
    public String getVideoTitle() {
        return this.videoTitle;
    }

    /*�������Ҫ��ѯ������: �û�*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*�������Ҫ��ѯ������: ����ʱ��*/
    private String shareTime;
    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }
    public String getShareTime() {
        return this.shareTime;
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

    private int videoShareId;
    public void setVideoShareId(int videoShareId) {
        this.videoShareId = videoShareId;
    }
    public int getVideoShareId() {
        return videoShareId;
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
    @Resource UserInfoDAO userInfoDAO;
    @Resource VideoShareDAO videoShareDAO;

    /*��������VideoShare����*/
    private VideoShare videoShare;
    public void setVideoShare(VideoShare videoShare) {
        this.videoShare = videoShare;
    }
    public VideoShare getVideoShare() {
        return this.videoShare;
    }

    /*��ת�����VideoShare��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�UserInfo��Ϣ*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*���VideoShare��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddVideoShare() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(videoShare.getUserObj().getUser_name());
            videoShare.setUserObj(userObj);
            /*������Ƶ�ļ��ϴ�*/
            String videoFilePath = "upload/noimage.jpg"; 
       	 	if(videoFileFile != null)
       	 		videoFilePath = photoUpload(videoFileFile,videoFileFileContentType);
       	 	videoShare.setVideoFile(videoFilePath);
            videoShareDAO.AddVideoShare(videoShare);
            ctx.put("message",  java.net.URLEncoder.encode("VideoShare��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("VideoShare���ʧ��!"));
            return "error";
        }
    }

    /*��ѯVideoShare��Ϣ*/
    public String QueryVideoShare() {
        if(currentPage == 0) currentPage = 1;
        if(videoTitle == null) videoTitle = "";
        if(shareTime == null) shareTime = "";
        List<VideoShare> videoShareList = videoShareDAO.QueryVideoShareInfo(videoTitle, userObj, shareTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        videoShareDAO.CalculateTotalPageAndRecordNumber(videoTitle, userObj, shareTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = videoShareDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��̨������excel*/
    public String QueryVideoShareOutputToExcel() { 
        if(videoTitle == null) videoTitle = "";
        if(shareTime == null) shareTime = "";
        List<VideoShare> videoShareList = videoShareDAO.QueryVideoShareInfo(videoTitle,userObj,shareTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "VideoShare��Ϣ��¼"; 
        String[] headers = { "����id","��Ƶ����","��Ƶ�ļ�","�û�","����ʱ��"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"VideoShare.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯVideoShare��Ϣ*/
    public String FrontQueryVideoShare() {
        if(currentPage == 0) currentPage = 1;
        if(videoTitle == null) videoTitle = "";
        if(shareTime == null) shareTime = "";
        List<VideoShare> videoShareList = videoShareDAO.QueryVideoShareInfo(videoTitle, userObj, shareTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        videoShareDAO.CalculateTotalPageAndRecordNumber(videoTitle, userObj, shareTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = videoShareDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��ѯҪ�޸ĵ�VideoShare��Ϣ*/
    public String ModifyVideoShareQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������videoShareId��ȡVideoShare����*/
        VideoShare videoShare = videoShareDAO.GetVideoShareByVideoShareId(videoShareId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("videoShare",  videoShare);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�VideoShare��Ϣ*/
    public String FrontShowVideoShareQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������videoShareId��ȡVideoShare����*/
        VideoShare videoShare = videoShareDAO.GetVideoShareByVideoShareId(videoShareId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("videoShare",  videoShare);
        return "front_show_view";
    }

    /*�����޸�VideoShare��Ϣ*/
    public String ModifyVideoShare() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(videoShare.getUserObj().getUser_name());
            videoShare.setUserObj(userObj);
            /*������Ƶ�ļ��ϴ�*/
            if(videoFileFile != null) {
            	String videoFilePath = photoUpload(videoFileFile,videoFileFileContentType);
            	videoShare.setVideoFile(videoFilePath);
            }
            videoShareDAO.UpdateVideoShare(videoShare);
            ctx.put("message",  java.net.URLEncoder.encode("VideoShare��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("VideoShare��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��VideoShare��Ϣ*/
    public String DeleteVideoShare() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            videoShareDAO.DeleteVideoShare(videoShareId);
            ctx.put("message",  java.net.URLEncoder.encode("VideoShareɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("VideoShareɾ��ʧ��!"));
            return "error";
        }
    }

}
