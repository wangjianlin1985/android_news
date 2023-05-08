<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.PhotoComment" %>
<%@ page import="com.chengxusheji.domain.PhotoShare" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //��ȡ���е�PhotoShare��Ϣ
    List<PhotoShare> photoShareList = (List<PhotoShare>)request.getAttribute("photoShareList");
    //��ȡ���е�UserInfo��Ϣ
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    PhotoComment photoComment = (PhotoComment)request.getAttribute("photoComment");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>�޸�ͼƬ����</TITLE>
<STYLE type=text/css>
BODY {
	MARGIN-LEFT: 0px; BACKGROUND-COLOR: #ffffff
}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
<script language="javascript">
/*��֤��*/
function checkForm() {
    var content = document.getElementById("photoComment.content").value;
    if(content=="") {
        alert('��������������!');
        return false;
    }
    return true; 
}
 </script>
</HEAD>
<BODY background="<%=basePath %>images/adminBg.jpg">
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="PhotoComment/PhotoComment_ModifyPhotoComment.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>����id:</td>
    <td width=70%><input id="photoComment.photoCommentId" name="photoComment.photoCommentId" type="text" value="<%=photoComment.getPhotoCommentId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>����ͼƬ:</td>
    <td width=70%>
      <select name="photoComment.photoObj.sharePhotoId">
      <%
        for(PhotoShare photoShare:photoShareList) {
          String selected = "";
          if(photoShare.getSharePhotoId() == photoComment.getPhotoObj().getSharePhotoId())
            selected = "selected";
      %>
          <option value='<%=photoShare.getSharePhotoId() %>' <%=selected %>><%=photoShare.getPhotoTitle() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
    <td width=70%><textarea id="photoComment.content" name="photoComment.content" rows=5 cols=50><%=photoComment.getContent() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>�û�:</td>
    <td width=70%>
      <select name="photoComment.userInfoObj.user_name">
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(photoComment.getUserInfoObj().getUser_name()))
            selected = "selected";
      %>
          <option value='<%=userInfo.getUser_name() %>' <%=selected %>><%=userInfo.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>����ʱ��:</td>
    <td width=70%><input id="photoComment.commentTime" name="photoComment.commentTime" type="text" size="20" value='<%=photoComment.getCommentTime() %>'/></td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='����' >
        &nbsp;&nbsp;
        <input type="reset" value='��д' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
