package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.PhotoComment;
import com.mobileclient.service.PhotoCommentService;
import com.mobileclient.domain.PhotoShare;
import com.mobileclient.service.PhotoShareService;
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
public class PhotoCommentAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明被评图片下拉框
	private Spinner spinner_photoObj;
	private ArrayAdapter<String> photoObj_adapter;
	private static  String[] photoObj_ShowText  = null;
	private List<PhotoShare> photoShareList = null;
	/*被评图片管理业务逻辑层*/
	private PhotoShareService photoShareService = new PhotoShareService();
	// 声明评论内容输入框
	private EditText ET_content;
	// 声明用户下拉框
	private Spinner spinner_userInfoObj;
	private ArrayAdapter<String> userInfoObj_adapter;
	private static  String[] userInfoObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*用户管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	// 声明评论时间输入框
	private EditText ET_commentTime;
	protected String carmera_path;
	/*要保存的图片评论信息*/
	PhotoComment photoComment = new PhotoComment();
	/*图片评论管理业务逻辑层*/
	private PhotoCommentService photoCommentService = new PhotoCommentService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.photocomment_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加图片评论");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		spinner_photoObj = (Spinner) findViewById(R.id.Spinner_photoObj);
		// 获取所有的被评图片
		try {
			photoShareList = photoShareService.QueryPhotoShare(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int photoShareCount = photoShareList.size();
		photoObj_ShowText = new String[photoShareCount];
		for(int i=0;i<photoShareCount;i++) { 
			photoObj_ShowText[i] = photoShareList.get(i).getPhotoTitle();
		}
		// 将可选内容与ArrayAdapter连接起来
		photoObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, photoObj_ShowText);
		// 设置下拉列表的风格
		photoObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_photoObj.setAdapter(photoObj_adapter);
		// 添加事件Spinner事件监听
		spinner_photoObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				photoComment.setPhotoObj(photoShareList.get(arg2).getSharePhotoId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_photoObj.setVisibility(View.VISIBLE);
		ET_content = (EditText) findViewById(R.id.ET_content);
		spinner_userInfoObj = (Spinner) findViewById(R.id.Spinner_userInfoObj);
		// 获取所有的用户
		try {
			userInfoList = userInfoService.QueryUserInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int userInfoCount = userInfoList.size();
		userInfoObj_ShowText = new String[userInfoCount];
		for(int i=0;i<userInfoCount;i++) { 
			userInfoObj_ShowText[i] = userInfoList.get(i).getName();
		}
		// 将可选内容与ArrayAdapter连接起来
		userInfoObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userInfoObj_ShowText);
		// 设置下拉列表的风格
		userInfoObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_userInfoObj.setAdapter(userInfoObj_adapter);
		// 添加事件Spinner事件监听
		spinner_userInfoObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				photoComment.setUserInfoObj(userInfoList.get(arg2).getUser_name()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userInfoObj.setVisibility(View.VISIBLE);
		ET_commentTime = (EditText) findViewById(R.id.ET_commentTime);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加图片评论按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取评论内容*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(PhotoCommentAddActivity.this, "评论内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					photoComment.setContent(ET_content.getText().toString());
					/*验证获取评论时间*/ 
					if(ET_commentTime.getText().toString().equals("")) {
						Toast.makeText(PhotoCommentAddActivity.this, "评论时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_commentTime.setFocusable(true);
						ET_commentTime.requestFocus();
						return;	
					}
					photoComment.setCommentTime(ET_commentTime.getText().toString());
					/*调用业务逻辑层上传图片评论信息*/
					PhotoCommentAddActivity.this.setTitle("正在上传图片评论信息，稍等...");
					String result = photoCommentService.AddPhotoComment(photoComment);
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
