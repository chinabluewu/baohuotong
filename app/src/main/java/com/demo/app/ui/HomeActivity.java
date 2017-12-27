package com.demo.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.demo.app.Data;
import com.demo.app.R;

/**
 * Created by wu on 2016/2/12.
 */
public class HomeActivity extends Activity implements View.OnClickListener{
    private TextView tv_back;
    private TextView tv_user;

    private TextView tv_add;
    private TextView tv_manage;
    private TextView tv_history;
    private TextView tv_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

    }


    private void initView(){

        this.tv_back = (TextView)findViewById(R.id.tv_back);
        this.tv_back.setOnClickListener(this);

        this.tv_user = (TextView)findViewById(R.id.tv_user);
        this.tv_user.setOnClickListener(this);

        this.tv_add = (TextView)findViewById(R.id.tv_add);
        this.tv_add.setOnClickListener(this);

        this.tv_manage = (TextView)findViewById(R.id.tv_manage);
        this.tv_manage.setOnClickListener(this);

        this.tv_history = (TextView)findViewById(R.id.tv_history);
        this.tv_history.setOnClickListener(this);

        this.tv_list = (TextView)findViewById(R.id.tv_list);
        this.tv_list.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.tv_back:
                goToActivity(LoginActivity.class);
                break;
            case R.id.tv_user:
                System.out.println("点击了个人中心");
                //showUserInfo();
                goToActivity(UserInfoActivity.class);
                break;

            case R.id.tv_add:
                goToActivity(AddActivity.class);
                break;
            case R.id.tv_manage:
                goToActivity(MainActivity.class);
                break;
            case R.id.tv_history:
                goToActivity(ChargeHistoryActivity.class);
                break;
            case R.id.tv_list:
                break;
            default:
                break;


        }
    }


    public void showUserInfo(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("个人信息")
                .setMessage("真实姓名: " + Data.getRealName()
                        + "\n联系电话: " + Data.getMobile()
                        + "\n所在地址: " + Data.getAddr()
                        + "\n当前余额: " + Data.getPoint()+ "元")
                      //  + "\n当前余额: " + Integer.parseInt(Data.getPoint())/100)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();


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
