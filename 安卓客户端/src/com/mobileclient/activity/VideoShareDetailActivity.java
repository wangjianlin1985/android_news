package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.VideoShare;
import com.mobileclient.service.VideoShareService;
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
public class VideoShareDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明分享id控件
	private TextView TV_videoShareId;
	// 声明视频标题控件
	private TextView TV_videoTitle;
	// 声明视频文件图片框
	private ImageView iv_videoFile;
	// 声明用户控件
	private TextView TV_userObj;
	// 声明分享时间控件
	private TextView TV_shareTime;
	/* 要保存的视频分享信息 */
	VideoShare videoShare = new VideoShare(); 
	/* 视频分享管理业务逻辑层 */
	private VideoShareService videoShareService = new VideoShareService();
	private UserInfoService userInfoService = new UserInfoService();
	private int videoShareId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.videoshare_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看视频分享详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_videoShareId = (TextView) findViewById(R.id.TV_videoShareId);
		TV_videoTitle = (TextView) findViewById(R.id.TV_videoTitle);
		iv_videoFile = (ImageView) findViewById(R.id.iv_videoFile); 
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		TV_shareTime = (TextView) findViewById(R.id.TV_shareTime);
		Bundle extras = this.getIntent().getExtras();
		videoShareId = extras.getInt("videoShareId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				VideoShareDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    videoShare = videoShareService.GetVideoShare(videoShareId); 
		this.TV_videoShareId.setText(videoShare.getVideoShareId() + "");
		this.TV_videoTitle.setText(videoShare.getVideoTitle());
		byte[] videoFile_data = null;
		try {
			// 获取图片数据
			videoFile_data = ImageService.getImage(HttpUtil.BASE_URL + videoShare.getVideoFile());
			Bitmap videoFile = BitmapFactory.decodeByteArray(videoFile_data, 0,videoFile_data.length);
			this.iv_videoFile.setImageBitmap(videoFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserInfo userObj = userInfoService.GetUserInfo(videoShare.getUserObj());
		this.TV_userObj.setText(userObj.getName());
		this.TV_shareTime.setText(videoShare.getShareTime());
	} 
}
