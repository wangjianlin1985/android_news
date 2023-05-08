<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.Huodong" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的UserInfo信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    Huodong huodong = (Huodong)request.getAttribute("huodong");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改活动信息</TITLE>
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
/*验证表单*/
function checkForm() {
    var title = document.getElementById("huodong.title").value;
    if(title=="") {
        alert('请输入活动主题 !');
        return false;
    }
    var content = document.getElementById("huodong.content").value;
    if(content=="") {
        alert('请输入活动内容!');
        return false;
    }
    var telephone = document.getElementById("huodong.telephone").value;
    if(telephone=="") {
        alert('请输入报名电话!');
        return false;
    }
    var personList = document.getElementById("huodong.personList").value;
    if(personList=="") {
        alert('请输入参与名单!');
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
    <td width=30%>活动id:</td>
    <td width=70%><input id="huodong.huodongId" name="huodong.huodongId" type="text" value="<%=huodong.getHuodongId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>活动主题 :</td>
    <td width=70%><input id="huodong.title" name="huodong.title" type="text" size="80" value='<%=huodong.getTitle() %>'/></td>
  </tr>

  <tr>
    <td width=30%>活动内容:</td>
    <td width=70%><textarea id="huodong.content" name="huodong.content" rows=5 cols=50><%=huodong.getContent() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>报名电话:</td>
    <td width=70%><input id="huodong.telephone" name="huodong.telephone" type="text" size="20" value='<%=huodong.getTelephone() %>'/></td>
  </tr>

  <tr>
    <td width=30%>参与名单:</td>
    <td width=70%><textarea id="huodong.personList" name="huodong.personList" rows=5 cols=50><%=huodong.getPersonList() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>发起人:</td>
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
    <td width=30%>发布时间:</td>
    <td width=70%><input id="huodong.addTime" name="huodong.addTime" type="text" size="20" value='<%=huodong.getAddTime() %>'/></td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='保存' >
        &nbsp;&nbsp;
        <input type="reset" value='重写' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
