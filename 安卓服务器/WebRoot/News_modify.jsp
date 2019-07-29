<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.News" %>
<%@ page import="com.chengxusheji.domain.NewsClass" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的NewsClass信息
    List<NewsClass> newsClassList = (List<NewsClass>)request.getAttribute("newsClassList");
    News news = (News)request.getAttribute("news");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改新闻信息</TITLE>
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
    var newsTitle = document.getElementById("news.newsTitle").value;
    if(newsTitle=="") {
        alert('请输入新闻标题!');
        return false;
    }
    var content = document.getElementById("news.content").value;
    if(content=="") {
        alert('请输入新闻内容!');
        return false;
    }
    var comFrom = document.getElementById("news.comFrom").value;
    if(comFrom=="") {
        alert('请输入新闻来源!');
        return false;
    }
    var addTime = document.getElementById("news.addTime").value;
    if(addTime=="") {
        alert('请输入添加时间!');
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
    <TD align="left" vAlign=top ><s:form action="News/News_ModifyNews.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>新闻id:</td>
    <td width=70%><input id="news.newsId" name="news.newsId" type="text" value="<%=news.getNewsId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>新闻类别:</td>
    <td width=70%>
      <select name="news.newsClassObj.newsClassId">
      <%
        for(NewsClass newsClass:newsClassList) {
          String selected = "";
          if(newsClass.getNewsClassId() == news.getNewsClassObj().getNewsClassId())
            selected = "selected";
      %>
          <option value='<%=newsClass.getNewsClassId() %>' <%=selected %>><%=newsClass.getNewsClassName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>新闻标题:</td>
    <td width=70%><input id="news.newsTitle" name="news.newsTitle" type="text" size="50" value='<%=news.getNewsTitle() %>'/></td>
  </tr>

  <tr>
    <td width=30%>新闻图片:</td>
    <td width=70%><img src="<%=basePath %><%=news.getNewsPhoto() %>" width="200px" border="0px"/><br/>
    <input type=hidden name="news.newsPhoto" value="<%=news.getNewsPhoto() %>" />
    <input id="newsPhotoFile" name="newsPhotoFile" type="file" size="50" /></td>
  </tr>
  <tr>
    <td width=30%>新闻内容:</td>
    <td width=70%><textarea id="news.content" name="news.content" rows=5 cols=50><%=news.getContent() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>新闻来源:</td>
    <td width=70%><input id="news.comFrom" name="news.comFrom" type="text" size="20" value='<%=news.getComFrom() %>'/></td>
  </tr>

  <tr>
    <td width=30%>浏览次数:</td>
    <td width=70%><input id="news.hitNum" name="news.hitNum" type="text" size="8" value='<%=news.getHitNum() %>'/></td>
  </tr>

  <tr>
    <td width=30%>添加时间:</td>
    <td width=70%><input id="news.addTime" name="news.addTime" type="text" size="20" value='<%=news.getAddTime() %>'/></td>
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
