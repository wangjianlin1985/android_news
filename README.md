# android_news
# 安卓Android新闻发布系统app

系统开发环境: Windows + Myclipse(服务器端) + Eclipse(手机客户端) + mysql数据库

服务器也可以用Eclipse或者idea等工具，客户端也可以采用android studio工具！

系统客户端和服务器端架构技术: 界面层，业务逻辑层，数据层3层分离技术，MVC设计思想！

服务器和客户端数据通信格式：json格式,采用servlet方式

【服务器端采用SSH框架，请自己启动tomcat服务器，hibernate会自动生成数据库表的哈！】

hibernate生成数据库表后，只需要在admin管理员表中加个测试账号密码就可以登录后台了哈！

下面是数据库的字段说明：

新闻分类: 分类id,分类名称

新闻信息: 新闻id,新闻类别,新闻标题,新闻图片,新闻内容,新闻来源,浏览次数,添加时间

用户信息: 用户名,密码,姓名,性别,出生日期,联系电话,邮箱地址,家庭地址,照片,附加信息

新闻标记: 标记id,被标记新闻,标记的用户,新闻状态,标记时间

新闻评论: 评论id,被评新闻,评论人,评论内容,评论时间

新闻收藏: 收藏id,被收藏新闻,收藏人,收藏时间

新闻赞: 赞id,被赞新闻,用户,被赞时间

图片分享: 分享id,图片标题,图片,上传用户,分享时间

图片评论: 评论id,被评图片,评论内容,用户,评论时间

视频分享: 分享id,视频标题,视频文件,用户,分享时间

活动信息: 活动id,活动主题 ,活动内容,报名电话,参与名单,发起人,发布时间
