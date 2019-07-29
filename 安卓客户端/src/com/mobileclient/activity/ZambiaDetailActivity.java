package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Zambia;
import com.mobileclient.service.ZambiaService;
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
public class ZambiaDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明赞id控件
	private TextView TV_zambiaId;
	// 声明被赞新闻控件
	private TextView TV_newsObj;
	// 声明用户控件
	private TextView TV_userObj;
	// 声明被赞时间控件
	private TextView TV_zambiaTime;
	/* 要保存的新闻赞信息 */
	Zambia zambia = new Zambia(); 
	/* 新闻赞管理业务逻辑层 */
	private ZambiaService zambiaService = new ZambiaService();
	private NewsService newsService = new NewsService();
	private UserInfoService userInfoService = new UserInfoService();
	private int zambiaId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.zambia_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看新闻赞详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_zambiaId = (TextView) findViewById(R.id.TV_zambiaId);
		TV_newsObj = (TextView) findViewById(R.id.TV_newsObj);
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		TV_zambiaTime = (TextView) findViewById(R.id.TV_zambiaTime);
		Bundle extras = this.getIntent().getExtras();
		zambiaId = extras.getInt("zambiaId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ZambiaDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    zambia = zambiaService.GetZambia(zambiaId); 
		this.TV_zambiaId.setText(zambia.getZambiaId() + "");
		News newsObj = newsService.GetNews(zambia.getNewsObj());
		this.TV_newsObj.setText(newsObj.getNewsTitle());
		UserInfo userObj = userInfoService.GetUserInfo(zambia.getUserObj());
		this.TV_userObj.setText(userObj.getName());
		this.TV_zambiaTime.setText(zambia.getZambiaTime());
	} 
}
