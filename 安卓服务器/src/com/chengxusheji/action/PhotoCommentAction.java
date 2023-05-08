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

    /*�������Ҫ��ѯ������: ����ͼƬ*/
    private PhotoShare photoObj;
    public void setPhotoObj(PhotoShare photoObj) {
        this.photoObj = photoObj;
    }
    public PhotoShare getPhotoObj() {
        return this.photoObj;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String content;
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return this.content;
    }

    /*�������Ҫ��ѯ������: �û�*/
    private UserInfo userInfoObj;
    public void setUserInfoObj(UserInfo userInfoObj) {
        this.userInfoObj = userInfoObj;
    }
    public UserInfo getUserInfoObj() {
        return this.userInfoObj;
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

    private int photoCommentId;
    public void setPhotoCommentId(int photoCommentId) {
        this.photoCommentId = photoCommentId;
    }
    public int getPhotoCommentId() {
        return photoCommentId;
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
    @Resource PhotoShareDAO photoShareDAO;
    @Resource UserInfoDAO userInfoDAO;
    @Resource PhotoCommentDAO photoCommentDAO;

    /*��������PhotoComment����*/
    private PhotoComment photoComment;
    public void setPhotoComment(PhotoComment photoComment) {
        this.photoComment = photoComment;
    }
    public PhotoComment getPhotoComment() {
        return this.photoComment;
    }

    /*��ת�����PhotoComment��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�PhotoShare��Ϣ*/
        List<PhotoShare> photoShareList = photoShareDAO.QueryAllPhotoShareInfo();
        ctx.put("photoShareList", photoShareList);
        /*��ѯ���е�UserInfo��Ϣ*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*���PhotoComment��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddPhotoComment() {
        ActionContext ctx = ActionContext.getContext();
        try {
            PhotoShare photoObj = photoShareDAO.GetPhotoShareBySharePhotoId(photoComment.getPhotoObj().getSharePhotoId());
            photoComment.setPhotoObj(photoObj);
            UserInfo userInfoObj = userInfoDAO.GetUserInfoByUser_name(photoComment.getUserInfoObj().getUser_name());
            photoComment.setUserInfoObj(userInfoObj);
            photoCommentDAO.AddPhotoComment(photoComment);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoComment��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoComment���ʧ��!"));
            return "error";
        }
    }

    /*��ѯPhotoComment��Ϣ*/
    public String QueryPhotoComment() {
        if(currentPage == 0) currentPage = 1;
        if(content == null) content = "";
        List<PhotoComment> photoCommentList = photoCommentDAO.QueryPhotoCommentInfo(photoObj, content, userInfoObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        photoCommentDAO.CalculateTotalPageAndRecordNumber(photoObj, content, userInfoObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = photoCommentDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��̨������excel*/
    public String QueryPhotoCommentOutputToExcel() { 
        if(content == null) content = "";
        List<PhotoComment> photoCommentList = photoCommentDAO.QueryPhotoCommentInfo(photoObj,content,userInfoObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "PhotoComment��Ϣ��¼"; 
        String[] headers = { "����id","����ͼƬ","��������","�û�","����ʱ��"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"PhotoComment.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯPhotoComment��Ϣ*/
    public String FrontQueryPhotoComment() {
        if(currentPage == 0) currentPage = 1;
        if(content == null) content = "";
        List<PhotoComment> photoCommentList = photoCommentDAO.QueryPhotoCommentInfo(photoObj, content, userInfoObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        photoCommentDAO.CalculateTotalPageAndRecordNumber(photoObj, content, userInfoObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = photoCommentDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��ѯҪ�޸ĵ�PhotoComment��Ϣ*/
    public String ModifyPhotoCommentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������photoCommentId��ȡPhotoComment����*/
        PhotoComment photoComment = photoCommentDAO.GetPhotoCommentByPhotoCommentId(photoCommentId);

        List<PhotoShare> photoShareList = photoShareDAO.QueryAllPhotoShareInfo();
        ctx.put("photoShareList", photoShareList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("photoComment",  photoComment);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�PhotoComment��Ϣ*/
    public String FrontShowPhotoCommentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������photoCommentId��ȡPhotoComment����*/
        PhotoComment photoComment = photoCommentDAO.GetPhotoCommentByPhotoCommentId(photoCommentId);

        List<PhotoShare> photoShareList = photoShareDAO.QueryAllPhotoShareInfo();
        ctx.put("photoShareList", photoShareList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("photoComment",  photoComment);
        return "front_show_view";
    }

    /*�����޸�PhotoComment��Ϣ*/
    public String ModifyPhotoComment() {
        ActionContext ctx = ActionContext.getContext();
        try {
            PhotoShare photoObj = photoShareDAO.GetPhotoShareBySharePhotoId(photoComment.getPhotoObj().getSharePhotoId());
            photoComment.setPhotoObj(photoObj);
            UserInfo userInfoObj = userInfoDAO.GetUserInfoByUser_name(photoComment.getUserInfoObj().getUser_name());
            photoComment.setUserInfoObj(userInfoObj);
            photoCommentDAO.UpdatePhotoComment(photoComment);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoComment��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoComment��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��PhotoComment��Ϣ*/
    public String DeletePhotoComment() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            photoCommentDAO.DeletePhotoComment(photoCommentId);
            ctx.put("message",  java.net.URLEncoder.encode("PhotoCommentɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PhotoCommentɾ��ʧ��!"));
            return "error";
        }
    }

}
