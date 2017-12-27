package com.demo.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.demo.app.Data;
import com.demo.app.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import com.demo.app.ToastUtil;
import com.demo.app.bean.URLs;
import com.demo.app.update.UpdateManager;


/**
 * Created by wu on 2016/2/10.
 */


    public class LoginActivity extends Activity implements View.OnClickListener {


    private EditText edit_name;
    private EditText edit_pw;
    private TextView findPw;
    private TextView user_login;
    private TextView user_register;

    private ProgressDialog progressDialog_login;
    private TextView tvUpdate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // setContentView(R.layout.activity_home);
        initView();

        UpdateManager updateManager = new UpdateManager(this);
        updateManager.checkUpdate();
    }


    public void initView() {
        // this.view_back = findViewById(R.id.view_back);
        // this.view_back.setOnClickListener(this);

        // if(parentActivity == null){
        //    this.view_back.setVisibility(View.GONE);
        //}
        // this.view_sure = findViewById(R.id.view_sure);
        //this.view_sure.setOnClickListener(this);
        this.edit_name = (EditText) findViewById(R.id.edit_name);
        this.edit_pw = (EditText) findViewById(R.id.edit_pw);

        edit_name.setText(Data.getuserName());
        edit_pw.setText(Data.getUserPw());

         SharedPreferences msharedPreferences = getSharedPreferences("user",Activity.MODE_PRIVATE);

        edit_name.setText(msharedPreferences.getString("UserName",""));
        edit_pw.setText(msharedPreferences.getString("UserPw",""));


        this.findPw = (TextView) findViewById(R.id.findPw);
        this.findPw.setOnClickListener(this);
        this.findPw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

        //this.user_register = (TextView) findViewById(R.id.user_register);
        //this.user_register.setOnClickListener(this);
        //this.user_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

        this.user_login = (TextView) findViewById(R.id.user_login);
        this.user_login.setOnClickListener(this);

        this.tvUpdate = (TextView) findViewById(R.id.tv_Update);
        this.tvUpdate.setOnClickListener(this);

        user_register = (TextView) findViewById(R.id.register);
        user_register.setOnClickListener(this);

    }

    public void initData() {

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
               /* case R.id.view_back:
               //     LoginActivity.this.finish();
                //    break;

                case R.id.findPw:
                    //忘记密码
                    Intent intent = new Intent(LoginActivity.this, FindMiMaActivity.class);
                    startActivity(intent);
                    break;
               */
           case R.id.register:  //用户注册
                    goToActivity(RegisterActivity.class);
                    break;

            case R.id.tv_Update:
                UpdateManager updateManager = new UpdateManager(this);
                updateManager.checkUpdate();

                break;
            case R.id.user_login:

                //登录
                progressDialog_login = ProgressDialog.show(this, null, "正在登录，请稍候...", true,
                        false);
                login();
                break;
        }
    }

    public void login() {
        ImageView imageView;
        imageView = (ImageView)findViewById(R.id.iv_loading);
        String name = this.edit_name.getText().toString();
        String pw = this.edit_pw.getText().toString();


        if (name.length() == 0 || pw.length() == 0) {
            progressDialog_login.cancel();
            ToastUtil.makeToastDuttom(this, getString(R.string.msg_errorFormNull));
            return;
        }
        if (pw.length() < 6) {
            progressDialog_login.cancel();
            ToastUtil.makeToast(this, getString(R.string.reg_pw_errorsize));
            return;
        }
           /*
            //开始联网登录
            final CallbackSuccess callbackSuccess = new CallbackSuccess(){
                @Override
                public void doCallback(MsgCarrier msgCarrier) {
                    // TODO Auto-generated method stub
                    MsgLoginSuccess mLoginSuccess = (MsgLoginSuccess) msgCarrier;
                    //保存登录状态
                    ValueUtil.getSystemSetting().loginSuccess(mLoginSuccess.getVloginSuccess());

                }
            };
            */

        //imageView.setVisibility(View.VISIBLE);
        final String baseURL = URLs.URL_FORUSER;

        String Act = "login";

        // List<String[]> pairList = parms_login(name,pw);
        JSONObject user = new JSONObject();
        try {
            user.put("Act", "login");
            user.put("UserName", name);
            user.put("Pwd", pw);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Data.setUserName(name);
        Data.setUserPw(pw);

        UserPostThread userThread = new UserPostThread(user, baseURL);
        userThread.start();


      // String resultCode = userThread.getResultCode();
       // String sessionID = userThread.getSessinID();
       // ToastUtil.makeToast(this, "result =  "+ resultCode );
       // ToastUtil.makeToast(this, "sessionID = "+ sessionID );


        /*
       switch (Integer.parseInt(resultCode) ){
           case 1:
               ToastUtil.makeToast(this, "密码错误，请重新输入！ " );
               break;
           case 2:
               ToastUtil.makeToast(this, "用户账号不存在！ " );
               break;
           case 3:
               ToastUtil.makeToast(this, "账号已停用！ " );
               break;
           case 4:
               ToastUtil.makeToast(this, "账号已过期！ " );
               break;
           case 5:
               ToastUtil.makeToast(this, "账号已关闭！ " );
               break;
           default:
               ToastUtil.makeToast(this, "开始登录！ " );
               break;

       }
      */
    }

    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            progressDialog_login.cancel();
             SharedPreferences sharedPreferences = getSharedPreferences("user",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch(msg.what) {
                case -1:
                    ToastUtil.makeToast(getApplication(), "无法连接服务器！ ");
                    break;
                case 1:
                    //ToastUtil.makeToast(this, "密码错误，请重新输入！ " );
                    Toast.makeText(getApplication(), "密码错误，请重新输入！ ", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    ToastUtil.makeToast(getApplication(), "用户账号不存在！ " );
                    break;
                case 3:
                    ToastUtil.makeToast(getApplication(), "账号已停用！ " );
                    break;
                case 4:
                    ToastUtil.makeToast(getApplication(), "账号已过期！ " );
                    break;
                case 5:
                    ToastUtil.makeToast(getApplication(), "账号已关闭！ " );
                    break;
                default:
                    httpGUserInfo();
                    ToastUtil.makeToast(getApplication(), "登录成功！ ");
                    editor.putString("UserName",edit_name.getText().toString());
                    editor.putString("UserPw",edit_pw.getText().toString());
                    editor.commit();
                    //listmain(msg.obj + "");

                    goToActivity(HomeActivity.class);
                    break;

            }
        }
    };

    /*
            public void listmain (String sid) {
                JSONObject user = new JSONObject();
                try {
                    user.put("Act", "oList");
                    user.put("PSize", "10");
                    user.put("PNo", "1");
                    user.put("STime", "");
                    user.put("ETime", "");

                    user.put("SId", sid);
                    user.put("Type", "2");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                UserPostThread userPostThread = new UserPostThread(user, URLs.URL_FORDATA);
                //UserPostThread userPostThread = new UserPostThread(user, URLs.URL_FORUSER);
                userPostThread.start();

            }
    */
    /**
     * 显示响应结果到命令行和TextView
     * @param response
     */

    private void showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            //ToastUtil.makeToast(this, "Response Content from server: " + "noreturn");
            return;
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;

            }

            System.out.println("Response Content from server: "+ result);
            //mResult.setText("Response Content from server: " + result);
            ToastUtil.makeToast(this, "Response Content from server: " + result);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void goToActivity(Class<?> c){
            Intent intent = new Intent(this, c);
            //intent.putExtra("LoginActivity.sID",sID);

           // Bundle bundle = new Bundle(); //　　Bundle的底层是一个HashMap<String, Object
           // bundle.putString("hello", "world");
           // intent.putExtra("bundle", bundle);

            startActivity(intent);
            this.finish();
        }

    //获取用户信息 (业务处理)
    public void httpGUserInfo(){

        new Thread(){

          public void run() {
            //  Message msg = new Message();
              HttpPost httpPost = new HttpPost(URLs.URL_FORUSER);
              JSONObject user = new JSONObject();

              try {
                  user.put("Act", "gUserInfo");
                  user.put("SId", Data.getsID()); //sessionID
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
                  JSONArray info = result.getJSONArray("info");
                 // System.out.println("收到的个人信息为："+info.toString());

                  JSONObject info0 = info.getJSONObject(0);

                  String RealName = info0.getString("RealName");
                  String SNo = info0.getString("SNo");
                  String Mobile = info0.getString("Mobile");
                  String Addr = info0.getString("Addr");
                  String Point = info0.getString("Point");  //帐户余额，单位为分

                  Data.setRealName(RealName);
                  Data.setSNo(SNo);
                  Data.setMobile(Mobile);
                  Data.setAddr(Addr);
                  Data.setPoint(Point);

                  System.out.println("收到的个人信息为：" + RealName + SNo + Mobile + Addr + Point);

              }catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
              } catch (JSONException e) {
                  e.printStackTrace();
              } catch (ClientProtocolException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              }
            //  msg.what = 6;   //要大于5
            //  mHandler.sendMessage(msg);
          }
          }.start();


    }



    public class UserPostThread extends Thread {
        private String baseURL;
        //private List<NameValuePair> pairList;
        private JSONObject user;
        public String resultCode ;
        private  String sessinID ;



        public  UserPostThread (JSONObject user,String baseURL){
            //this.pairList = pairList;
            this.user = user;
            this.baseURL = baseURL;

        }

        public void run (){
              Message msg = new Message();
            try {
                //HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
                //       pairList, HTTP.UTF_8);

                // URL使用基本URL即可，其中不需要加参数


                HttpPost httpPost = new HttpPost(baseURL);

                // 将请求体内容加入请求中
               // httpPost.setEntity(new StringEntity(user.toString()));

                httpPost.setEntity(new StringEntity(user.toString(),"utf-8")); //解决发送到服务器乱码的问题


                // httpPost.setEntity(requestHttpEntity);

                /*
                BasicHttpParams httpParameters = new BasicHttpParams();
                //设置请求超时
                int timeoutConnection = 3 * 1000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,timeoutConnection);
                //设置响应超时
                int timeoutSocket = 5 * 1000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                 */

                // 需要客户端对象来发送请求
                HttpClient httpClient = new DefaultHttpClient();

                //请求超时
                httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
                //读取超时

                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);


                // 发送请求
                HttpResponse response = httpClient.execute(httpPost);

                int re = response.getStatusLine().getStatusCode();


                // 显示响应
                // showResponseResult(response);
                System.out.println("服务器Response Content from server: "+ re);
                // System.out.println("Response Content from server: "+ response.toString());

                String retSrc = EntityUtils.toString(response.getEntity());
               // System.out.println("解析数据00：" + retSrc);
                try {
                    // 生成 JSON 对象，并截取其中的result和info信息
                    JSONObject result = new JSONObject(retSrc);
                    resultCode = result.getString("result");
                    sessinID = result.getString("info");
                    Data.setsID(sessinID);   //保存用户ID

                    msg.what = Integer.parseInt(resultCode);

                  //  JSONArray DataList  = result.getJSONArray("list");


                    System.out.println("server result =  " + resultCode);
                    System.out.println("server info =  " + sessinID);
                    //System.out.println("server list =  " + DataList.toString());

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                /*
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("resultCode",resultCode);
                b.putString("sessionID", sessinID);
                // msg.sendToTarget();
                msg.setData(b);
                */
                // int a = Integer.parseInt(resultCode);
                 //int b = Integer.parseInt(sessinID);
               // mHandler.obtainMessage(a,0,0,sessinID).sendToTarget();


               // mHandler.obtainMessage(Integer.parseInt(resultCode),0,0,sessinID).sendToTarget();


                //mHandler.obtainMessage(MSG_SUCCESS,bm).sendToTarget();


            /* 显示返回的json数据
            HttpEntity httpEntity = response.getEntity();
            if (null == response)
            {
                //ToastUtil.makeToast(this, "Response Content from server: " + "noreturn");
                return;
            }
            try {
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream));
                String result = "";
                String line = "";
                    while (null != (line = reader.readLine())) {
                        result += line;
                        //if(line == "}") return;

                    }

                System.out.println("Response Content from server: " + result);
                }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                */

            } catch (Exception e) {
                //ToastUtil.makeToast(this, "server exception: ");
                msg.what = -1;
                System.out.println("server 无连接 ");
                e.printStackTrace();
            }

            mHandler.sendMessage(msg);


        }

    }




}





