package com.demo.app.cascadingmenu;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.demo.app.R;
import com.demo.app.ToastUtil;
import com.demo.app.cascadingmenu.interfaces.CascadingMenuViewOnSelectListener;
import com.demo.app.model.Area;

public class CityMainActivity extends FragmentActivity implements OnClickListener {
	

	ArrayList<Area> provinceList;
	// 两级联动菜单数据
	private CascadingMenuFragment cascadingMenuFragment = null;
	private CascadingMenuPopWindow cascadingMenuPopWindow = null;

	//private Button menuViewPopWindow;
	//private Button menuViewFragment;
	private Button btn_cityOK,btn_cityCancel;
	private Button btn_citypicker;
	private DBhelper dBhelper;

	public   static String KEY_CITY_NAME= "KEY_CITY_NAME";
	public   static String KEY_CITY_CODE= "KEY_CITY_CODE";
	public static String AREA_NAME="深圳";
	public  static String AREA_CODE = "123456";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_main);
//		DBManager dbManager = new DBManager(this);
//		dbManager.openDatabase();
//		dbManager.closeDatabase();


		//向三级menu添加地区数据
		dBhelper = new DBhelper(this);
		provinceList = dBhelper.getProvince();
		//System.out.println("dBhelper打开正常");

//		for (int k = 0; k < 7; k++) {
//			secondItems = new ArrayList<MenuItem>();
//			for (int j = 0; j < 7; j++) {
//				thirdItems = new ArrayList<MenuItem>();
//				for (int i = 0; i < 8; i++) {
//					thirdItems.add(new MenuItem(3, "3级菜单" + k+""+ j + "" + i, null,
//							null));
//				}
//				secondItems.add(new MenuItem(2, "2级菜单" +k+""+ j, null, thirdItems));
//			}
//			firstItems.add(new MenuItem(3,"1级菜单"+k,secondItems,thirdItems));
//		}


		btn_cityOK=(Button)findViewById(R.id.btn_cityOK);
		btn_cityCancel=(Button)findViewById(R.id.btn_cityCancel);
		btn_citypicker=(Button)findViewById(R.id.btn_citypicker);

		btn_cityOK.setOnClickListener(this);
		btn_cityCancel.setOnClickListener(this);
		btn_citypicker.setOnClickListener(this);
		//showPopMenu();
        //initdata();


		//System.out.println("创建完成");

	}



	public void showFragmentMenu() {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.short_menu_pop_in,
				R.anim.short_menu_pop_out);

		if (cascadingMenuFragment == null) {
			cascadingMenuFragment = CascadingMenuFragment.getInstance();
			cascadingMenuFragment.setMenuItems(provinceList);
			cascadingMenuFragment
					.setMenuViewOnSelectListener(new NMCascadingMenuViewOnSelectListener());
			fragmentTransaction.replace(R.id.liner, cascadingMenuFragment);
		} else {
			fragmentTransaction.remove(cascadingMenuFragment);
			cascadingMenuFragment = null;
		}
		fragmentTransaction.commit();
	}

	public void showPopMenu() {
		if (cascadingMenuPopWindow == null) {
			cascadingMenuPopWindow = new CascadingMenuPopWindow(
					getApplicationContext(), provinceList);
			cascadingMenuPopWindow
					.setMenuViewOnSelectListener(new NMCascadingMenuViewOnSelectListener());
			cascadingMenuPopWindow.showAsDropDown(btn_cityOK, 5, 5);
			ToastUtil.makeToast(this, "请点击要选择的地名,选好后点确定");
		} else if (cascadingMenuPopWindow != null
				&& cascadingMenuPopWindow.isShowing()) {
			cascadingMenuPopWindow.dismiss();
		} else {
			cascadingMenuPopWindow.showAsDropDown(btn_cityOK, 5, 5);
			ToastUtil.makeToast(this, "请点击要选择的地名,选好后点确定");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	// 级联菜单选择回调接口
	class NMCascadingMenuViewOnSelectListener implements
			CascadingMenuViewOnSelectListener {

		@Override
		public void getValue(Area area) {
			cascadingMenuFragment = null;
		//	Toast.makeText(getApplicationContext(), "选择的地址为:" + area.getName(),
		//			1000).show();
			//areaName = area.getPname()+area.getCname()+area.getName();
			//areaName = area.getName();
			//areaCode = area.getPcode();
			//ToastUtil.makeToast(getApplicationContext(), "选择的地址为:" + AREA_NAME + AREA_CODE);

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//点的确定：
		case R.id.btn_cityOK:
			//	case R.id.tv_cfd:
            if(AREA_CODE != null) {
				Intent intent = new Intent();
				intent.putExtra(KEY_CITY_NAME, AREA_NAME);
				intent.putExtra(KEY_CITY_CODE, AREA_CODE);
				setResult(RESULT_OK, intent);
				finish();

			}else {
				ToastUtil.makeToast(this, "请点击城市名");
			}

			break;
		case R.id.btn_citypicker:
			showPopMenu();

			break;

		   //点的取消：
		case R.id.btn_cityCancel:
			//showFragmentMenu();
			finish();
			break;
		}
	}
}
