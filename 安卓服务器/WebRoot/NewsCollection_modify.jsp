<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.NewsCollection" %>
<%@ page import="com.chengxusheji.domain.News" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //��ȡ���е�News��Ϣ
    List<News> newsList = (List<News>)request.getAttribute("newsList");
    //��ȡ���е�UserInfo��Ϣ
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    NewsCollection newsCollection = (NewsCollection)request.getAttribute("newsCollection");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>�޸������ղ�</TITLE>
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
    var collectTime = document.getElementById("newsCollection.collectTime").value;
    if(collectTime=="") {
        alert('�������ղ�ʱ��!');
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
    <TD align="left" vAlign=top ><s:form action="NewsCollection/NewsCollection_ModifyNewsCollection.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>�ղ�id:</td>
    <td width=70%><input id="newsCollection.collectionId" name="newsCollection.collectionId" type="text" value="<%=newsCollection.getCollectionId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>���ղ�����:</td>
    <td width=70%>
      <select name="newsCollection.newsObj.newsId">
      <%
        for(News news:newsList) {
          String selected = "";
          if(news.getNewsId() == newsCollection.getNewsObj().getNewsId())
            selected = "selected";
      %>
          <option value='<%=news.getNewsId() %>' <%=selected %>><%=news.getNewsTitle() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>�ղ���:</td>
    <td width=70%>
      <select name="newsCollection.userObj.user_name">
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(newsCollection.getUserObj().getUser_name()))
            selected = "selected";
      %>
          <option value='<%=userInfo.getUser_name() %>' <%=selected %>><%=userInfo.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>�ղ�ʱ��:</td>
    <td width=70%><input id="newsCollection.collectTime" name="newsCollection.collectTime" type="text" size="30" value='<%=newsCollection.getCollectTime() %>'/></td>
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
