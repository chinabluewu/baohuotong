package com.demo.app.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.app.AppContext;
import com.demo.app.Data;
import com.demo.app.R;
import com.demo.app.adapter.ChargeHistoryListViewAdapter;
import com.demo.app.adapter.MainListViewAdapter;
import com.demo.app.bean.ChargeHistory;
import com.demo.app.bean.ChargeHistoryList;
import com.demo.app.bean.News;
import com.demo.app.bean.NewsList;
import com.demo.app.bean.URLs;
import com.demo.app.cascadingmenu.CityMainActivity;
import com.demo.app.common.UIHelper;
import com.demo.app.widget.MyListView;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by wu on 2016/3/1.
 */
public class ChargeHistoryActivity extends Activity implements View.OnClickListener {
    private ProgressDialog progressDialog_loading;
    private AppContext appContext;// 全局Context
    private List<ChargeHistory> historyList;
   // private ChargeHistoryListViewAdapter listViewAdapter;

    private ChargeHistoryListViewAdapter listViewAdapter;

    private MyListView listView;
   // private ListView listView;
    private TextView tv_chargeTotal;
    private Button bt_chargeBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_history);

        bt_chargeBack = (Button)findViewById(R.id.bt_chargeBack);
        tv_chargeTotal = (TextView)findViewById(R.id.tv_chargeTotal);
        bt_chargeBack.setOnClickListener(this);
        listView = (MyListView) findViewById(R.id.chargeHistory_listview);

        appContext = (AppContext) getApplication();
        // 网络连接判断
        if (!appContext.isNetworkConnected())
            UIHelper.ToastMessage(this, R.string.network_not_connected);
        //progressDialog_loading = ProgressDialog.show(this, null, "正在载入，请稍候...", true,
           //     false);

        http_post();


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_chargeBack:
                goToActivity(HomeActivity.class);
                break;
            default:
                break;
        }

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

    android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            progressDialog_loading.cancel();
            if (msg.what == 1) {
                historyList = (List<ChargeHistory>) msg.obj;
              //  listViewAdapter = new MainListViewAdapter(ChargeHistoryActivity.this,
               //         historyList);
                listViewAdapter = new ChargeHistoryListViewAdapter(ChargeHistoryActivity.this,historyList);

                // listViewAdapter = new MainListViewAdapter(ChargeHistoryActivity.this,
                 //       historyList);
                System.out.println("listViewAdapter为234："+listViewAdapter.toString());
                listView.setAdapter(listViewAdapter);
                tv_chargeTotal.setText(">>>充值总金额: "+msg.arg1 +"元<<<");
            } else if (msg.what == -1) {
                UIHelper.ToastMessage(ChargeHistoryActivity.this, "没有数据");
            } else if (msg.what == -2) {
                UIHelper.ToastMessage(ChargeHistoryActivity.this,
                        R.string.xml_parser_failed);
            }
        }
    };

    public void http_post() {
       // progressDialog_loading.show();
        progressDialog_loading = ProgressDialog.show(this, null, "正在载入，请稍候...", true,
                false);
        new Thread() {

            public void run() {
                ChargeHistoryList chargeHistoryList = new ChargeHistoryList();
                Message msg = new Message();
                HttpPost httpPost = new HttpPost(URLs.URL_FORDATA);
                JSONObject user = new JSONObject();
                try {
                    user.put("Act", "rList");
                    user.put("PSize", "10");  //每次传输记录的数量
                    user.put("PNo", "1");
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
                    String count = result.getString("count");
                    System.out.println("count长度为："+count);

                    if(Integer.parseInt(count) == 0){
                        msg.what = -1;  //没有数据
                        mHandler.sendMessage(msg);
                        return;


                    }

                        msg.what = 1;

                        JSONArray list = result.getJSONArray("list");
                    System.out.println("list长度为："+list.length());

                      msg.arg1 =Integer.parseInt(result.getString("reCount"));
                        if(list.length() == 0){
                            msg.what = -1;  //没有数据
                            mHandler.sendMessage(msg);
                            return;

                        }

                   //

                    msg.obj = chargeHistoryList.parse(list).getChargeHistoryList(); //解析JSON数组成 LIST
                        // msg.obj = chargeHistoryList.parse(list);
                        // System.out.print("收到的JSONLIST为："+msg.obj.toString());
                        System.out.println("收到的JSONLIST为："+msg.obj.toString());
                   // System.out.println("收到的JSONLIST为："+ chargeHistoryList.parse(list).getChargeHistoryList().toArray());
                  //  msg.what = -1;  //没有数据


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

    public void goToActivity(Class<?> c){
        Intent intent = new Intent(this, c);
        //intent.putExtra("LoginActivity.sID",sID);
        startActivity(intent);
        this.finish();
    }

}
