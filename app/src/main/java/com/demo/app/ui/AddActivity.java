package com.demo.app.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.demo.app.Data;
import com.demo.app.R;
import com.demo.app.ToastUtil;
import com.demo.app.adapter.MainListViewAdapter;
import com.demo.app.bean.News;
import com.demo.app.bean.URLs;

import com.demo.app.cascadingmenu.CascadingMenuFragment;
import com.demo.app.cascadingmenu.CascadingMenuPopWindow;
import com.demo.app.cascadingmenu.CityMainActivity;
import com.demo.app.cascadingmenu.DBhelper;
import com.demo.app.common.UIHelper;
import com.demo.app.cascadingmenu.interfaces.CascadingMenuViewOnSelectListener;
import com.demo.app.model.Area;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wu on 2016/2/13.
 */
public class AddActivity extends Activity implements View.OnClickListener{
    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView tv_add_back;

    private TextView tvCfd,tvMdd,tvCph;
    private EditText etCfd,etMdd,etBbrxm,etBbrsj,etHwmc,etHwsl,etHwje,etCph;
    private AutoCompleteTextView autoCtvCph,autoCtvBbr;
    private ArrayAdapter<String> arr_adapter;


    private TextView tvDate,tvDate2,tvHour,tvHour2;

    private Button btn_tj;
    private ProgressDialog progressDialog_tj;
    private Spinner spinnerBxgs,spinnerXz,spinnerBzfs,spinnerHwdw;

    private int mYear,mYear2;
    private int mMonth,mMonth2;
    private int mDay,mDay2;
    private int mHour,mHour2;
    private String cfdCode,mddCode;


    private static final int DATE1_DIALOG_ID = 1;
    private static final int DATE2_DIALOG_ID = 2;

    private static final int TIMER1_DIALOG_ID = 3;
    private static final int TIMER2_DIALOG_ID = 4;

    Calendar calendar0 = Calendar.getInstance();
     Calendar calendar = Calendar.getInstance(); //启运日期
    Calendar calendar2 = Calendar.getInstance(); //到达日期

    //使用SharedPreferences保存历史记录在history.xml 文件
   // SharedPreferences sharedPreferences = getSharedPreferences("history",Activity.MODE_PRIVATE);
   // SharedPreferences.Editor editor = sharedPreferences.edit();


    public final static int CFDRESULT=1;
    public final static int MDDRESULT=2;

