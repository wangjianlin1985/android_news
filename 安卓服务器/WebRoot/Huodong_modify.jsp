<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.Huodong" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //��ȡ���е�UserInfo��Ϣ
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    Huodong huodong = (Huodong)request.getAttribute("huodong");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>�޸Ļ��Ϣ</TITLE>
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
    var title = document.getElementById("huodong.title").value;
    if(title=="") {
        alert('���������� !');
        return false;
    }
    var content = document.getElementById("huodong.content").value;
    if(content=="") {
        alert('����������!');
        return false;
    }
    var telephone = document.getElementById("huodong.telephone").value;
    if(telephone=="") {
        alert('�����뱨���绰!');
        return false;
    }
    var personList = document.getElementById("huodong.personList").value;
    if(personList=="") {
        alert('�������������!');
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
    <TD align="left" vAlign=top ><s:form action="Huodong/Huodong_ModifyHuodong.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>�id:</td>
    <td width=70%><input id="huodong.huodongId" name="huodong.huodongId" type="text" value="<%=huodong.getHuodongId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>����� :</td>
    <td width=70%><input id="huodong.title" name="huodong.title" type="text" size="80" value='<%=huodong.getTitle() %>'/></td>
  </tr>

  <tr>
    <td width=30%>�����:</td>
    <td width=70%><textarea id="huodong.content" name="huodong.content" rows=5 cols=50><%=huodong.getContent() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>�����绰:</td>
    <td width=70%><input id="huodong.telephone" name="huodong.telephone" type="text" size="20" value='<%=huodong.getTelephone() %>'/></td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
    <td width=70%><textarea id="huodong.personList" name="huodong.personList" rows=5 cols=50><%=huodong.getPersonList() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>������:</td>
    <td width=70%>
      <select name="huodong.userObj.user_name">
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(huodong.getUserObj().getUser_name()))
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
    <td width=70%><input id="huodong.addTime" name="huodong.addTime" type="text" size="20" value='<%=huodong.getAddTime() %>'/></td>
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
