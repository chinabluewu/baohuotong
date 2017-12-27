package com.demo.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.app.Data;
import com.demo.app.R;
import com.demo.app.ToastUtil;
import com.demo.app.bean.URLs;
import com.demo.app.cascadingmenu.CityMainActivity;
import com.demo.app.common.UIHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by wu on 2016/5/25.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText zcyhm,zcmm,zcmm2,zcxm,zcdh,zclxdz,zcsfzh;
    private TextView zccs,tvzccs,zcback;

    private Button btnTj;
    private String zccsCode;

    private ProgressDialog progressDialog_zctj;

    public final static int ZCCSRESULT=5;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ZCCSRESULT && resultCode == RESULT_OK){
            zccs.setText(data.getStringExtra(CityMainActivity.KEY_CITY_NAME));
            zccsCode = data.getStringExtra(CityMainActivity.KEY_CITY_CODE);
            // ToastUtil.makeToast(this,"获取的地区编码："+cfdCode);
            //tvCfdCode.setText(data.getStringExtra(CityMainActivity.KEY_CITY_CODE));

            //ToastUtil.makeToast(this, "收到出发地信息:" + data.getStringExtra(PickerMainActivity.KEY_CITY_CODE));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }


    public void initView() {
       zcyhm = (EditText)findViewById(R.id.et_zcyhm);
       zcmm = (EditText)findViewById(R.id.et_zcmm);

        zcmm2 = (EditText)findViewById(R.id.et_zcmm2);
        zcxm = (EditText)findViewById(R.id.et_zcxm);
        zcsfzh = (EditText)findViewById(R.id.et_zcsfzh);

        zcdh = (EditText)findViewById(R.id.et_zcdh);
        zccs = (TextView)findViewById(R.id.et_zccs);
        tvzccs = (TextView)findViewById(R.id.tv_zccs);

        zcback = (TextView)findViewById(R.id.tv_zc_back);

        zclxdz = (EditText)findViewById(R.id.et_zclxdz);

        btnTj = (Button)findViewById(R.id.btn_zctj);

        tvzccs.setOnClickListener(this);
        btnTj.setOnClickListener(this);
        zcback.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_zccs:

                System.out.println("载入城市选择");
                startActivityForResult(new Intent(this, CityMainActivity.class), ZCCSRESULT);

                break;
            case R.id.tv_zc_back:
                goToActivity(LoginActivity.class);
                break;

            case R.id.btn_zctj:

                 if ((zcmm.getText().toString().length()<6)||(zcmm.getText().toString().length()>8)){

                     ToastUtil.makeToast(this,"密码应控制在6~8位，请检查。");
                     break;
                 } else if(!zcmm.getText().toString().equals(zcmm2.getText().toString())){
                    ToastUtil.makeToast(this,"两次密码不一致，请检查。");
                    break;
                }

                progressDialog_zctj = ProgressDialog.show(this, null, "正在提交，请稍候...", true,
                        false);

                RegisterSubmit();

                break;

            default:
                break;


        }

    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            progressDialog_zctj.cancel();

            UIHelper.ToastMessage(RegisterActivity.this, msg.obj + "", Toast.LENGTH_LONG);


            System.out.println("msg.what= :" +msg.what);

            if (msg.what == 1) {
                // editor.putString()
                goToActivity(LoginActivity.class); //注册成功，则跳转到。

            } else if (msg.what == -1) {

                UIHelper.ToastMessage(RegisterActivity.this, "网络无法连接！");
                //ToastUtil.makeToast(AddActivity.this,"没有数据");
            } else if (msg.what == -2) {
                UIHelper.ToastMessage(RegisterActivity.this,
                        "数据解析异常");
            }
        }
    };



    private void RegisterSubmit(){

      final   JSONObject user = new JSONObject();
        try {
            user.put("Act", "reg");


            user.put("UserName", zcyhm.getText().toString());  //
            user.put("Pwd", zcmm2.getText().toString());   //

            user.put("CityCode", zccsCode); // 城市编码
            user.put("CityName", zccs.getText().toString()); //需要加 “，”

            user.put("Mobile", zcdh.getText().toString()); //手机
            user.put("RealName", zcxm.getText().toString());
            user.put("SNo", zcsfzh.getText().toString()); //身份证号

            user.put("Address", zclxdz.getText().toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(){
            public void run(){

                Message msg = new Message();
                HttpPost httpPost = new HttpPost(URLs.URL_FORUSER);

                // 将请求体内容加入请求中
                try {

                    // HttpEntity requestHttpEntity = new UrlEncodedFormEntity(user, HTTP.UTF_8);
                    //httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    httpPost.setEntity(new StringEntity(user.toString(),"utf-8")); //解决发送到服务器乱码的问题

                    // httpPost.setEntity(requestHttpEntity);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // httpPost.setEntity(requestHttpEntity);

                // 需要客户端对象来发送请求
                HttpClient httpClient = new DefaultHttpClient();

                // 发送请求
                HttpResponse response = null;
                try {
                    response = httpClient.execute(httpPost);

                    System.out.println("注册 response:"+response.toString());

                } catch (IOException e) {
                    msg.what = - 1;
                    e.printStackTrace();
                }

                //int re = response.getStatusLine().getStatusCode();
                String retSrc = null;
                try {
                    retSrc = EntityUtils.toString(response.getEntity());
                    System.out.println("注册retSrc:"+retSrc);

                } catch (IOException e) {
                    msg.what = - 2;
                    e.printStackTrace();
                }
                JSONObject result = null;
                try {
                    result = new JSONObject(retSrc);
                    String resultFromWeb = result.getString("result");
                    System.out.println("resultFromWeb:"+resultFromWeb);
                   if(resultFromWeb.equals("0")){
                        msg.what = 1;
                        System.out.println("msg.what1 = :" +msg.what);
                    }

                    String info = result.getString("info");
                    msg.obj = info;
                } catch (JSONException e) {
                    msg.what = - 2;
                    e.printStackTrace();
                }
                mHandler.sendMessage(msg);

            }

        }.start();

    }



    //按下返回键将返回到上一级页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)){
            //progressDialog_loading.cancel();
            goToActivity(LoginActivity.class);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    public void goToActivity(Class<?> c){
        Intent intent = new Intent(this, c);
        //intent.putExtra("LoginActivity.sID",sID);
        startActivity(intent);
        this.finish();
    }


}
