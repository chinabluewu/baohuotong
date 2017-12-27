package com.demo.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.demo.app.Data;
import com.demo.app.R;

/**
 * Created by wu on 2016/6/3.
 */
public class UserInfoActivity extends Activity implements View.OnClickListener {

    private TextView tv_infoback;
    private TextView tv_infoUser;

    private TextView tv_infoName;
    private TextView tv_infoID;
    private TextView tv_infotel;
    private TextView tv_infoaddress;
    private TextView tv_infoMoney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        initview();
    }

    private void initview(){
        tv_infoback = (TextView)findViewById(R.id.tv_infoback);

        tv_infoback.setOnClickListener(this);


        tv_infoUser = (TextView)findViewById(R.id.tv_infoUser);
        tv_infoName = (TextView)findViewById(R.id.tv_infoName);
        tv_infoID = (TextView)findViewById(R.id.tv_infoID);

        tv_infotel = (TextView)findViewById(R.id.tv_infotel);
        tv_infoaddress = (TextView)findViewById(R.id.tv_infoaddress);
        tv_infoMoney = (TextView)findViewById(R.id.tv_infoMoney);


        tv_infoUser.setText(Data.getuserName());
        tv_infoName.setText(Data.getRealName());
        tv_infoID.setText(Data.getSNo());
        tv_infotel.setText(Data.getMobile());
        tv_infoaddress.setText(Data.getAddr());
        tv_infoMoney.setText(Data.getPoint()+ "元");


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_infoback:
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

    public void goToActivity(Class<?> c){
        Intent intent = new Intent(this, c);
        //intent.putExtra("LoginActivity.sID",sID);
        startActivity(intent);
        this.finish();
    }


}
