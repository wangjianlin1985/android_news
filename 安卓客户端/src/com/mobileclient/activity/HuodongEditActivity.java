package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.Huodong;
import com.mobileclient.service.HuodongService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class HuodongEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明活动idTextView
	private TextView TV_huodongId;
	// 声明活动主题 输入框
	private EditText ET_title;
	// 声明活动内容输入框
	private EditText ET_content;
	// 声明报名电话输入框
	private EditText ET_telephone;
	// 声明参与名单输入框
	private EditText ET_personList;
	// 声明发起人下拉框
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*发起人管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	// 声明发布时间输入框
	private EditText ET_addTime;
	protected String carmera_path;
	/*要保存的活动信息信息*/
	Huodong huodong = new Huodong();
	/*活动信息管理业务逻辑层*/
	private HuodongService huodongService = new HuodongService();

	private int huodongId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.huodong_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑活动信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_huodongId = (TextView) findViewById(R.id.TV_huodongId);
		ET_title = (EditText) findViewById(R.id.ET_title);
		ET_content = (EditText) findViewById(R.id.ET_content);
		ET_telephone = (EditText) findViewById(R.id.ET_telephone);
		ET_personList = (EditText) findViewById(R.id.ET_personList);
		spinner_userObj = (Spinner) findViewById(R.id.Spinner_userObj);
		// 获取所有的发起人
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
		// 设置图书类别下拉列表的风格
		userObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_userObj.setAdapter(userObj_adapter);
		// 添加事件Spinner事件监听
		spinner_userObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				huodong.setUserObj(userInfoList.get(arg2).getUser_name()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_addTime = (EditText) findViewById(R.id.ET_addTime);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		huodongId = extras.getInt("huodongId");
		/*单击修改活动信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取活动主题 */ 
					if(ET_title.getText().toString().equals("")) {
						Toast.makeText(HuodongEditActivity.this, "活动主题 输入不能为空!", Toast.LENGTH_LONG).show();
						ET_title.setFocusable(true);
						ET_title.requestFocus();
						return;	
					}
					huodong.setTitle(ET_title.getText().toString());
					/*验证获取活动内容*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(HuodongEditActivity.this, "活动内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					huodong.setContent(ET_content.getText().toString());
					/*验证获取报名电话*/ 
					if(ET_telephone.getText().toString().equals("")) {
						Toast.makeText(HuodongEditActivity.this, "报名电话输入不能为空!", Toast.LENGTH_LONG).show();
						ET_telephone.setFocusable(true);
						ET_telephone.requestFocus();
						return;	
					}
					huodong.setTelephone(ET_telephone.getText().toString());
					/*验证获取参与名单*/ 
					if(ET_personList.getText().toString().equals("")) {
						Toast.makeText(HuodongEditActivity.this, "参与名单输入不能为空!", Toast.LENGTH_LONG).show();
						ET_personList.setFocusable(true);
						ET_personList.requestFocus();
						return;	
					}
					huodong.setPersonList(ET_personList.getText().toString());
					/*验证获取发布时间*/ 
					if(ET_addTime.getText().toString().equals("")) {
						Toast.makeText(HuodongEditActivity.this, "发布时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_addTime.setFocusable(true);
						ET_addTime.requestFocus();
						return;	
					}
					huodong.setAddTime(ET_addTime.getText().toString());
					/*调用业务逻辑层上传活动信息信息*/
					HuodongEditActivity.this.setTitle("正在更新活动信息信息，稍等...");
					String result = huodongService.UpdateHuodong(huodong);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    huodong = huodongService.GetHuodong(huodongId);
		this.TV_huodongId.setText(huodongId+"");
		this.ET_title.setText(huodong.getTitle());
		this.ET_content.setText(huodong.getContent());
		this.ET_telephone.setText(huodong.getTelephone());
		this.ET_personList.setText(huodong.getPersonList());
		for (int i = 0; i < userInfoList.size(); i++) {
			if (huodong.getUserObj().equals(userInfoList.get(i).getUser_name())) {
				this.spinner_userObj.setSelection(i);
				break;
			}
		}
		this.ET_addTime.setText(huodong.getAddTime());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
