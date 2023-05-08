package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.VideoShare;
import com.mobileclient.service.VideoShareService;
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
public class VideoShareAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明视频标题输入框
	private EditText ET_videoTitle;
	// 声明视频文件图片框控件
	private ImageView iv_videoFile;
	private Button btn_videoFile;
	protected int REQ_CODE_SELECT_IMAGE_videoFile = 1;
	private int REQ_CODE_CAMERA_videoFile = 2;
	// 声明用户下拉框
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*用户管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	// 声明分享时间输入框
	private EditText ET_shareTime;
	protected String carmera_path;
	/*要保存的视频分享信息*/
	VideoShare videoShare = new VideoShare();
	/*视频分享管理业务逻辑层*/
	private VideoShareService videoShareService = new VideoShareService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.videoshare_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加视频分享");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_videoTitle = (EditText) findViewById(R.id.ET_videoTitle);
		iv_videoFile = (ImageView) findViewById(R.id.iv_videoFile);
		/*单击图片显示控件时进行图片的选择*/
		iv_videoFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(VideoShareAddActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_videoFile);
			}
		});
		btn_videoFile = (Button) findViewById(R.id.btn_videoFile);
		btn_videoFile.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_videoFile.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_videoFile);  
			}
		});
		spinner_userObj = (Spinner) findViewById(R.id.Spinner_userObj);
		// 获取所有的用户
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
				videoShare.setUserObj(userInfoList.get(arg2).getUser_name()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_shareTime = (EditText) findViewById(R.id.ET_shareTime);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加视频分享按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取视频标题*/ 
					if(ET_videoTitle.getText().toString().equals("")) {
						Toast.makeText(VideoShareAddActivity.this, "视频标题输入不能为空!", Toast.LENGTH_LONG).show();
						ET_videoTitle.setFocusable(true);
						ET_videoTitle.requestFocus();
						return;	
					}
					videoShare.setVideoTitle(ET_videoTitle.getText().toString());
					if(videoShare.getVideoFile() != null) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						VideoShareAddActivity.this.setTitle("正在上传图片，稍等...");
						String videoFile = HttpUtil.uploadFile(videoShare.getVideoFile());
						VideoShareAddActivity.this.setTitle("图片上传完毕！");
						videoShare.setVideoFile(videoFile);
					} else {
						videoShare.setVideoFile("upload/noimage.jpg");
					}
					/*验证获取分享时间*/ 
					if(ET_shareTime.getText().toString().equals("")) {
						Toast.makeText(VideoShareAddActivity.this, "分享时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_shareTime.setFocusable(true);
						ET_shareTime.requestFocus();
						return;	
					}
					videoShare.setShareTime(ET_shareTime.getText().toString());
					/*调用业务逻辑层上传视频分享信息*/
					VideoShareAddActivity.this.setTitle("正在上传视频分享信息，稍等...");
					String result = videoShareService.AddVideoShare(videoShare);
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
		if (requestCode == REQ_CODE_CAMERA_videoFile  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_videoFile.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_videoFile.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_videoFile.setImageBitmap(booImageBm);
				this.iv_videoFile.setScaleType(ScaleType.FIT_CENTER);
				this.videoShare.setVideoFile(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_videoFile && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true; 
			BitmapFactory.decodeFile(filepath, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);
			opts.inJustDecodeBounds = false; 
			try { 
				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
				this.iv_videoFile.setImageBitmap(bm); 
				this.iv_videoFile.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			videoShare.setVideoFile(filename); 
		}
	}
}
