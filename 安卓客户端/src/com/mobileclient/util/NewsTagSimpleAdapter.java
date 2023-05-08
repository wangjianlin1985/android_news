package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.NewsService;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ImageLoadListener;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import android.content.Context;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.ListView;
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class NewsTagSimpleAdapter extends SimpleAdapter { 
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //图片异步缓存加载类,带内存缓存和文件缓存
    private SyncImageLoader syncImageLoader;

    public NewsTagSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
        mListView = listView; 
        syncImageLoader = SyncImageLoader.getInstance();
        ListViewOnScrollListener onScrollListener = new ListViewOnScrollListener(syncImageLoader,listView,getCount());
        mListView.setOnScrollListener(onScrollListener);
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
	  ViewHolder holder = null;
	  ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.newstag_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_tagId = (TextView)convertView.findViewById(R.id.tv_tagId);
	  holder.tv_newsObj = (TextView)convertView.findViewById(R.id.tv_newsObj);
	  holder.tv_userObj = (TextView)convertView.findViewById(R.id.tv_userObj);
	  holder.tv_newsState = (TextView)convertView.findViewById(R.id.tv_newsState);
	  holder.tv_tagTime = (TextView)convertView.findViewById(R.id.tv_tagTime);
	  /*设置各个控件的展示内容*/
	  holder.tv_tagId.setText("标记id：" + mData.get(position).get("tagId").toString());
	  holder.tv_newsObj.setText("被标记新闻：" + (new NewsService()).GetNews(Integer.parseInt(mData.get(position).get("newsObj").toString())).getNewsTitle());
	  holder.tv_userObj.setText("标记的用户：" + (new UserInfoService()).GetUserInfo(mData.get(position).get("userObj").toString()).getName());
	  holder.tv_newsState.setText("新闻状态：" + mData.get(position).get("newsState").toString());
	  holder.tv_tagTime.setText("标记时间：" + mData.get(position).get("tagTime").toString());
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_tagId;
    	TextView tv_newsObj;
    	TextView tv_userObj;
    	TextView tv_newsState;
    	TextView tv_tagTime;
    }
} 
