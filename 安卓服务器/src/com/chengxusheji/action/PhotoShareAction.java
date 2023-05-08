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

	/*ͼƬ���ļ��ֶ�sharePhoto��������*/
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
    /*�������Ҫ��ѯ������: �ϴ��û�*/
    private UserInfo userInfoObj;
    public void setUserInfoObj(UserInfo userInfoObj) {
        this.userInfoObj = userInfoObj;
    }
    public UserInfo getUserInfoObj() {
        return this.userInfoObj;
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

    private int sharePhotoId;
    public void setSharePhotoId(int sharePhotoId) {
        this.sharePhotoId = sharePhotoId;
    }
    public int getSharePhotoId() {
        return sharePhotoId;
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
    @Resource PhotoShareDAO photoShareDAO;

    /*��������PhotoShare����*/
    private PhotoShare photoShare;
    public void setPhotoShare(PhotoShare photoShare) {
        this.photoShare = photoShare;
    }
    public PhotoShare getPhotoShare() {
        return this.photoShare;
    }

    /*��ת�����PhotoShare��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�UserInfo��Ϣ*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*���PhotoShare��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddPhotoShare() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userInfoObj = userInfoDAO.GetUserInfoByUser_name(photoShare.getUserInfoObj().getUser_name());
            photoShare.setUserInfoObj(userInfoObj);
            /*����ͼƬ�ϴ�*/
            String sharePhotoPath = "upload/noimage.jpg"; 
       	 	if(sharePhotoFile != null)
       	 		sharePhotoPath = photoUpload(sharePhotoFile,sharePhotoFileContentType);
       	 	photoShare.setSharePhoto(sharePhotoPath);
            photoShareDAO.AddPhotoShare(photoShare);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoShare��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoShare���ʧ��!"));
            return "error";
        }
    }

    /*��ѯPhotoShare��Ϣ*/
    public String QueryPhotoShare() {
        if(currentPage == 0) currentPage = 1;
        if(shareTime == null) shareTime = "";
        List<PhotoShare> photoShareList = photoShareDAO.QueryPhotoShareInfo(userInfoObj, shareTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        photoShareDAO.CalculateTotalPageAndRecordNumber(userInfoObj, shareTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = photoShareDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��̨������excel*/
    public String QueryPhotoShareOutputToExcel() { 
        if(shareTime == null) shareTime = "";
        List<PhotoShare> photoShareList = photoShareDAO.QueryPhotoShareInfo(userInfoObj,shareTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "PhotoShare��Ϣ��¼"; 
        String[] headers = { "����id","ͼƬ����","ͼƬ","�ϴ��û�","����ʱ��"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"PhotoShare.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯPhotoShare��Ϣ*/
    public String FrontQueryPhotoShare() {
        if(currentPage == 0) currentPage = 1;
        if(shareTime == null) shareTime = "";
        List<PhotoShare> photoShareList = photoShareDAO.QueryPhotoShareInfo(userInfoObj, shareTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        photoShareDAO.CalculateTotalPageAndRecordNumber(userInfoObj, shareTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = photoShareDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��ѯҪ�޸ĵ�PhotoShare��Ϣ*/
    public String ModifyPhotoShareQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������sharePhotoId��ȡPhotoShare����*/
        PhotoShare photoShare = photoShareDAO.GetPhotoShareBySharePhotoId(sharePhotoId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("photoShare",  photoShare);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�PhotoShare��Ϣ*/
    public String FrontShowPhotoShareQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������sharePhotoId��ȡPhotoShare����*/
        PhotoShare photoShare = photoShareDAO.GetPhotoShareBySharePhotoId(sharePhotoId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("photoShare",  photoShare);
        return "front_show_view";
    }

    /*�����޸�PhotoShare��Ϣ*/
    public String ModifyPhotoShare() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userInfoObj = userInfoDAO.GetUserInfoByUser_name(photoShare.getUserInfoObj().getUser_name());
            photoShare.setUserInfoObj(userInfoObj);
            /*����ͼƬ�ϴ�*/
            if(sharePhotoFile != null) {
            	String sharePhotoPath = photoUpload(sharePhotoFile,sharePhotoFileContentType);
            	photoShare.setSharePhoto(sharePhotoPath);
            }
            photoShareDAO.UpdatePhotoShare(photoShare);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoShare��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoShare��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��PhotoShare��Ϣ*/
    public String DeletePhotoShare() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            photoShareDAO.DeletePhotoShare(sharePhotoId);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoShareɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoShareɾ��ʧ��!"));
            return "error";
        }
    }

}
