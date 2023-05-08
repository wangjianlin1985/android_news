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

public class PhotoShareSimpleAdapter extends SimpleAdapter { 
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

    public PhotoShareSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
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
	  if (convertView == null) convertView = mInflater.inflate(R.layout.photoshare_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_sharePhotoId = (TextView)convertView.findViewById(R.id.tv_sharePhotoId);
	  holder.tv_photoTitle = (TextView)convertView.findViewById(R.id.tv_photoTitle);
	  holder.iv_sharePhoto = (ImageView)convertView.findViewById(R.id.iv_sharePhoto);
	  holder.tv_userInfoObj = (TextView)convertView.findViewById(R.id.tv_userInfoObj);
	  holder.tv_shareTime = (TextView)convertView.findViewById(R.id.tv_shareTime);
	  /*设置各个控件的展示内容*/
	  holder.tv_sharePhotoId.setText("分享id：" + mData.get(position).get("sharePhotoId").toString());
	  holder.tv_photoTitle.setText("图片标题：" + mData.get(position).get("photoTitle").toString());
	  holder.iv_sharePhoto.setImageResource(R.drawable.default_photo);
	  ImageLoadListener sharePhotoLoadListener = new ImageLoadListener(mListView,R.id.iv_sharePhoto);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("sharePhoto"),sharePhotoLoadListener);  
	  holder.tv_userInfoObj.setText("上传用户：" + (new UserInfoService()).GetUserInfo(mData.get(position).get("userInfoObj").toString()).getName());
	  holder.tv_shareTime.setText("分享时间：" + mData.get(position).get("shareTime").toString());
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_sharePhotoId;
    	TextView tv_photoTitle;
    	ImageView iv_sharePhoto;
    	TextView tv_userInfoObj;
    	TextView tv_shareTime;
    }
} 
