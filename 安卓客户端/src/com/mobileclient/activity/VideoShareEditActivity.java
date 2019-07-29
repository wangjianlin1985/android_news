package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class VideoShareEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明分享idTextView
	private TextView TV_videoShareId;
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

	private int videoShareId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.videoshare_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑视频分享信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_videoShareId = (TextView) findViewById(R.id.TV_videoShareId);
		ET_videoTitle = (EditText) findViewById(R.id.ET_videoTitle);
		iv_videoFile = (ImageView) findViewById(R.id.iv_videoFile);
		/*单击图片显示控件时进行图片的选择*/
		iv_videoFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(VideoShareEditActivity.this,photoListActivity.class);
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
		// 设置图书类别下拉列表的风格
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
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		videoShareId = extras.getInt("videoShareId");
		/*单击修改视频分享按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取视频标题*/ 
					if(ET_videoTitle.getText().toString().equals("")) {
						Toast.makeText(VideoShareEditActivity.this, "视频标题输入不能为空!", Toast.LENGTH_LONG).show();
						ET_videoTitle.setFocusable(true);
						ET_videoTitle.requestFocus();
						return;	
					}
					videoShare.setVideoTitle(ET_videoTitle.getText().toString());
					if (!videoShare.getVideoFile().startsWith("upload/")) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						VideoShareEditActivity.this.setTitle("正在上传图片，稍等...");
						String videoFile = HttpUtil.uploadFile(videoShare.getVideoFile());
						VideoShareEditActivity.this.setTitle("图片上传完毕！");
						videoShare.setVideoFile(videoFile);
					} 
					/*验证获取分享时间*/ 
					if(ET_shareTime.getText().toString().equals("")) {
						Toast.makeText(VideoShareEditActivity.this, "分享时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_shareTime.setFocusable(true);
						ET_shareTime.requestFocus();
						return;	
					}
					videoShare.setShareTime(ET_shareTime.getText().toString());
					/*调用业务逻辑层上传视频分享信息*/
					VideoShareEditActivity.this.setTitle("正在更新视频分享信息，稍等...");
					String result = videoShareService.UpdateVideoShare(videoShare);
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
	    videoShare = videoShareService.GetVideoShare(videoShareId);
		this.TV_videoShareId.setText(videoShareId+"");
		this.ET_videoTitle.setText(videoShare.getVideoTitle());
		byte[] videoFile_data = null;
		try {
			// 获取图片数据
			videoFile_data = ImageService.getImage(HttpUtil.BASE_URL + videoShare.getVideoFile());
			Bitmap videoFile = BitmapFactory.decodeByteArray(videoFile_data, 0, videoFile_data.length);
			this.iv_videoFile.setImageBitmap(videoFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < userInfoList.size(); i++) {
			if (videoShare.getUserObj().equals(userInfoList.get(i).getUser_name())) {
				this.spinner_userObj.setSelection(i);
				break;
			}
		}
		this.ET_shareTime.setText(videoShare.getShareTime());
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
