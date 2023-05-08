package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.PhotoComment;
import com.mobileclient.service.PhotoCommentService;
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
public class PhotoCommentDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明评论id控件
	private TextView TV_photoCommentId;
	// 声明被评图片控件
	private TextView TV_photoObj;
	// 声明评论内容控件
	private TextView TV_content;
	// 声明用户控件
	private TextView TV_userInfoObj;
	// 声明评论时间控件
	private TextView TV_commentTime;
	/* 要保存的图片评论信息 */
	PhotoComment photoComment = new PhotoComment(); 
	/* 图片评论管理业务逻辑层 */
	private PhotoCommentService photoCommentService = new PhotoCommentService();
	private PhotoShareService photoShareService = new PhotoShareService();
	private UserInfoService userInfoService = new UserInfoService();
	private int photoCommentId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.photocomment_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看图片评论详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_photoCommentId = (TextView) findViewById(R.id.TV_photoCommentId);
		TV_photoObj = (TextView) findViewById(R.id.TV_photoObj);
		TV_content = (TextView) findViewById(R.id.TV_content);
		TV_userInfoObj = (TextView) findViewById(R.id.TV_userInfoObj);
		TV_commentTime = (TextView) findViewById(R.id.TV_commentTime);
		Bundle extras = this.getIntent().getExtras();
		photoCommentId = extras.getInt("photoCommentId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PhotoCommentDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    photoComment = photoCommentService.GetPhotoComment(photoCommentId); 
		this.TV_photoCommentId.setText(photoComment.getPhotoCommentId() + "");
		PhotoShare photoObj = photoShareService.GetPhotoShare(photoComment.getPhotoObj());
		this.TV_photoObj.setText(photoObj.getPhotoTitle());
		this.TV_content.setText(photoComment.getContent());
		UserInfo userInfoObj = userInfoService.GetUserInfo(photoComment.getUserInfoObj());
		this.TV_userInfoObj.setText(userInfoObj.getName());
		this.TV_commentTime.setText(photoComment.getCommentTime());
	} 
}
