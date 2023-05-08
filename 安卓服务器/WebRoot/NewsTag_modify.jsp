<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.NewsTag" %>
<%@ page import="com.chengxusheji.domain.News" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的News信息
    List<News> newsList = (List<News>)request.getAttribute("newsList");
    //获取所有的UserInfo信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    NewsTag newsTag = (NewsTag)request.getAttribute("newsTag");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改新闻标记</TITLE>
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
    var tagTime = document.getElementById("newsTag.tagTime").value;
    if(tagTime=="") {
        alert('请输入标记时间!');
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
    <TD align="left" vAlign=top ><s:form action="NewsTag/NewsTag_ModifyNewsTag.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>标记id:</td>
    <td width=70%><input id="newsTag.tagId" name="newsTag.tagId" type="text" value="<%=newsTag.getTagId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>被标记新闻:</td>
    <td width=70%>
      <select name="newsTag.newsObj.newsId">
      <%
        for(News news:newsList) {
          String selected = "";
          if(news.getNewsId() == newsTag.getNewsObj().getNewsId())
            selected = "selected";
      %>
          <option value='<%=news.getNewsId() %>' <%=selected %>><%=news.getNewsTitle() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>标记的用户:</td>
    <td width=70%>
      <select name="newsTag.userObj.user_name">
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(newsTag.getUserObj().getUser_name()))
            selected = "selected";
      %>
          <option value='<%=userInfo.getUser_name() %>' <%=selected %>><%=userInfo.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>新闻状态:</td>
    <td width=70%><input id="newsTag.newsState" name="newsTag.newsState" type="text" size="8" value='<%=newsTag.getNewsState() %>'/></td>
  </tr>

  <tr>
    <td width=30%>标记时间:</td>
    <td width=70%><input id="newsTag.tagTime" name="newsTag.tagTime" type="text" size="30" value='<%=newsTag.getTagTime() %>'/></td>
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
