package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.News;
import com.mobileclient.domain.NewsClass;
import com.mobileclient.service.NewsClassService;

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
public class NewsQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明新闻类别下拉框
	private Spinner spinner_newsClassObj;
	private ArrayAdapter<String> newsClassObj_adapter;
	private static  String[] newsClassObj_ShowText  = null;
	private List<NewsClass> newsClassList = null; 
	/*新闻分类管理业务逻辑层*/
	private NewsClassService newsClassService = new NewsClassService();
	// 声明新闻标题输入框
	private EditText ET_newsTitle;
	// 声明新闻来源输入框
	private EditText ET_comFrom;
	// 声明添加时间输入框
	private EditText ET_addTime;
	/*查询过滤条件保存到这个对象中*/
	private News queryConditionNews = new News();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.news_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置新闻信息查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_newsClassObj = (Spinner) findViewById(R.id.Spinner_newsClassObj);
		// 获取所有的新闻分类
		try {
			newsClassList = newsClassService.QueryNewsClass(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int newsClassCount = newsClassList.size();
		newsClassObj_ShowText = new String[newsClassCount+1];
		newsClassObj_ShowText[0] = "不限制";
		for(int i=1;i<=newsClassCount;i++) { 
			newsClassObj_ShowText[i] = newsClassList.get(i-1).getNewsClassName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		newsClassObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, newsClassObj_ShowText);
		// 设置新闻类别下拉列表的风格
		newsClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_newsClassObj.setAdapter(newsClassObj_adapter);
		// 添加事件Spinner事件监听
		spinner_newsClassObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionNews.setNewsClassObj(newsClassList.get(arg2-1).getNewsClassId()); 
				else
					queryConditionNews.setNewsClassObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_newsClassObj.setVisibility(View.VISIBLE);
		ET_newsTitle = (EditText) findViewById(R.id.ET_newsTitle);
		ET_comFrom = (EditText) findViewById(R.id.ET_comFrom);
		ET_addTime = (EditText) findViewById(R.id.ET_addTime);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionNews.setNewsTitle(ET_newsTitle.getText().toString());
					queryConditionNews.setComFrom(ET_comFrom.getText().toString());
					queryConditionNews.setAddTime(ET_addTime.getText().toString());
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionNews", queryConditionNews);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
