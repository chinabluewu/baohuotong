package com.demo.app.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.app.AppContext;
import com.demo.app.Data;
import com.demo.app.R;
import com.demo.app.bean.News;
import com.demo.app.bean.URLs;
import com.demo.app.common.BitmapManager;
import com.demo.app.common.StringUtils;
import com.demo.app.common.UIHelper;
import com.demo.app.ui.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainListViewAdapter extends BaseAdapter {
	LayoutInflater inflater;
	// 定义Context
	private Context mContext;
	private ViewHolder holder;
	private int clickTemp = -1;
	List<News> list = new ArrayList<News>();
	private BitmapManager bmpManager;
	private ProgressDialog progressDialog_loading;

	String PdfNo = null;
	String forInsuState = null;
	String forCompany = null;


	private int progress;
	private ProgressBar mProgress;

	private boolean cancelUpdate = false;
	private Dialog mDownloadDialog;


	public MainListViewAdapter(Activity activity, List<News> listViewList) {
		mContext = activity;
		inflater = activity.getLayoutInflater();
		list = listViewList;
		//this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(
			//	activity.getResources(), R.drawable.umeng_socialize_share_pic));
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public void setSeclection(int position) {
		clickTemp = position;
	}

	private class ViewHolder {
		TextView companyName; //保险公司名
		TextView name; //被保人 add by wu
		TextView time;

		TextView smallNo; //保单号
		TextView truckNo; //车牌号 add by wu
		TextView state;  //状态
		TextView detail;
		ImageView img;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_item, null);
			holder.companyName = (TextView) convertView
					.findViewById(R.id.textview_home_listview_CompanyName);

			holder.name = (TextView) convertView
					.findViewById(R.id.textview_home_listview_name);  //add by wu

			holder.time = (TextView) convertView
					.findViewById(R.id.textview_home_listview_time);

			holder.smallNo = (TextView) convertView
					.findViewById(R.id.textview_home_listview_SmallNo);

			holder.truckNo = (TextView) convertView
					.findViewById(R.id.textview_home_listview_TruckNo);  //add by wu

			holder.state = (TextView) convertView
					.findViewById(R.id.textview_home_listview_state);

			//holder.detail = (TextView)convertView.findViewById(R.id.textview_home_listview_detail);

			//holder.img = (ImageView) convertView
			//		.findViewById(R.id.imageview_home_listview_thumb);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.companyName.setText(list.get(position).getCompandName()+ "保险");
		holder.smallNo.setText("保单号:" +list.get(position).getSmallNo()); //wu add

		holder.name.setText(list.get(position).getName());
		holder.truckNo.setText("车牌号:" +list.get(position).getTruckNo()); //wu add

		holder.state.setText("状态:" + list.get(position).getState());
		holder.time.setText(list.get(position).getPublishTime());

       // holder.detail.setText(list.get(position).getDetail());  //wu add
		/*
		String imgURL = list.get(position).getFirstPicUrl();
		if (imgURL.endsWith("portrait.gif") || StringUtils.isEmpty(imgURL)) {
			holder.img.setImageResource(R.drawable.umeng_socialize_share_pic);
		} else {
			if (!imgURL.contains("http")) {
				imgURL = URLs.HTTP + URLs.HOST + "/" + imgURL;
			}
			bmpManager.loadBitmap(imgURL, holder.img);
		}
		*/
		holder.smallNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				System.out.println("点击了TableView");
				showInfo(position);

			}
		});

		holder.truckNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				System.out.println("点击了TableView");
				showInfo(position);

			}
		});

		holder.time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				System.out.println("点击了TableView");
				showInfo(position);

			}
		});


		return convertView;
	}

	public void showInfo(int position){

		//ImageView img=new ImageView(ListViewActivity.this);
		//img.setImageResource(R.drawable.b);

/*
		new AlertDialog.Builder(mContext)
				.setTitle("保单详情" + position)
				//.setMessage("投保人：" + title[position] + "   手机号:" + info[position])
				.setMessage("投保人：tom" + "   手机号:13349880713" +"保单号:"+ list.get(position).getSmallNo())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();
*/
		progressDialog_loading = ProgressDialog.show(mContext, null, "正在载入，请稍候...", true,
				false);
		http_post(position);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

/*
{"InsuNo":"43026","CompanyId":"1","Insured":"tom","InsuredSNo":"","InsuredTel":"13349881234",
"InsuredMobile":"13349881234","TruckNo":"eamt540","TruckNoG":"eamt540","FromAddress":"天津市天津市河西区",
"ToAddress":"内蒙古大同市回民区","BillType":"2","PackType":"2","GoodsName":"apple","GoodsAmount":"2箱",
"GoodsWorth":"CNY 20000","BeginTime":"2016-2-29 06","EndTime":"2016-02-21 06","AddTime":"2016/2/29 14:09:29"}
 */



	Handler mHandler = new android.os.Handler() {
		public void handleMessage(Message msg) {
			progressDialog_loading.cancel();

			String InsuNo=null,InsuSmallNo=null, CompanyId=null,Insured=null,InsuredTel=null,InsuredMobile=null,TruckNo=null,
					TruckNoG=null,FromAddress=null, ToAddress=null,BillType=null,PackType=null,GoodsName=null,
					GoodsAmount=null,GoodsWorth=null,BeginTime=null,EndTime=null,AddTime =null,InsuState=null;


			if (msg.what == 1) {

				JSONObject info = new JSONObject();
				info = (JSONObject)msg.obj;
				try {
					 InsuNo = info.getString("InsuNo");
					InsuSmallNo = info.getString("InsuSmallNo"); //KM开头的查询单号
					PdfNo = InsuSmallNo;

					CompanyId = info.getString("CompanyId");
					if(CompanyId.equals("1")){
						CompanyId = "平安保险";
					}else if(CompanyId.equals("4")){
						CompanyId = "人保保险";
					}else if(CompanyId.equals("5")){
						CompanyId = "大地保险";
					}


					 Insured = info.getString("Insured");
					 InsuredTel = info.getString("InsuredTel");

					// InsuredMobile = info.getString("InsuredMobile"); //
					 TruckNo = info.getString("TruckNo");
					 TruckNoG = info.getString("TruckNoG");
					 FromAddress = info.getString("FromAddress");

					 ToAddress = info.getString("ToAddress");
					 BillType = info.getString("BillType");  //
					 PackType = info.getString("PackType");  //
					 GoodsName = info.getString("GoodsName");

					 GoodsAmount = info.getString("GoodsAmount");
					 GoodsWorth = info.getString("GoodsWorth");
					 BeginTime = info.getString("BeginTime");
					 EndTime = info.getString("EndTime");

					 AddTime = info.getString("AddTime");
					InsuState = info.getString("InsuState");
                    forInsuState = InsuState;
					forCompany = CompanyId;
				} catch (JSONException e) {
					e.printStackTrace();
				}


				AlertDialog alertDialog = new AlertDialog.Builder(mContext)
						.setTitle("保单详情")
								//.setMessage("投保人：" + title[position] + "   手机号:" + info[position])
						.setMessage(
								"保单号: " + InsuNo
										+ "\n公司名: " + CompanyId
										+ "\n手机号: " + InsuredTel
										+ "\n车牌号: " + TruckNo + " " + TruckNoG

										+ "\n出发地: " + FromAddress
										+ "\n目的地: " + ToAddress
										+ "\n被保险人: " + Insured
										+ "\n货物名称: " + GoodsName

										+ "\n货物数量: " + GoodsAmount
										+ "\n货物价值: " + GoodsWorth + "元"
										+ "\n启运时间: " + BeginTime + "时"

										+ "\n到达时间: " + EndTime + "时"
										+ "\n投保时间: " + AddTime
										+ "\n保单状态: " + (InsuState.equals("2")? "成功" :"审核中"))
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						})


							.setNegativeButton("下载保单", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// final String PdfNo = InsuSmallNo;
									if (!forCompany.equals("平安保险")) {

										if (forInsuState.equals("2")) {
											showDownloadDialog();
										} else {
											UIHelper.ToastMessage(mContext, "只有审核成功的保单才能下载");
										}
									 }else {
											UIHelper.ToastMessage(mContext, "平安的保单暂时不提供下载");
										}
								}
							})

						.show();




			} else if (msg.what == 2) {
				// 设置进度条位置
				mProgress.setProgress(progress);
			}else if (msg.what == 3) {
				UIHelper.ToastMessage(mContext,"保单已成功下载到"+Environment.getExternalStorageDirectory() + "/download"+ "目录下，正在打开。。", Toast.LENGTH_LONG);


			}else if (msg.what == -1) {
				UIHelper.ToastMessage(mContext, "没有数据");
			} else if (msg.what == -2) {
				UIHelper.ToastMessage(mContext, "解析出错");
			}
		}
	};



	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("开始下载保单");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		// mProgress = (SeekBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		http_getpdf(PdfNo);



	}


	private void http_getpdf(String pdfNo) {
		final String urlStr = URLs.URL_DOWNLOAD_PDF+ "?BillNo=" +pdfNo ;
		System.out.println("保单下载为：" + urlStr);

			new Thread() {

			public void run() {

				String mSavePath = null ;
				// System.out.println("开始下载APK");
				try
				{
					// 判断SD卡是否存在，并且是否具有读写权限
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
					{
						// 获得存储卡的路径
						String sdpath = Environment.getExternalStorageDirectory() + "/";
						mSavePath = sdpath + "download";
						//URL url = new URL(mHashMap.get("url"));
						URL url = new URL(urlStr); //保单 pdf  下载地址
						// 创建连接
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.connect();
						//System.out.println("连上服务器");

						// 获取文件大小
						int length = conn.getContentLength();
						// 创建输入流
						InputStream is = conn.getInputStream();

						File file = new File(mSavePath);
						// 判断文件目录是否存在
						if (!file.exists())
						{
							file.mkdir();
						}
						// File apkFile = new File(mSavePath, mHashMap.get("name"));
						File PdfFile = new File(mSavePath,PdfNo+ ".pdf");

						FileOutputStream fos = new FileOutputStream(PdfFile);

						int count = 0;
						// 缓存
						byte buf[] = new byte[1024];
						// 写入到文件中
						do
						{
							int numread = is.read(buf);
							count += numread;
							// 计算进度条位置
							progress = (int) (((float) count / length) * 100);
							// 更新进度
							mHandler.sendEmptyMessage(2);
							//if (numread <= 0)
							if (numread == -1)
							{
								// 下载完成
								mHandler.sendEmptyMessage(3);
								break;
							}
							// 写入文件
							fos.write(buf, 0, numread);

							// System.out.println("buffer值= "+numread);
						} while (!cancelUpdate);// 点击取消就停止下载.

						fos.flush();
						fos.close();
						is.close();

						//openFile(PdfFile);  //打开下载的PDF文件
					}
				} catch (MalformedURLException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}


				//UIHelper.ToastMessage(mContext,"下载的保单已存储到"+ ""+"/"+PdfNo+ ".pdf");

				//UIHelper.ToastMessage(this,"下载的保单已存储到");

				System.out.println("PDF文件打开中0：" );
				// 取消下载对话框显示
			  	mDownloadDialog.dismiss();

				System.out.println("PDF文件打开中0：");
				openFile(new File(Environment.getExternalStorageDirectory() + "/download", PdfNo + ".pdf"));  //打开下载的PDF文件
				System.out.println("PDF文件打开中：" );

			}

		}.start();


	}

	public void http_post(int position) {
		// progressDialog_loading.show();
        final int fposition = position;
		new Thread() {

			public void run() {

				Message msg = new Message();
				HttpPost httpPost = new HttpPost(URLs.URL_SUBMIT);
				JSONObject user = new JSONObject();
				try {
					user.put("Act", "detail");
					user.put("BigNo", list.get(fposition).getSmallNo());  //保单号
					user.put("SId", Data.getsID());

				} catch (JSONException e) {
					e.printStackTrace();
				}

				try

				{
					httpPost.setEntity(new StringEntity(user.toString(), "UTF-8"));
					HttpClient httpClient = new DefaultHttpClient();
					HttpResponse response = httpClient.execute(httpPost);
					String retSrc = EntityUtils.toString(response.getEntity());

					JSONObject result = new JSONObject(retSrc);

					String result2 = result.getString("result");
					System.out.println("返回的result为："+result2);

					msg.what = 1;

					JSONArray infoList = result.getJSONArray("info");
					JSONObject info = infoList.getJSONObject(0);
					System.out.println("收到的info为："+info.toString());
                    msg.obj = info;
					//System.out.println("list长度为："+list.length());

					if(infoList.length() == 0){
						msg.what = -1;  //没有数据
						mHandler.sendMessage(msg);
						return;

					}



				} catch (UnsupportedEncodingException e) {
					msg.what = -2;   //解析异常
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					msg.what = -2;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = -2;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = -2;
					e.printStackTrace();
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}


	//Android获取一个用于打开PDF文件的intent

	private void openFile(File file) {
		// TODO Auto-generated method stub
		//Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/pdf");

		System.out.println("PDF文件打开中。。。：");
		mContext.startActivity(intent);
	}

  /*
	//Android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent( String param ){

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}
  */


}
