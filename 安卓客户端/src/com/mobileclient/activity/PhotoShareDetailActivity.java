package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.PhotoShare;
import com.mobileclient.service.PhotoShareService;
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
public class PhotoShareDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明分享id控件
	private TextView TV_sharePhotoId;
	// 声明图片标题控件
	private TextView TV_photoTitle;
	// 声明图片图片框
	private ImageView iv_sharePhoto;
	// 声明上传用户控件
	private TextView TV_userInfoObj;
	// 声明分享时间控件
	private TextView TV_shareTime;
	/* 要保存的图片分享信息 */
	PhotoShare photoShare = new PhotoShare(); 
	/* 图片分享管理业务逻辑层 */
	private PhotoShareService photoShareService = new PhotoShareService();
	private UserInfoService userInfoService = new UserInfoService();
	private int sharePhotoId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.photoshare_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看图片分享详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_sharePhotoId = (TextView) findViewById(R.id.TV_sharePhotoId);
		TV_photoTitle = (TextView) findViewById(R.id.TV_photoTitle);
		iv_sharePhoto = (ImageView) findViewById(R.id.iv_sharePhoto); 
		TV_userInfoObj = (TextView) findViewById(R.id.TV_userInfoObj);
		TV_shareTime = (TextView) findViewById(R.id.TV_shareTime);
		Bundle extras = this.getIntent().getExtras();
		sharePhotoId = extras.getInt("sharePhotoId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PhotoShareDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    photoShare = photoShareService.GetPhotoShare(sharePhotoId); 
		this.TV_sharePhotoId.setText(photoShare.getSharePhotoId() + "");
		this.TV_photoTitle.setText(photoShare.getPhotoTitle());
		byte[] sharePhoto_data = null;
		try {
			// 获取图片数据
			sharePhoto_data = ImageService.getImage(HttpUtil.BASE_URL + photoShare.getSharePhoto());
			Bitmap sharePhoto = BitmapFactory.decodeByteArray(sharePhoto_data, 0,sharePhoto_data.length);
			this.iv_sharePhoto.setImageBitmap(sharePhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserInfo userInfoObj = userInfoService.GetUserInfo(photoShare.getUserInfoObj());
		this.TV_userInfoObj.setText(userInfoObj.getName());
		this.TV_shareTime.setText(photoShare.getShareTime());
	} 
}