    public final static int CPHRESULT=3;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CFDRESULT && resultCode == RESULT_OK){
            etCfd.setText(data.getStringExtra(CityMainActivity.KEY_CITY_NAME));
            cfdCode = data.getStringExtra(CityMainActivity.KEY_CITY_CODE);
           // ToastUtil.makeToast(this,"获取的地区编码："+cfdCode);
            //tvCfdCode.setText(data.getStringExtra(CityMainActivity.KEY_CITY_CODE));

            //ToastUtil.makeToast(this, "收到出发地信息:" + data.getStringExtra(PickerMainActivity.KEY_CITY_CODE));
        }
        else if(requestCode==MDDRESULT && resultCode == RESULT_OK){
            etMdd.setText(data.getStringExtra(CityMainActivity.KEY_CITY_NAME));
            mddCode = data.getStringExtra(CityMainActivity.KEY_CITY_CODE);
           // ToastUtil.makeToast(this,"获取的地区编码："+mddCode);
           // tvMddCode.setText(data.getStringExtra(CityMainActivity.KEY_CITY_CODE));
           // ToastUtil.makeToast(this,"收到目的地信息:"+PickerMainActivity.KEY_CITY_NAME);
        }else if(requestCode==CPHRESULT && resultCode == RESULT_OK){
           // etCph.setText(data.getStringExtra(TruckNoActivity.KEY_TRUCK_NO));
             autoCtvCph.setText(data.getStringExtra(TruckNoActivity.KEY_TRUCK_NO));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initview();


    }

    private void initview(){
        this.tv_add_back = (TextView)findViewById(R.id.tv_add_back);
        this.tv_add_back.setOnClickListener(this);


        this.textView = (TextView)findViewById(R.id.textView);
        this.textView2 = (TextView)findViewById(R.id.textView2);

       // this.tvCfdCode = (TextView)findViewById(R.id.tv_cfd_code);
       // this.tvMddCode = (TextView)findViewById(R.id.tv_mdd_code);

        this.spinnerBxgs = (Spinner)findViewById(R.id.spinnerBxgs);
        this.spinnerXz = (Spinner)findViewById(R.id.spinnerXz);
        this.spinnerBzfs = (Spinner)findViewById(R.id.spinnerBzfs);
        this.spinnerHwdw = (Spinner)findViewById(R.id.spinnerHwdw);


        this.etCfd = (EditText)findViewById(R.id.et_cfd);
        this.etMdd = (EditText)findViewById(R.id.et_mdd);

        this.autoCtvBbr = (AutoCompleteTextView)findViewById(R.id.autoCtv_bbr);

        autoCtvBbr.setText(Data.getRealName());  //默认真实姓名

        this.autoCtvCph = (AutoCompleteTextView)findViewById(R.id.autoCtv_cph);

        this.etBbrsj = (EditText)findViewById(R.id.et_bbrsj);

        etBbrsj.setText(Data.getMobile());  //默认手机号

        this.etHwsl = (EditText)findViewById(R.id.et_hwsl);
        this.etHwje = (EditText)findViewById(R.id.et_hwje);

        this.etHwmc = (EditText)findViewById(R.id.et_hwmc);

       // this.etCph = (EditText)findViewById(R.id.et_cph);
      //  this.etGcph = (EditText)findViewById(R.id.et_gcph);


        this.tvCph = (TextView)findViewById(R.id.tv_cph);
        tvCph.setOnClickListener(this);


        this.tvCfd = (TextView)findViewById(R.id.tv_cfd);
        tvCfd.setOnClickListener(this);

        this.tvMdd = (TextView)findViewById(R.id.tv_mdd);
        tvMdd.setOnClickListener(this);

        this.tvDate = (TextView)findViewById(R.id.tvDate);
        tvDate.setOnClickListener(this);

        this.tvDate2 = (TextView)findViewById(R.id.tvDate2);
        tvDate2.setOnClickListener(this);

        this.tvHour = (TextView)findViewById(R.id.tvHour);
        tvHour.setOnClickListener(this);

        this.tvHour2 = (TextView)findViewById(R.id.tvHour2);
        tvHour2.setOnClickListener(this);

        this.btn_tj = (Button)findViewById(R.id.btn_tj);
        btn_tj.setOnClickListener(this);

        initDate();

        initAutoTextViewCph();
        initAutoTextViewBbr();


    }

    private void initDate(){

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY)+1;

        System.out.println(" Calender 为 :"+calendar.get(Calendar.YEAR)+"年"
                +calendar.get(Calendar.MONTH)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日");



       // calendar.setTimeInMillis(calendar.getTimeInMillis()+7*864000);

        calendar2.add(Calendar.DATE, 7);

        System.out.println(" Calender 为 :" + calendar.get(Calendar.YEAR) + "年"
                + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");

        System.out.println(" add 1 week0:"+calendar.getTime());

        mYear2 = calendar2.get(Calendar.YEAR);

        //System.out.println("before add 1 week0:" + (calendar.MONTH + 1) + "月" + calendar.DATE);

       // calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 7);
       // calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH));
       // Date date=calendar.getTime();

       // System.out.println(" add 1 week0:"+date);

       mMonth2 = calendar2.get(Calendar.MONTH);
        mDay2 = calendar2.get(Calendar.DAY_OF_MONTH);

        //mDay2 = mDay+7;

        mHour2 = mHour;

        updateDisplay();

    }

    private void initAutoTextViewCph(){

        // 获取搜索记录文件内容
        SharedPreferences sp = getSharedPreferences("add_history", 0);
        String truckNo = sp.getString("truckNo", "暂时没有记录");

        // 用逗号分割内容返回数组
        String[] truckNo_arr = truckNo.split(",");

        // 新建适配器，适配器数据为搜索历史文件内容
        arr_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, truckNo_arr);

        // 保留前50条数据
        if (truckNo_arr.length > 50) {
            String[] newArrays = new String[50];
            // 实现数组之间的复制
            System.arraycopy(truckNo_arr, 0, newArrays, 0, 50);
            arr_adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, truckNo_arr);
        }

        // 设置适配器
        autoCtvCph.setAdapter(arr_adapter);

    }

    private void initAutoTextViewBbr(){

        // 获取搜索记录文件内容
        SharedPreferences sp = getSharedPreferences("add_history", 0);
        String name = sp.getString("name", "暂时没有记录");

        // 用逗号分割内容返回数组
        String[] name_arr = name.split(",");

        // 新建适配器，适配器数据为搜索历史文件内容
        arr_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, name_arr);

        // 保留前50条数据
        if (name_arr.length > 50) {
            String[] newArrays = new String[50];
            // 实现数组之间的复制
            System.arraycopy(name_arr, 0, newArrays, 0, 50);
            arr_adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, name_arr);
        }

        // 设置适配器
        autoCtvBbr.setAdapter(arr_adapter);

    }


    private void autoTextViewTruckNoSave(){
        // 获取搜索框信息
        String text = autoCtvCph.getText().toString();
        SharedPreferences mys1 = getSharedPreferences("add_history", 0);
        String old_text = mys1.getString("truckNo", "暂时没有记录");

        // 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
        StringBuilder builder = new StringBuilder(old_text);
        builder.append(text + ",");

        // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
        if (!old_text.contains(text + ",")) {
            SharedPreferences.Editor myeditor = mys1.edit();
            myeditor.putString("truckNo", builder.toString());
            myeditor.commit();
           // Toast.makeText(this, text + "添加成功", Toast.LENGTH_SHORT).show();
        } else {
           // Toast.makeText(this, text + "已存在", Toast.LENGTH_SHORT).show();
        }

    }

    private void autoTextViewNameSave(){
        // 获取搜索框信息
        String text = autoCtvBbr.getText().toString();
        SharedPreferences mys = getSharedPreferences("add_history", 0);
        String old_text = mys.getString("name", "暂时没有记录");

        // 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
        StringBuilder builder = new StringBuilder(old_text);
        builder.append(text + ",");

        // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
        if (!old_text.contains(text + ",")) {
            SharedPreferences.Editor myeditor = mys.edit();
            myeditor.putString("name", builder.toString());
            myeditor.commit();
          //  Toast.makeText(this, text + "添加成功", Toast.LENGTH_SHORT).show();
        } else {
          //  Toast.makeText(this, text + "已存在", Toast.LENGTH_SHORT).show();
        }

    }


    private void toast(String string){
        ToastUtil.makeToast(this, string);
    }

    //启运日期设置
    private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener()
   {

       @Override
       public void onDateSet(DatePicker datePicker, int year, int month, int day) {
         /*
          if( (year>=calendar.get(Calendar.YEAR))
                  &&(month>=calendar.get(Calendar.MONTH))
                  &&(day>=calendar.get(Calendar.DAY_OF_MONTH)) )
              */
           if( (year*10000+month*100+day) >= calendar0.get(Calendar.YEAR)*10000
                  + calendar0.get(Calendar.MONTH)*100+ calendar0.get(Calendar.DAY_OF_MONTH) )

          {

              calendar.set(year,month,day);
              calendar2.set(year,month,day);

              mYear = year;
              mMonth = month;
              mDay = day;

              calendar2.add(calendar.DATE, 7);

              mYear2 = calendar2.get(Calendar.YEAR);
              mMonth2 = calendar2.get(Calendar.MONTH);


             // mDay2 = mDay+1;
              mDay2 = calendar2.get(Calendar.DAY_OF_MONTH);

              updateDisplay();
          } else {
              //ToastUtil.makeToast(this,"必须晚于当前时间");
              toast("必须晚于当前时间");
             // initDate();
          }
       }
   };


    //启运时间设置
   private TimePickerDialog.OnTimeSetListener timeSetListener1 = new TimePickerDialog.OnTimeSetListener(){
       @Override
       public void onTimeSet(TimePicker timePicker, int i, int i1) {
           //选第二天，时间小于当前时间，会出BUG。系统日期会被一起修改
           System.out.println("所选启运时间为" +(mYear*10000 + mMonth*100 + mDay));
           System.out.println("系统时间为" +(calendar0.get(Calendar.YEAR)*10000
                   +calendar0.get(Calendar.MONTH)*100+calendar0.get(Calendar.DAY_OF_MONTH)) );

          if( (mYear*10000 + mMonth*100 + mDay) > (calendar0.get(Calendar.YEAR)*10000
                  +calendar0.get(Calendar.MONTH)*100+calendar0.get(Calendar.DAY_OF_MONTH)) )
          {
              mHour = i;
              updateDisplay();

          }else if(i >= calendar.get(Calendar.HOUR_OF_DAY)+1) {
               mHour = i;
               updateDisplay();
           }else {
              //选第二天，时间小于当前时间，会出BUG
               toast("必须晚于当前时间1小时以上");

              mHour = calendar.get(Calendar.HOUR_OF_DAY)+1;
              //不应该出现24时
               if(mHour == 24) mHour = 0;
              updateDisplay();
           }
       }
   };


    private DatePickerDialog.OnDateSetListener mDateSetListener2 = new DatePickerDialog.OnDateSetListener()
    {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
           /*
            if( (year>=calendar.get(Calendar.YEAR))
                    &&(month>=calendar.get(Calendar.MONTH))
                    &&(day>=calendar.get(Calendar.DAY_OF_MONTH)+1) )
             */
            if((year*10000+month*100+day) >= (mYear*10000 + mMonth*100 + mDay +1) )
            // if((year*10000+month*100+day) >= (mYear*10000 + mMonth*100 + mDay +7) )

            {
                mYear2 = year;
                mMonth2 = month;
                mDay2 = day;
                mHour2 = mHour;
                updateDisplay();
            } else {
                //ToastUtil.makeToast(this,"必须晚于当前时间");
                toast("必须晚于启运时间1天以上");

                calendar2.set(mYear,mMonth,mDay);
                calendar2.add(Calendar.DATE,1);

                mYear2 = calendar2.get(Calendar.YEAR);
                mMonth2 = calendar2.get(Calendar.MONTH);
                mDay2 = calendar2.get(Calendar.DAY_OF_MONTH);
                mHour2 = mHour;

                updateDisplay();
               // initDate();
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener2 = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            //if(mYear2*10000 + mMonth2*100 + mDay2 > (mYear*10000 + mMonth*100 + mDay +1))
           if(mYear2*10000 + mMonth2*100 + mDay2 > (mYear*10000 + mMonth*100 + mDay +7))
            {
                mHour2 = i;
                updateDisplay();

            }else if(i >= mHour)
            {
                mHour2 = i;
                updateDisplay();
            }else {
                toast("必须晚于启运时间7天以上");
                mHour2 = mHour;
                updateDisplay();
            }
        }
    };

   private void updateDisplay(){
       tvDate.setText(mYear+"-"+(mMonth+1)+"-"+mDay);
       tvHour.setText(mHour+"时");

       tvDate2.setText(mYear2+"-"+(mMonth2+1)+"-"+mDay2);
       tvHour2.setText(mHour2+"时");
   }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.tv_add_back:
                goToActivity(HomeActivity.class);
                break;
            case R.id.tvDate:
                showDialog(DATE1_DIALOG_ID);
                break;

            case R.id.tvHour:
                showDialog(TIMER1_DIALOG_ID);
                break;

            case R.id.tvDate2:
                showDialog(DATE2_DIALOG_ID);
                break;

            case R.id.tvHour2:
                showDialog(TIMER2_DIALOG_ID);
                break;

            case R.id.tv_cph:

           // case R.id.autoCtv_cph:
                startActivityForResult(new Intent(this,TruckNoActivity.class),CPHRESULT);
                break;

            case R.id.tv_cfd:
                System.out.println("PickerMainActivity goto----->" );
                startActivityForResult(new Intent(this, CityMainActivity.class), CFDRESULT);
                break;
            case R.id.tv_mdd:
                System.out.println("PickerMainActivity goto----->" );
                startActivityForResult(new Intent(this, CityMainActivity.class), MDDRESULT);
                break;

            case R.id.btn_tj:

               if (spinnerBxgs.getSelectedItem().toString().substring(0,1).equals("0")||
                       spinnerXz.getSelectedItem().toString().substring(0,1).equals("0")||
                spinnerBzfs.getSelectedItem().toString().substring(0,3).equals("000")){
                   toast("请选择好保险公司、险种和包装方式");
                   break;
               }
                progressDialog_tj = ProgressDialog.show(this, null, "正在提交，请稍候...", true,
                        false);
                orderSubmit();
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


