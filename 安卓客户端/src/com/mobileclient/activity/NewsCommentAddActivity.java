package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.NewsComment;
import com.mobileclient.service.NewsCommentService;
import com.mobileclient.domain.News;
import com.mobileclient.service.NewsService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class NewsCommentAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明被评新闻下拉框
	private Spinner spinner_newsObj;
	private ArrayAdapter<String> newsObj_adapter;
	private static  String[] newsObj_ShowText  = null;
	private List<News> newsList = null;
	/*被评新闻管理业务逻辑层*/
	private NewsService newsService = new NewsService();
	// 声明评论人下拉框
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*评论人管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	// 声明评论内容输入框
	private EditText ET_content;
	// 声明评论时间输入框
	private EditText ET_commentTime;
	protected String carmera_path;
	/*要保存的新闻评论信息*/
	NewsComment newsComment = new NewsComment();
	/*新闻评论管理业务逻辑层*/
	private NewsCommentService newsCommentService = new NewsCommentService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.newscomment_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加新闻评论");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		spinner_newsObj = (Spinner) findViewById(R.id.Spinner_newsObj);
		// 获取所有的被评新闻
		try {
			newsList = newsService.QueryNews(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int newsCount = newsList.size();
		newsObj_ShowText = new String[newsCount];
		for(int i=0;i<newsCount;i++) { 
			newsObj_ShowText[i] = newsList.get(i).getNewsTitle();
		}
		// 将可选内容与ArrayAdapter连接起来
		newsObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, newsObj_ShowText);
		// 设置下拉列表的风格
		newsObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_newsObj.setAdapter(newsObj_adapter);
		// 添加事件Spinner事件监听
		spinner_newsObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				newsComment.setNewsObj(newsList.get(arg2).getNewsId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_newsObj.setVisibility(View.VISIBLE);
		spinner_userObj = (Spinner) findViewById(R.id.Spinner_userObj);
		// 获取所有的评论人
		try {
			userInfoList = userInfoService.QueryUserInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int userInfoCount = userInfoList.size();
		userObj_ShowText = new String[userInfoCount];
		for(int i=0;i<userInfoCount;i++) { 
			userObj_ShowText[i] = userInfoList.get(i).getName();
		}
		// 将可选内容与ArrayAdapter连接起来
		userObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userObj_ShowText);
		// 设置下拉列表的风格
		userObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_userObj.setAdapter(userObj_adapter);
		// 添加事件Spinner事件监听
		spinner_userObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				newsComment.setUserObj(userInfoList.get(arg2).getUser_name()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_content = (EditText) findViewById(R.id.ET_content);
		ET_commentTime = (EditText) findViewById(R.id.ET_commentTime);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加新闻评论按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取评论内容*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(NewsCommentAddActivity.this, "评论内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					newsComment.setContent(ET_content.getText().toString());
					/*验证获取评论时间*/ 
					if(ET_commentTime.getText().toString().equals("")) {
						Toast.makeText(NewsCommentAddActivity.this, "评论时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_commentTime.setFocusable(true);
						ET_commentTime.requestFocus();
						return;	
					}
					newsComment.setCommentTime(ET_commentTime.getText().toString());
					/*调用业务逻辑层上传新闻评论信息*/
					NewsCommentAddActivity.this.setTitle("正在上传新闻评论信息，稍等...");
					String result = newsCommentService.AddNewsComment(newsComment);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
