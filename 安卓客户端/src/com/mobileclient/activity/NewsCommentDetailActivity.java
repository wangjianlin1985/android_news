package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.NewsComment;
import com.mobileclient.service.NewsCommentService;
import com.mobileclient.domain.News;
import com.mobileclient.service.NewsService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
public class NewsCommentDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明评论id控件
	private TextView TV_commentId;
	// 声明被评新闻控件
	private TextView TV_newsObj;
	// 声明评论人控件
	private TextView TV_userObj;
	// 声明评论内容控件
	private TextView TV_content;
	// 声明评论时间控件
	private TextView TV_commentTime;
	/* 要保存的新闻评论信息 */
	NewsComment newsComment = new NewsComment(); 
	/* 新闻评论管理业务逻辑层 */
	private NewsCommentService newsCommentService = new NewsCommentService();
	private NewsService newsService = new NewsService();
	private UserInfoService userInfoService = new UserInfoService();
	private int commentId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.newscomment_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看新闻评论详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_commentId = (TextView) findViewById(R.id.TV_commentId);
		TV_newsObj = (TextView) findViewById(R.id.TV_newsObj);
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		TV_content = (TextView) findViewById(R.id.TV_content);
		TV_commentTime = (TextView) findViewById(R.id.TV_commentTime);
		Bundle extras = this.getIntent().getExtras();
		commentId = extras.getInt("commentId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NewsCommentDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    newsComment = newsCommentService.GetNewsComment(commentId); 
		this.TV_commentId.setText(newsComment.getCommentId() + "");
		News newsObj = newsService.GetNews(newsComment.getNewsObj());
		this.TV_newsObj.setText(newsObj.getNewsTitle());
		UserInfo userObj = userInfoService.GetUserInfo(newsComment.getUserObj());
		this.TV_userObj.setText(userObj.getName());
		this.TV_content.setText(newsComment.getContent());
		this.TV_commentTime.setText(newsComment.getCommentTime());
	} 
}
