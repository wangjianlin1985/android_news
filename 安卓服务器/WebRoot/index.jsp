<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%> <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>���ڰ�׿Android����ϵͳ-��ҳ</title>
<link href="<%=basePath %>css/index.css" rel="stylesheet" type="text/css" />
 </head>
<body>
<div id="container">
	<div id="banner"><img src="<%=basePath %>images/logo.gif" /></div>
	<div id="globallink">
		<ul>
			<li><a href="<%=basePath %>index.jsp">��ҳ</a></li>
			<li><a href="<%=basePath %>NewsClass/NewsClass_FrontQueryNewsClass.action" target="OfficeMain">���ŷ���</a></li> 
			<li><a href="<%=basePath %>News/News_FrontQueryNews.action" target="OfficeMain">������Ϣ</a></li> 
			<li><a href="<%=basePath %>UserInfo/UserInfo_FrontQueryUserInfo.action" target="OfficeMain">�û���Ϣ</a></li> 
			<li><a href="<%=basePath %>NewsTag/NewsTag_FrontQueryNewsTag.action" target="OfficeMain">���ű��</a></li> 
			<li><a href="<%=basePath %>NewsComment/NewsComment_FrontQueryNewsComment.action" target="OfficeMain">��������</a></li> 
			<li><a href="<%=basePath %>NewsCollection/NewsCollection_FrontQueryNewsCollection.action" target="OfficeMain">�����ղ�</a></li> 
			<li><a href="<%=basePath %>Zambia/Zambia_FrontQueryZambia.action" target="OfficeMain">������</a></li> 
			<li><a href="<%=basePath %>PhotoShare/PhotoShare_FrontQueryPhotoShare.action" target="OfficeMain">ͼƬ����</a></li> 
			<li><a href="<%=basePath %>PhotoComment/PhotoComment_FrontQueryPhotoComment.action" target="OfficeMain">ͼƬ����</a></li> 
			<li><a href="<%=basePath %>VideoShare/VideoShare_FrontQueryVideoShare.action" target="OfficeMain">��Ƶ����</a></li> 
			<li><a href="<%=basePath %>Huodong/Huodong_FrontQueryHuodong.action" target="OfficeMain">���Ϣ</a></li> 
		</ul>
		<br />
	</div> 
	<div id="main">
	 <iframe id="frame1" src="<%=basePath %>desk.jsp" name="OfficeMain" width="100%" height="100%" scrolling="yes" marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0 >
	 </iframe>
	</div>
	<div id="footer">
		<p>˫������� QQ:287307421��254540457 &copy;��Ȩ���� <a href="http://www.shuangyulin.com" target="_blank">˫���������</a>&nbsp;&nbsp;<a href="<%=basePath%>login/login_view.action"><font color=red>��̨��½</font></a></p>
	</div>
</div>
</body>
</html>
