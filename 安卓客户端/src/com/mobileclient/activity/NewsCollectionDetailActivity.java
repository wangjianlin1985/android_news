package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.NewsCollection;
import com.mobileclient.service.NewsCollectionService;
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
public class NewsCollectionDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明收藏id控件
	private TextView TV_collectionId;
	// 声明被收藏新闻控件
	private TextView TV_newsObj;
	// 声明收藏人控件
	private TextView TV_userObj;
	// 声明收藏时间控件
	private TextView TV_collectTime;
	/* 要保存的新闻收藏信息 */
	NewsCollection newsCollection = new NewsCollection(); 
	/* 新闻收藏管理业务逻辑层 */
	private NewsCollectionService newsCollectionService = new NewsCollectionService();
	private NewsService newsService = new NewsService();
	private UserInfoService userInfoService = new UserInfoService();
	private int collectionId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.newscollection_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看新闻收藏详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_collectionId = (TextView) findViewById(R.id.TV_collectionId);
		TV_newsObj = (TextView) findViewById(R.id.TV_newsObj);
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		TV_collectTime = (TextView) findViewById(R.id.TV_collectTime);
		Bundle extras = this.getIntent().getExtras();
		collectionId = extras.getInt("collectionId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NewsCollectionDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    newsCollection = newsCollectionService.GetNewsCollection(collectionId); 
		this.TV_collectionId.setText(newsCollection.getCollectionId() + "");
		News newsObj = newsService.GetNews(newsCollection.getNewsObj());
		this.TV_newsObj.setText(newsObj.getNewsTitle());
		UserInfo userObj = userInfoService.GetUserInfo(newsCollection.getUserObj());
		this.TV_userObj.setText(userObj.getName());
		this.TV_collectTime.setText(newsCollection.getCollectTime());
	} 
}
