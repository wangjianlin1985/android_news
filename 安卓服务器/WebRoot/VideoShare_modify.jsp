<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.VideoShare" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的UserInfo信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    VideoShare videoShare = (VideoShare)request.getAttribute("videoShare");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改视频分享</TITLE>
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
    var videoTitle = document.getElementById("videoShare.videoTitle").value;
    if(videoTitle=="") {
        alert('请输入视频标题!');
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
    <TD align="left" vAlign=top ><s:form action="VideoShare/VideoShare_ModifyVideoShare.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>分享id:</td>
    <td width=70%><input id="videoShare.videoShareId" name="videoShare.videoShareId" type="text" value="<%=videoShare.getVideoShareId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>视频标题:</td>
    <td width=70%><input id="videoShare.videoTitle" name="videoShare.videoTitle" type="text" size="80" value='<%=videoShare.getVideoTitle() %>'/></td>
  </tr>

  <tr>
    <td width=30%>视频文件:</td>
    <td width=70%><img src="<%=basePath %><%=videoShare.getVideoFile() %>" width="200px" border="0px"/><br/>
    <input type=hidden name="videoShare.videoFile" value="<%=videoShare.getVideoFile() %>" />
    <input id="videoFileFile" name="videoFileFile" type="file" size="50" /></td>
  </tr>
  <tr>
    <td width=30%>用户:</td>
    <td width=70%>
      <select name="videoShare.userObj.user_name">
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(videoShare.getUserObj().getUser_name()))
            selected = "selected";
      %>
          <option value='<%=userInfo.getUser_name() %>' <%=selected %>><%=userInfo.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>分享时间:</td>
    <td width=70%><input id="videoShare.shareTime" name="videoShare.shareTime" type="text" size="20" value='<%=videoShare.getShareTime() %>'/></td>
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
