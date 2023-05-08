package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.News;
import com.mobileclient.service.NewsService;
import com.mobileclient.domain.NewsClass;
import com.mobileclient.service.NewsClassService;
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

public class NewsEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明新闻idTextView
	private TextView TV_newsId;
	// 声明新闻类别下拉框
	private Spinner spinner_newsClassObj;
	private ArrayAdapter<String> newsClassObj_adapter;
	private static  String[] newsClassObj_ShowText  = null;
	private List<NewsClass> newsClassList = null;
	/*新闻类别管理业务逻辑层*/
	private NewsClassService newsClassService = new NewsClassService();
	// 声明新闻标题输入框
	private EditText ET_newsTitle;
	// 声明新闻图片图片框控件
	private ImageView iv_newsPhoto;
	private Button btn_newsPhoto;
	protected int REQ_CODE_SELECT_IMAGE_newsPhoto = 1;
	private int REQ_CODE_CAMERA_newsPhoto = 2;
	// 声明新闻内容输入框
	private EditText ET_content;
	// 声明新闻来源输入框
	private EditText ET_comFrom;
	// 声明浏览次数输入框
	private EditText ET_hitNum;
	// 声明添加时间输入框
	private EditText ET_addTime;
	protected String carmera_path;
	/*要保存的新闻信息信息*/
	News news = new News();
	/*新闻信息管理业务逻辑层*/
	private NewsService newsService = new NewsService();

	private int newsId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.news_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑新闻信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_newsId = (TextView) findViewById(R.id.TV_newsId);
		spinner_newsClassObj = (Spinner) findViewById(R.id.Spinner_newsClassObj);
		// 获取所有的新闻类别
		try {
			newsClassList = newsClassService.QueryNewsClass(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int newsClassCount = newsClassList.size();
		newsClassObj_ShowText = new String[newsClassCount];
		for(int i=0;i<newsClassCount;i++) { 
			newsClassObj_ShowText[i] = newsClassList.get(i).getNewsClassName();
		}
		// 将可选内容与ArrayAdapter连接起来
		newsClassObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, newsClassObj_ShowText);
		// 设置图书类别下拉列表的风格
		newsClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_newsClassObj.setAdapter(newsClassObj_adapter);
		// 添加事件Spinner事件监听
		spinner_newsClassObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				news.setNewsClassObj(newsClassList.get(arg2).getNewsClassId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_newsClassObj.setVisibility(View.VISIBLE);
		ET_newsTitle = (EditText) findViewById(R.id.ET_newsTitle);
		iv_newsPhoto = (ImageView) findViewById(R.id.iv_newsPhoto);
		/*单击图片显示控件时进行图片的选择*/
		iv_newsPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(NewsEditActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_newsPhoto);
			}
		});
		btn_newsPhoto = (Button) findViewById(R.id.btn_newsPhoto);
		btn_newsPhoto.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_newsPhoto.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_newsPhoto);  
			}
		});
		ET_content = (EditText) findViewById(R.id.ET_content);
		ET_comFrom = (EditText) findViewById(R.id.ET_comFrom);
		ET_hitNum = (EditText) findViewById(R.id.ET_hitNum);
		ET_addTime = (EditText) findViewById(R.id.ET_addTime);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		newsId = extras.getInt("newsId");
		/*单击修改新闻信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取新闻标题*/ 
					if(ET_newsTitle.getText().toString().equals("")) {
						Toast.makeText(NewsEditActivity.this, "新闻标题输入不能为空!", Toast.LENGTH_LONG).show();
						ET_newsTitle.setFocusable(true);
						ET_newsTitle.requestFocus();
						return;	
					}
					news.setNewsTitle(ET_newsTitle.getText().toString());
					if (!news.getNewsPhoto().startsWith("upload/")) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						NewsEditActivity.this.setTitle("正在上传图片，稍等...");
						String newsPhoto = HttpUtil.uploadFile(news.getNewsPhoto());
						NewsEditActivity.this.setTitle("图片上传完毕！");
						news.setNewsPhoto(newsPhoto);
					} 
					/*验证获取新闻内容*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(NewsEditActivity.this, "新闻内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					news.setContent(ET_content.getText().toString());
					/*验证获取新闻来源*/ 
					if(ET_comFrom.getText().toString().equals("")) {
						Toast.makeText(NewsEditActivity.this, "新闻来源输入不能为空!", Toast.LENGTH_LONG).show();
						ET_comFrom.setFocusable(true);
						ET_comFrom.requestFocus();
						return;	
					}
					news.setComFrom(ET_comFrom.getText().toString());
					/*验证获取浏览次数*/ 
					if(ET_hitNum.getText().toString().equals("")) {
						Toast.makeText(NewsEditActivity.this, "浏览次数输入不能为空!", Toast.LENGTH_LONG).show();
						ET_hitNum.setFocusable(true);
						ET_hitNum.requestFocus();
						return;	
					}
					news.setHitNum(Integer.parseInt(ET_hitNum.getText().toString()));
					/*验证获取添加时间*/ 
					if(ET_addTime.getText().toString().equals("")) {
						Toast.makeText(NewsEditActivity.this, "添加时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_addTime.setFocusable(true);
						ET_addTime.requestFocus();
						return;	
					}
					news.setAddTime(ET_addTime.getText().toString());
					/*调用业务逻辑层上传新闻信息信息*/
					NewsEditActivity.this.setTitle("正在更新新闻信息信息，稍等...");
					String result = newsService.UpdateNews(news);
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
	    news = newsService.GetNews(newsId);
		this.TV_newsId.setText(newsId+"");
		for (int i = 0; i < newsClassList.size(); i++) {
			if (news.getNewsClassObj() == newsClassList.get(i).getNewsClassId()) {
				this.spinner_newsClassObj.setSelection(i);
				break;
			}
		}
		this.ET_newsTitle.setText(news.getNewsTitle());
		byte[] newsPhoto_data = null;
		try {
			// 获取图片数据
			newsPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + news.getNewsPhoto());
			Bitmap newsPhoto = BitmapFactory.decodeByteArray(newsPhoto_data, 0, newsPhoto_data.length);
			this.iv_newsPhoto.setImageBitmap(newsPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.ET_content.setText(news.getContent());
		this.ET_comFrom.setText(news.getComFrom());
		this.ET_hitNum.setText(news.getHitNum() + "");
		this.ET_addTime.setText(news.getAddTime());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_newsPhoto  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_newsPhoto.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_newsPhoto.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_newsPhoto.setImageBitmap(booImageBm);
				this.iv_newsPhoto.setScaleType(ScaleType.FIT_CENTER);
				this.news.setNewsPhoto(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_newsPhoto && resultCode == Activity.RESULT_OK) {
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
				this.iv_newsPhoto.setImageBitmap(bm); 
				this.iv_newsPhoto.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			news.setNewsPhoto(filename); 
		}
	}
}
