package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

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

public class VideoShareSimpleAdapter extends SimpleAdapter { 
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

    public VideoShareSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
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
	  if (convertView == null) convertView = mInflater.inflate(R.layout.videoshare_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_videoShareId = (TextView)convertView.findViewById(R.id.tv_videoShareId);
	  holder.tv_videoTitle = (TextView)convertView.findViewById(R.id.tv_videoTitle);
	  holder.iv_videoFile = (ImageView)convertView.findViewById(R.id.iv_videoFile);
	  holder.tv_userObj = (TextView)convertView.findViewById(R.id.tv_userObj);
	  holder.tv_shareTime = (TextView)convertView.findViewById(R.id.tv_shareTime);
	  /*设置各个控件的展示内容*/
	  holder.tv_videoShareId.setText("分享id：" + mData.get(position).get("videoShareId").toString());
	  holder.tv_videoTitle.setText("视频标题：" + mData.get(position).get("videoTitle").toString());
	  holder.iv_videoFile.setImageResource(R.drawable.default_photo);
	  ImageLoadListener videoFileLoadListener = new ImageLoadListener(mListView,R.id.iv_videoFile);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("videoFile"),videoFileLoadListener);  
	  holder.tv_userObj.setText("用户：" + (new UserInfoService()).GetUserInfo(mData.get(position).get("userObj").toString()).getName());
	  holder.tv_shareTime.setText("分享时间：" + mData.get(position).get("shareTime").toString());
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_videoShareId;
    	TextView tv_videoTitle;
    	ImageView iv_videoFile;
    	TextView tv_userObj;
    	TextView tv_shareTime;
    }
} 
