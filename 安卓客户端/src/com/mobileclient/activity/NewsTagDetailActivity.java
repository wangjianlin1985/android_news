package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.NewsTag;
import com.mobileclient.service.NewsTagService;
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
public class NewsTagDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明标记id控件
	private TextView TV_tagId;
	// 声明被标记新闻控件
	private TextView TV_newsObj;
	// 声明标记的用户控件
	private TextView TV_userObj;
	// 声明新闻状态控件
	private TextView TV_newsState;
	// 声明标记时间控件
	private TextView TV_tagTime;
	/* 要保存的新闻标记信息 */
	NewsTag newsTag = new NewsTag(); 
	/* 新闻标记管理业务逻辑层 */
	private NewsTagService newsTagService = new NewsTagService();
	private NewsService newsService = new NewsService();
	private UserInfoService userInfoService = new UserInfoService();
	private int tagId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.newstag_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看新闻标记详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_tagId = (TextView) findViewById(R.id.TV_tagId);
		TV_newsObj = (TextView) findViewById(R.id.TV_newsObj);
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		TV_newsState = (TextView) findViewById(R.id.TV_newsState);
		TV_tagTime = (TextView) findViewById(R.id.TV_tagTime);
		Bundle extras = this.getIntent().getExtras();
		tagId = extras.getInt("tagId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NewsTagDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    newsTag = newsTagService.GetNewsTag(tagId); 
		this.TV_tagId.setText(newsTag.getTagId() + "");
		News newsObj = newsService.GetNews(newsTag.getNewsObj());
		this.TV_newsObj.setText(newsObj.getNewsTitle());
		UserInfo userObj = userInfoService.GetUserInfo(newsTag.getUserObj());
		this.TV_userObj.setText(userObj.getName());
		this.TV_newsState.setText(newsTag.getNewsState() + "");
		this.TV_tagTime.setText(newsTag.getTagTime());
	} 
}
