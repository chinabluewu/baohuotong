package com.demo.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.demo.app.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wu on 2016/4/2.
 */
public class TruckNoActivity extends Activity implements View.OnClickListener{

   static String KEY_TRUCK_NO;

   // private EditText etTruckNoShow;
    private TextView truckNoShow,truckNoBack,truckNoDel,truckNoOK,showArea,showNum;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv0;
    private TextView tvA,tvB,tvC,tvD,tvE,tvF,tvG,tvH,tvJ,tvK,tvL,tvM,tvN,tvP,tvQ,tvR,tvS,tvT,tvU,tvV,tvW,tvX,tvY,tvZ;

    private TextView tvJing,tvJin,tvHu,tvYuCQ,tvYi,tvYu,tvYun,tvLiao,tvHei,tvXiang,tvWan,tvLu,tvXin,tvSu,tvZhe,tvGan,
                     tvEe,tvGui,tvGanGS,tvJinSX,tvMong,tvShan,tvJi,tvMin,tvGuiGZ,tvYue,tvQing,tvZang,tvChuan,tvNing,
                     tvQiong,tvGua;

    private TextView[] textViewsArea = new TextView[]{tvJing,tvJin,tvHu,tvYuCQ,tvYi,tvYu,tvYun,tvLiao,tvHei,tvXiang,
                                                    tvWan,tvLu,tvXin,tvSu,tvZhe,tvGan, tvEe,tvGui,tvGanGS,tvJinSX,
                                                    tvMong,tvShan,tvJi,tvMin,tvGuiGZ,tvYue,tvQing,tvZang,tvChuan,
                                                    tvNing, tvQiong,tvGua };


    int[] idArea = new int[]{ R.id.truckNoJing, R.id.truckNoJin, R.id.truckNoHu, R.id.truckNoYuCQ, R.id.truckNoYi,
                                R.id.truckNoYu, R.id.truckNoYun, R.id.truckNoLiao, R.id.truckNoHei, R.id.truckNoXiang,
                                R.id.truckNoWan, R.id.truckNoLu, R.id.truckNoXin, R.id.truckNoSu, R.id.truckNoZhe,
                                R.id.truckNoGan, R.id.truckNoEe, R.id.truckNoGui, R.id.truckNoGanGS, R.id.truckNoJinSX,
                                R.id.truckNoMong, R.id.truckNoShan, R.id.truckNoJi, R.id.truckNoMin, R.id.truckNoGuiGZ,
                                R.id.truckNoYue, R.id.truckNoQing, R.id.truckNoZang, R.id.truckNoChuan, R.id.truckNoNing,
                                R.id.truckNoQiong, R.id.truckNoGua };



    private TextView[] textViewsNum = new TextView[]{tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv0,
                                                 tvA,tvB,tvC,tvD,tvE,tvF,tvG,tvH,tvJ,tvK,tvL,tvM,tvN,
                                                 tvP,tvQ,tvR,tvS,tvT,tvU,tvV,tvW,tvX,tvY,tvZ};

    int[] idNum  = new int[]{ R.id.truckNo0, R.id.truckNo1, R.id.truckNo2, R.id.truckNo3,R.id.truckNo4,
                           R.id.truckNo5, R.id.truckNo6, R.id.truckNo7, R.id.truckNo8, R.id.truckNo9,
                            R.id.truckNoA, R.id.truckNoB,R.id.truckNoC, R.id.truckNoD, R.id.truckNoE,
                            R.id.truckNoF, R.id.truckNoG, R.id.truckNoH, R.id.truckNoJ, R.id.truckNoK,
                            R.id.truckNoL, R.id.truckNoM, R.id.truckNoN,R.id.truckNoP, R.id.truckNoQ,
                            R.id.truckNoR, R.id.truckNoS, R.id.truckNoT, R.id.truckNoU, R.id.truckNoV,
                            R.id.truckNoW, R.id.truckNoX, R.id.truckNoY, R.id.truckNoZ };


    private TableLayout TlArea,TlNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_no);
        showArea  = (TextView)findViewById(R.id.tvShowArea);
        showNum  = (TextView)findViewById(R.id.tvShowNum);

        TlArea  = (TableLayout)findViewById(R.id.TlArea);
        TlNum  = (TableLayout)findViewById(R.id.TlNum);

        truckNoBack = (TextView)findViewById(R.id.tvTruckNoBack);
        truckNoDel = (TextView)findViewById(R.id.tvTruckNoDel);
        truckNoOK = (TextView)findViewById(R.id.tvTruckNoOK);
        truckNoShow = (TextView)findViewById(R.id.tvTruckNoShow);

        showArea.setOnClickListener(this);
        showNum.setOnClickListener(this);
        truckNoBack.setOnClickListener(this);
        truckNoDel.setOnClickListener(this);
        truckNoOK.setOnClickListener(this);



        System.out.println("界面已经启动");

        //地区简称初始化
        for(int j =0;j<32;j++){
            textViewsArea[j] = (TextView)findViewById(idArea[j]);
            textViewsArea[j].setOnClickListener(this);
        }

        //字母数字初始化
        for(int i =0;i<34;i++){
             textViewsNum[i] = (TextView)findViewById(idNum[i]);
             textViewsNum[i].setOnClickListener(this);
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvTruckNoBack:
                this.finish();
                break;
            //回删
            case R.id.tvTruckNoDel:
                String strTemp = truckNoShow.getText().toString();
                if(!strTemp.isEmpty()) {
                    truckNoShow.setText(strTemp.substring(0, strTemp.length() - 1));
                  if(strTemp.length() == 1){
                      TlNum.setVisibility(View.GONE);
                      TlArea.setVisibility(View.VISIBLE);
                  }


                }else {
                    truckNoShow.setText(strTemp);

                }


                break;
            case R.id.tvTruckNoOK:
                Intent intent = new Intent();
                intent.putExtra(KEY_TRUCK_NO, truckNoShow.getText().toString());
                // intent.putExtra(KEY_TRUCK_NO, "12345");
                setResult(RESULT_OK, intent);
                finish();
                System.out.println("点击了OK按钮");
                break;
            case R.id.tvShowArea:
                TlNum.setVisibility(View.GONE);
                TlArea.setVisibility(View.VISIBLE);
                System.out.println("点击了地区按钮");
                break;
            case R.id.tvShowNum:

                TlArea.setVisibility(View.GONE);
                TlNum.setVisibility(View.VISIBLE);
                System.out.println("点击了数字按钮");
                break;

            default:
                //etTruckNoShow.setText(textViews[1].getText());
                TextView tvTemp = (TextView)findViewById(view.getId());
                //tvTemp.setBackgroundColor(getResources().getColor(R.color.red));
                truckNoShow.append(tvTemp.getText());

                Pattern p =Pattern.compile("[\u4e00-\u9fa5]");
                Matcher matcher = p.matcher(tvTemp.getText());
                if(matcher.matches()){
                    TlArea.setVisibility(View.GONE);
                    TlNum.setVisibility(View.VISIBLE);
                }

                System.out.println("点击了其他按钮");
                //tvTemp.setBackgroundColor(getResources().getColor(R.color.gold));
                break;


        }

    }
}
