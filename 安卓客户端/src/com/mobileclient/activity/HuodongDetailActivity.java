package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Huodong;
import com.mobileclient.service.HuodongService;
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
public class HuodongDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明活动id控件
	private TextView TV_huodongId;
	// 声明活动主题 控件
	private TextView TV_title;
	// 声明活动内容控件
	private TextView TV_content;
	// 声明报名电话控件
	private TextView TV_telephone;
	// 声明参与名单控件
	private TextView TV_personList;
	// 声明发起人控件
	private TextView TV_userObj;
	// 声明发布时间控件
	private TextView TV_addTime;
	/* 要保存的活动信息信息 */
	Huodong huodong = new Huodong(); 
	/* 活动信息管理业务逻辑层 */
	private HuodongService huodongService = new HuodongService();
	private UserInfoService userInfoService = new UserInfoService();
	private int huodongId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.huodong_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看活动信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_huodongId = (TextView) findViewById(R.id.TV_huodongId);
		TV_title = (TextView) findViewById(R.id.TV_title);
		TV_content = (TextView) findViewById(R.id.TV_content);
		TV_telephone = (TextView) findViewById(R.id.TV_telephone);
		TV_personList = (TextView) findViewById(R.id.TV_personList);
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		TV_addTime = (TextView) findViewById(R.id.TV_addTime);
		Bundle extras = this.getIntent().getExtras();
		huodongId = extras.getInt("huodongId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HuodongDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    huodong = huodongService.GetHuodong(huodongId); 
		this.TV_huodongId.setText(huodong.getHuodongId() + "");
		this.TV_title.setText(huodong.getTitle());
		this.TV_content.setText(huodong.getContent());
		this.TV_telephone.setText(huodong.getTelephone());
		this.TV_personList.setText(huodong.getPersonList());
		UserInfo userObj = userInfoService.GetUserInfo(huodong.getUserObj());
		this.TV_userObj.setText(userObj.getName());
		this.TV_addTime.setText(huodong.getAddTime());
	} 
}
