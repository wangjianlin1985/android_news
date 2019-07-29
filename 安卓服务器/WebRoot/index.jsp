<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%> <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>基于安卓Android新闻系统-首页</title>
<link href="<%=basePath %>css/index.css" rel="stylesheet" type="text/css" />
 </head>
<body>
<div id="container">
	<div id="banner"><img src="<%=basePath %>images/logo.gif" /></div>
	<div id="globallink">
		<ul>
			<li><a href="<%=basePath %>index.jsp">首页</a></li>
			<li><a href="<%=basePath %>NewsClass/NewsClass_FrontQueryNewsClass.action" target="OfficeMain">新闻分类</a></li> 
			<li><a href="<%=basePath %>News/News_FrontQueryNews.action" target="OfficeMain">新闻信息</a></li> 
			<li><a href="<%=basePath %>UserInfo/UserInfo_FrontQueryUserInfo.action" target="OfficeMain">用户信息</a></li> 
			<li><a href="<%=basePath %>NewsTag/NewsTag_FrontQueryNewsTag.action" target="OfficeMain">新闻标记</a></li> 
			<li><a href="<%=basePath %>NewsComment/NewsComment_FrontQueryNewsComment.action" target="OfficeMain">新闻评论</a></li> 
			<li><a href="<%=basePath %>NewsCollection/NewsCollection_FrontQueryNewsCollection.action" target="OfficeMain">新闻收藏</a></li> 
			<li><a href="<%=basePath %>Zambia/Zambia_FrontQueryZambia.action" target="OfficeMain">新闻赞</a></li> 
			<li><a href="<%=basePath %>PhotoShare/PhotoShare_FrontQueryPhotoShare.action" target="OfficeMain">图片分享</a></li> 
			<li><a href="<%=basePath %>PhotoComment/PhotoComment_FrontQueryPhotoComment.action" target="OfficeMain">图片评论</a></li> 
			<li><a href="<%=basePath %>VideoShare/VideoShare_FrontQueryVideoShare.action" target="OfficeMain">视频分享</a></li> 
			<li><a href="<%=basePath %>Huodong/Huodong_FrontQueryHuodong.action" target="OfficeMain">活动信息</a></li> 
		</ul>
		<br />
	</div> 
	<div id="main">
	 <iframe id="frame1" src="<%=basePath %>desk.jsp" name="OfficeMain" width="100%" height="100%" scrolling="yes" marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0 >
	 </iframe>
	</div>
	<div id="footer">
		<p>双鱼林设计 QQ:287307421或254540457 &copy;版权所有 <a href="http://www.shuangyulin.com" target="_blank">双鱼林设计网</a>&nbsp;&nbsp;<a href="<%=basePath%>login/login_view.action"><font color=red>后台登陆</font></a></p>
	</div>
</div>
</body>
</html>
