package com.mobileclient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("手机客户端-主界面");
        setContentView(R.layout.main_menu);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        
        AnimationSet set = new AnimationSet(false);
        Animation animation = new AlphaAnimation(0,1);
        animation.setDuration(500);
        set.addAnimation(animation);
        
        animation = new TranslateAnimation(1, 13, 10, 50);
        animation.setDuration(300);
        set.addAnimation(animation);
        
        animation = new RotateAnimation(30,10);
        animation.setDuration(300);
        set.addAnimation(animation);
        
        animation = new ScaleAnimation(5,0,2,0);
        animation.setDuration(300);
        set.addAnimation(animation);
        
        LayoutAnimationController controller = new LayoutAnimationController(set, 1);
        
        gridview.setLayoutAnimation(controller);
        
        gridview.setAdapter(new ImageAdapter(this));
    }
    
    // 继承BaseAdapter
    public class ImageAdapter extends BaseAdapter {
    	
    	LayoutInflater inflater;
    	
    	// 上下文
        private Context mContext;
        
        // 图片资源数组
        private Integer[] mThumbIds = {
                R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon
        };
        private String[] menuString = {"新闻分类管理","新闻信息管理","用户信息管理","新闻标记管理","新闻评论管理","新闻收藏管理","新闻赞管理","图片分享管理","图片评论管理","视频分享管理","活动信息管理"};

        // 构造方法
        public ImageAdapter(Context c) {
            mContext = c;
            inflater = LayoutInflater.from(mContext);
        }
        // 组件个数
        public int getCount() {
            return mThumbIds.length;
        }
        // 当前组件
        public Object getItem(int position) {
            return null;
        }
        // 当前组件id
        public long getItemId(int position) {
            return 0;
        }
        // 获得当前视图
        public View getView(int position, View convertView, ViewGroup parent) { 
        	View view = inflater.inflate(R.layout.gv_item, null);
			TextView tv = (TextView) view.findViewById(R.id.gv_item_appname);
			ImageView iv = (ImageView) view.findViewById(R.id.gv_item_icon);  
			tv.setText(menuString[position]); 
			iv.setImageResource(mThumbIds[position]); 
			  switch (position) {
				case 0:
					// 新闻分类管理监听器
					view.setOnClickListener(newsClassLinstener);
					break;
				case 1:
					// 新闻信息管理监听器
					view.setOnClickListener(newsLinstener);
					break;
				case 2:
					// 用户信息管理监听器
					view.setOnClickListener(userInfoLinstener);
					break;
				case 3:
					// 新闻标记管理监听器
					view.setOnClickListener(newsTagLinstener);
					break;
				case 4:
					// 新闻评论管理监听器
					view.setOnClickListener(newsCommentLinstener);
					break;
				case 5:
					// 新闻收藏管理监听器
					view.setOnClickListener(newsCollectionLinstener);
					break;
				case 6:
					// 新闻赞管理监听器
					view.setOnClickListener(zambiaLinstener);
					break;
				case 7:
					// 图片分享管理监听器
					view.setOnClickListener(photoShareLinstener);
					break;
				case 8:
					// 图片评论管理监听器
					view.setOnClickListener(photoCommentLinstener);
					break;
				case 9:
					// 视频分享管理监听器
					view.setOnClickListener(videoShareLinstener);
					break;
				case 10:
					// 活动信息管理监听器
					view.setOnClickListener(huodongLinstener);
					break;

			 
				default:
					break;
				} 
			return view; 
        }
       
    }
    
    OnClickListener newsClassLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动新闻分类管理Activity
			intent.setClass(MainMenuActivity.this, NewsClassListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener newsLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动新闻信息管理Activity
			intent.setClass(MainMenuActivity.this, NewsListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener userInfoLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动用户信息管理Activity
			intent.setClass(MainMenuActivity.this, UserInfoListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener newsTagLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动新闻标记管理Activity
			intent.setClass(MainMenuActivity.this, NewsTagListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener newsCommentLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动新闻评论管理Activity
			intent.setClass(MainMenuActivity.this, NewsCommentListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener newsCollectionLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动新闻收藏管理Activity
			intent.setClass(MainMenuActivity.this, NewsCollectionListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener zambiaLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动新闻赞管理Activity
			intent.setClass(MainMenuActivity.this, ZambiaListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener photoShareLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动图片分享管理Activity
			intent.setClass(MainMenuActivity.this, PhotoShareListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener photoCommentLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动图片评论管理Activity
			intent.setClass(MainMenuActivity.this, PhotoCommentListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener videoShareLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动视频分享管理Activity
			intent.setClass(MainMenuActivity.this, VideoShareListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener huodongLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// 启动活动信息管理Activity
			intent.setClass(MainMenuActivity.this, HuodongListActivity.class);
			startActivity(intent);
		}
	};


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "重新登入");  
		menu.add(0, 2, 2, "退出"); 
		return super.onCreateOptionsMenu(menu); 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {//重新登入 
			Intent intent = new Intent();
			intent.setClass(MainMenuActivity.this,
					LoginActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == 2) {//退出
			System.exit(0);  
		} 
		return true; 
	}
}
