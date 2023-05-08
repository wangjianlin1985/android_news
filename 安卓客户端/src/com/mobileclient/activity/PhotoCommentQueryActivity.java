package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.PhotoComment;
import com.mobileclient.domain.PhotoShare;
import com.mobileclient.service.PhotoShareService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.ImageView;
import android.widget.TextView;
public class PhotoCommentQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明被评图片下拉框
	private Spinner spinner_photoObj;
	private ArrayAdapter<String> photoObj_adapter;
	private static  String[] photoObj_ShowText  = null;
	private List<PhotoShare> photoShareList = null; 
	/*图片分享管理业务逻辑层*/
	private PhotoShareService photoShareService = new PhotoShareService();
	// 声明评论内容输入框
	private EditText ET_content;
	// 声明用户下拉框
	private Spinner spinner_userInfoObj;
	private ArrayAdapter<String> userInfoObj_adapter;
	private static  String[] userInfoObj_ShowText  = null;
	private List<UserInfo> userInfoList = null; 
	/*用户信息管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	/*查询过滤条件保存到这个对象中*/
	private PhotoComment queryConditionPhotoComment = new PhotoComment();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.photocomment_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置图片评论查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_photoObj = (Spinner) findViewById(R.id.Spinner_photoObj);
		// 获取所有的图片分享
		try {
			photoShareList = photoShareService.QueryPhotoShare(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int photoShareCount = photoShareList.size();
		photoObj_ShowText = new String[photoShareCount+1];
		photoObj_ShowText[0] = "不限制";
		for(int i=1;i<=photoShareCount;i++) { 
			photoObj_ShowText[i] = photoShareList.get(i-1).getPhotoTitle();
		} 
		// 将可选内容与ArrayAdapter连接起来
		photoObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, photoObj_ShowText);
		// 设置被评图片下拉列表的风格
		photoObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_photoObj.setAdapter(photoObj_adapter);
		// 添加事件Spinner事件监听
		spinner_photoObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionPhotoComment.setPhotoObj(photoShareList.get(arg2-1).getSharePhotoId()); 
				else
					queryConditionPhotoComment.setPhotoObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_photoObj.setVisibility(View.VISIBLE);
		ET_content = (EditText) findViewById(R.id.ET_content);
		spinner_userInfoObj = (Spinner) findViewById(R.id.Spinner_userInfoObj);
		// 获取所有的用户信息
		try {
			userInfoList = userInfoService.QueryUserInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int userInfoCount = userInfoList.size();
		userInfoObj_ShowText = new String[userInfoCount+1];
		userInfoObj_ShowText[0] = "不限制";
		for(int i=1;i<=userInfoCount;i++) { 
			userInfoObj_ShowText[i] = userInfoList.get(i-1).getName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		userInfoObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userInfoObj_ShowText);
		// 设置用户下拉列表的风格
		userInfoObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_userInfoObj.setAdapter(userInfoObj_adapter);
		// 添加事件Spinner事件监听
		spinner_userInfoObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionPhotoComment.setUserInfoObj(userInfoList.get(arg2-1).getUser_name()); 
				else
					queryConditionPhotoComment.setUserInfoObj("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userInfoObj.setVisibility(View.VISIBLE);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionPhotoComment.setContent(ET_content.getText().toString());
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionPhotoComment", queryConditionPhotoComment);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
