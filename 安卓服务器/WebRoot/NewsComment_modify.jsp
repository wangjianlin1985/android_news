<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.NewsComment" %>
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
    NewsComment newsComment = (NewsComment)request.getAttribute("newsComment");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改新闻评论</TITLE>
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
    var content = document.getElementById("newsComment.content").value;
    if(content=="") {
        alert('请输入评论内容!');
        return false;
    }
    var commentTime = document.getElementById("newsComment.commentTime").value;
    if(commentTime=="") {
        alert('请输入评论时间!');
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
    <TD align="left" vAlign=top ><s:form action="NewsComment/NewsComment_ModifyNewsComment.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>评论id:</td>
    <td width=70%><input id="newsComment.commentId" name="newsComment.commentId" type="text" value="<%=newsComment.getCommentId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>被评新闻:</td>
    <td width=70%>
      <select name="newsComment.newsObj.newsId">
      <%
        for(News news:newsList) {
          String selected = "";
          if(news.getNewsId() == newsComment.getNewsObj().getNewsId())
            selected = "selected";
      %>
          <option value='<%=news.getNewsId() %>' <%=selected %>><%=news.getNewsTitle() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>评论人:</td>
    <td width=70%>
      <select name="newsComment.userObj.user_name">
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(newsComment.getUserObj().getUser_name()))
            selected = "selected";
      %>
          <option value='<%=userInfo.getUser_name() %>' <%=selected %>><%=userInfo.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>评论内容:</td>
    <td width=70%><textarea id="newsComment.content" name="newsComment.content" rows=5 cols=50><%=newsComment.getContent() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>评论时间:</td>
    <td width=70%><input id="newsComment.commentTime" name="newsComment.commentTime" type="text" size="30" value='<%=newsComment.getCommentTime() %>'/></td>
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
