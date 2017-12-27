package com.demo.app;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
	public static void makeToast(Context context,int stringId){
		Toast toast = Toast.makeText(context,context.getResources().getString(stringId), Toast.LENGTH_SHORT);
    	toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2, toast.getYOffset()/2);  
    	toast.show();
	}
	public static void makeToast(Context context,String string){
		Toast toast = Toast.makeText(context,string, Toast.LENGTH_SHORT);
    	toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2, toast.getYOffset()/2);  
    	toast.show();
	}
	public static void makeToastDuttom(Context context,String string){
		Toast toast = Toast.makeText(context,string, Toast.LENGTH_SHORT);
    	toast.show();
	}
	
	
	
}
