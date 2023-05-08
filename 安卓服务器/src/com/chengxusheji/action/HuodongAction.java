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
import com.chengxusheji.dao.HuodongDAO;
import com.chengxusheji.domain.Huodong;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class HuodongAction extends BaseAction {

    /*�������Ҫ��ѯ������: ����� */
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*�������Ҫ��ѯ������: �����绰*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
    }

    /*�������Ҫ��ѯ������: ������*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*�������Ҫ��ѯ������: ����ʱ��*/
    private String addTime;
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
    public String getAddTime() {
        return this.addTime;
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

    private int huodongId;
    public void setHuodongId(int huodongId) {
        this.huodongId = huodongId;
    }
    public int getHuodongId() {
        return huodongId;
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
    @Resource HuodongDAO huodongDAO;

    /*��������Huodong����*/
    private Huodong huodong;
    public void setHuodong(Huodong huodong) {
        this.huodong = huodong;
    }
    public Huodong getHuodong() {
        return this.huodong;
    }

    /*��ת�����Huodong��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�UserInfo��Ϣ*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*���Huodong��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddHuodong() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(huodong.getUserObj().getUser_name());
            huodong.setUserObj(userObj);
            huodongDAO.AddHuodong(huodong);
            ctx.put("message",  java.net.URLEncoder.encode("Huodong��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Huodong���ʧ��!"));
            return "error";
        }
    }

    /*��ѯHuodong��Ϣ*/
    public String QueryHuodong() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        if(telephone == null) telephone = "";
        if(addTime == null) addTime = "";
        List<Huodong> huodongList = huodongDAO.QueryHuodongInfo(title, telephone, userObj, addTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        huodongDAO.CalculateTotalPageAndRecordNumber(title, telephone, userObj, addTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = huodongDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = huodongDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("huodongList",  huodongList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("title", title);
        ctx.put("telephone", telephone);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("addTime", addTime);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryHuodongOutputToExcel() { 
        if(title == null) title = "";
        if(telephone == null) telephone = "";
        if(addTime == null) addTime = "";
        List<Huodong> huodongList = huodongDAO.QueryHuodongInfo(title,telephone,userObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Huodong��Ϣ��¼"; 
        String[] headers = { "�id","����� ","�����绰","��������","������","����ʱ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<huodongList.size();i++) {
        	Huodong huodong = huodongList.get(i); 
        	dataset.add(new String[]{huodong.getHuodongId() + "",huodong.getTitle(),huodong.getTelephone(),huodong.getPersonList(),huodong.getUserObj().getName(),
huodong.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Huodong.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯHuodong��Ϣ*/
    public String FrontQueryHuodong() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        if(telephone == null) telephone = "";
        if(addTime == null) addTime = "";
        List<Huodong> huodongList = huodongDAO.QueryHuodongInfo(title, telephone, userObj, addTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        huodongDAO.CalculateTotalPageAndRecordNumber(title, telephone, userObj, addTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = huodongDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = huodongDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("huodongList",  huodongList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("title", title);
        ctx.put("telephone", telephone);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("addTime", addTime);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Huodong��Ϣ*/
    public String ModifyHuodongQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������huodongId��ȡHuodong����*/
        Huodong huodong = huodongDAO.GetHuodongByHuodongId(huodongId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("huodong",  huodong);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Huodong��Ϣ*/
    public String FrontShowHuodongQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������huodongId��ȡHuodong����*/
        Huodong huodong = huodongDAO.GetHuodongByHuodongId(huodongId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("huodong",  huodong);
        return "front_show_view";
    }

    /*�����޸�Huodong��Ϣ*/
    public String ModifyHuodong() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(huodong.getUserObj().getUser_name());
            huodong.setUserObj(userObj);
            huodongDAO.UpdateHuodong(huodong);
            ctx.put("message",  java.net.URLEncoder.encode("Huodong��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Huodong��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Huodong��Ϣ*/
    public String DeleteHuodong() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            huodongDAO.DeleteHuodong(huodongId);
            ctx.put("message",  java.net.URLEncoder.encode("Huodongɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Huodongɾ��ʧ��!"));
            return "error";
        }
    }

}
