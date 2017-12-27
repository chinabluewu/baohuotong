package com.demo.app.ui;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.demo.app.AppContext;
import com.demo.app.AppException;
import com.demo.app.Data;
import com.demo.app.R;
import com.demo.app.adapter.MainListViewAdapter;
import com.demo.app.bean.News;
import com.demo.app.bean.NewsList;
import com.demo.app.common.UIHelper;
import com.demo.app.widget.MyListView;

public class MainActivity extends Activity {
	private MyListView listview;
	private List<News> newsList;
	private AppContext appContext;// 全局Context
	private MainListViewAdapter listViewAdapter;
	private ProgressDialog selectorDialog;
	private TextView title;
	private Button bt_news, bt_jiben, bt_zonghe,  bt_back;

	private TableLayout tableLayout;
	public static String sID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		//从上一个UI传递过来的参数
		Intent intent = getIntent();
		 sID = intent.getStringExtra("LoginActivity.sID");
		System.out.println("session ID：" + sID);
        */

		sID = Data.getsID();

		appContext = (AppContext) getApplication();
		// 网络连接判断
		if (!appContext.isNetworkConnected())
			UIHelper.ToastMessage(this, R.string.network_not_connected);
		init();
		Data.setBillType(4);
		initData();
	}

	private void init() {
		title = (TextView)findViewById(R.id.main_head_title);
		bt_news = (Button) findViewById(R.id.bt_news);
		bt_jiben = (Button) findViewById(R.id.bt_jiben);
		bt_zonghe = (Button) findViewById(R.id.bt_zonghe);
		//bt_favour = (Button) findViewById(R.id.bt_favour);
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_news.setSelected(true);
		bt_news.setOnClickListener(onClick(bt_news));
		bt_jiben.setOnClickListener(onClick(bt_jiben));
		bt_zonghe.setOnClickListener(onClick(bt_zonghe));
		//bt_favour.setOnClickListener(onClick(bt_favour));
		bt_back.setOnClickListener(onClick(bt_back));

		//tableLayout = (TableLayout)findViewById(R.id.TableOne);
		//tableLayout.setOnClickListener(this);


		listview = (MyListView) findViewById(R.id.news_listview);
		selectorDialog = ProgressDialog.show(this, null, "正在加载，请稍候...", true,
				false);

	}
/*
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.TableOne:
				UIHelper.ToastMessage(MainActivity.this, "点击了TableOne");
				break;
			default:
				break;
		}

	}
*/

	private View.OnClickListener onClick(final Button btn) {
		return new View.OnClickListener() {
			public void onClick(View v) {

				bt_news.setSelected(false);
				bt_jiben.setSelected(false);
				bt_zonghe.setSelected(false);
				//bt_favour.setSelected(false);
				bt_back.setSelected(false);
				if (btn == bt_news) {
					bt_news.setSelected(true);
					title.setText("鲜活类货损险");
					Data.setBillType(4);
					initData();

				} else if (btn == bt_jiben) {
					bt_jiben.setSelected(true);
					title.setText("基本货损险");
					Data.setBillType(2);
					initData();

				} else if (btn == bt_zonghe) {
					bt_zonghe.setSelected(true);
					title.setText("综合货损险");
					Data.setBillType(3);
					initData();

				//} else if (btn == bt_favour) {
				//	bt_favour.setSelected(true);
				} else if (btn == bt_back) {
					bt_back.setSelected(true);
					goToActivity(HomeActivity.class);
				}
			}
		};
	}

	//按下返回键将返回到上一级页面
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)){
			//progressDialog_loading.cancel();
			goToActivity(HomeActivity.class);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			selectorDialog.cancel();
			if (msg.what == 1) {
				newsList = (List<News>) msg.obj;
				listViewAdapter = new MainListViewAdapter(MainActivity.this,
						newsList);
				System.out.println("listViewAdapter为235："+listViewAdapter.toString());
				listview.setAdapter(listViewAdapter);

			} else if (msg.what == -1) {
				UIHelper.ToastMessage(MainActivity.this, "没有数据");
			} else if (msg.what == -2) {
				UIHelper.ToastMessage(MainActivity.this,
						R.string.xml_parser_failed);
			}
		}
	};

	private void initData() {

		selectorDialog.show();
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				try {
					NewsList list = appContext.getNewsList();
					System.out.println("新闻长度wu：" + list.getNewsCount());
					if (list.getNewsCount() > 0) {
						msg.what = 1;
						msg.obj = list.getNewslist();
                        System.out.println("新闻111="+msg.obj);
						appContext.saveObject(list, "newslist_");
					} else {
						msg.what = -1;
					}
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -2;
					msg.obj = e;
				}
				mHandler.sendMessage(msg);
			}
		}.start();

	}


	public void goToActivity(Class<?> c){
		Intent intent = new Intent(this, c);
		//intent.putExtra("LoginActivity.sID",sID);
		startActivity(intent);
		this.finish();
	}
}