/**/
   protected Dialog onCreateDialog (int id){
       switch (id){
           case DATE1_DIALOG_ID:
               return new DatePickerDialog(this,mDateSetListener1,mYear,mMonth,mDay);

           case DATE2_DIALOG_ID:
               return new DatePickerDialog(this,mDateSetListener2,mYear2,mMonth2,mDay2);

           case TIMER1_DIALOG_ID:
               return new TimePickerDialog(this,timeSetListener1,mHour,0,true);

           case TIMER2_DIALOG_ID:
               return new TimePickerDialog(this,timeSetListener2,mHour,0,true);

       }
       return null;

   }

    public void goToActivity(Class<?> c){
        Intent intent = new Intent(this, c);
        //intent.putExtra("LoginActivity.sID",sID);
        startActivity(intent);
        this.finish();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            progressDialog_tj.cancel();
            UIHelper.ToastMessage(AddActivity.this, msg.obj + "",Toast.LENGTH_LONG);
            //提交成功，则保存新的投保人
            autoTextViewTruckNoSave();
            autoTextViewNameSave();

            System.out.println("msg.what= :" +msg.what);

            if (msg.what == 1) {
               // editor.putString()
               goToActivity(MainActivity.class); //投保成功，则跳转到保单列表。

            } else if (msg.what == -1) {

                UIHelper.ToastMessage(AddActivity.this, "网络无法连接！");
                //ToastUtil.makeToast(AddActivity.this,"没有数据");
            } else if (msg.what == -2) {
                UIHelper.ToastMessage(AddActivity.this,
                        "数据解析异常");
            }
        }
    };

    /*
    请求实例:
        {"Act":"goods","CompanyId":"1","BillType":"2","Insured":"小伍","InsuredSNo":"",
        "InsuredTel":"","InsuredAddress":"","GoodsNo":"","BeginTime":"2016-02-19 06",
        "EndTime":"2016-02-21 06","FromAddress":"深圳","FromCode":"421154","ToAddress":"武汉",
        "ToCode":"425574","PackType":"","GoodsName":"水果","GoodsAmount":"12吨","GoodsWorth":"10",
        "TruckNo":"在B12345","TruckNoG":"","SId":"Q7SNLZVGD4BT0QUJMSZK"}

        返回实例：
        {"result":200,"info": "被保险人不能为空"}
        返回成功实例：
        {"result":1,"info": "投保成功"}
    */
    private void orderSubmit(){

       final JSONObject user = new JSONObject();
        try {
            user.put("Act", "goods");
           // user.put("CompanyId", "1");  //1:平安; 4: 人保; 5: 大地
           // user.put("BillType", 2);   //2 ：基本; 3：综合4：鲜活

            user.put("CompanyId", spinnerBxgs.getSelectedItem().toString().substring(0,1));  //1:平安; 4: 人保; 5: 大地
            user.put("BillType", spinnerXz.getSelectedItem().toString().substring(0, 1));   //2 ：基本; 3：综合4：鲜活

            user.put("Insured", autoCtvBbr.getText()); // 被保人姓名
            user.put("InsuredSNo", "");

            user.put("InsuredTel", etBbrsj.getText()); //被保人手机
            user.put("InsuredAddress", "");
            user.put("GoodsNo", "");
            user.put("BeginTime", tvDate.getText() + " " + Integer.toString(mHour));
            user.put("EndTime", tvDate2.getText()+" "+Integer.toString(mHour2));

            user.put("FromAddress", etCfd.getText()); //出发地信息
            user.put("FromCode", cfdCode);
            user.put("ToAddress", etMdd.getText());  //目的地信息
            user.put("ToCode", mddCode);

            //货物信息
            user.put("PackType", spinnerBzfs.getSelectedItem().toString().substring(0,3)); //包装方式
            user.put("GoodsName", etHwmc.getText());
           // user.put("GoodsAmount", etHwsl.getText()+"吨");
            user.put("GoodsAmount", etHwsl.getText()+spinnerHwdw.getSelectedItem().toString());
            user.put("GoodsWorth", etHwje.getText());


            user.put("TruckNo", autoCtvCph.getText());
            user.put("TruckNoG", "");

            user.put("SId", Data.getsID());

           // user.put("Type", Data.getBillType());    //2 ：基本; 3：综合4：鲜活

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(){
           public void run(){
               Message msg = new Message();
               HttpPost httpPost = new HttpPost(URLs.URL_SUBMIT);

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

                   System.out.println("response:"+response.toString());

               } catch (IOException e) {
                   msg.what = - 1;
                   e.printStackTrace();
               }

               //int re = response.getStatusLine().getStatusCode();
               String retSrc = null;
                   try {
                       retSrc = EntityUtils.toString(response.getEntity());
                       System.out.println("retSrc:"+retSrc);

                   } catch (IOException e) {
                       msg.what = - 2;
                       e.printStackTrace();
                   }
               JSONObject result = null;
                   try {
                       result = new JSONObject(retSrc);
                       String resultFromWeb = result.getString("result");
                       System.out.println("resultFromWeb:"+resultFromWeb);
                       if(resultFromWeb.equals("1")){
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


}
