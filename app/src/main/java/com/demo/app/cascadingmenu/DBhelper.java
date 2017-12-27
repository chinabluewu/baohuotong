package com.demo.app.cascadingmenu;

import java.util.ArrayList;
import java.util.List;


import com.demo.app.model.Area;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBhelper {
	private SQLiteDatabase db;
	private Context context;
	private DBManager dbm;
	
	public DBhelper(Context context) {
		super();
		this.context = context;
		dbm = new DBManager(context);
	}


	//获取省份列表
	public ArrayList<Area> getProvince() {
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	ArrayList<Area> list = new ArrayList<Area>();
		
	 	try {    
	        String sql = "select * from province";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	String code=cursor.getString(cursor.getColumnIndex("code")); 
		        byte bytes[]=cursor.getBlob(2);
		        String name=new String(bytes,"gbk").trim();  //出掉结尾的空格
		        Area area=new Area();
				area.setName(name);
				//area.setPname(name);   //add by wu
		        area.setCode(code);
		        list.add(area);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("code")); 
	        byte bytes[]=cursor.getBlob(2); 
	        String name=new String(bytes,"gbk").trim();  //出掉结尾的空格
	        Area area=new Area();
			area.setName(name);
			//area.setPname(name);   //add by wu
	        area.setCode(code);
	        list.add(area);
	        
	    } catch (Exception e) {  
	    	return null;
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
		return list;
		
	}

	//获取城市列表
	public ArrayList<Area> getCity(String pcode) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		ArrayList<Area> list = new ArrayList<Area>();

		try {
			String sql = "select * from city where pcode='"+pcode+"'";
			Cursor cursor = db.rawQuery(sql,null);
			cursor.moveToFirst();
			while (!cursor.isLast()){
				String code=cursor.getString(cursor.getColumnIndex("code"));
				byte bytes[]=cursor.getBlob(2);
				String name=new String(bytes,"gbk").trim();  //出掉结尾的空格
				Area area=new Area();
				area.setName(name);
				//area.setCname(name);   //add by wu
				area.setCode(code);
				area.setPcode(pcode);
				list.add(area);
				cursor.moveToNext();
			}
			String code=cursor.getString(cursor.getColumnIndex("code"));
			byte bytes[]=cursor.getBlob(2);
			String name=new String(bytes,"gbk").trim();  //出掉结尾的空格
			Area area=new Area();
			area.setName(name);
			//area.setCname(name);      //add by wu
			area.setCode(code);
			area.setPcode(pcode);
			list.add(area);

		} catch (Exception e) {
			return null;
		}
		dbm.closeDatabase();
		db.close();

		return list;

	}

	//获取区域列表
	public ArrayList<Area> getDistrict(String pcode) {
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	ArrayList<Area> list = new ArrayList<Area>();
	 	try {    
	        String sql = "select * from district where pcode='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);
	        if (cursor.moveToFirst()) {
				while (!cursor.isLast()) {
					String code = cursor.getString(cursor
							.getColumnIndex("code"));
					byte bytes[] = cursor.getBlob(2);
					String name = new String(bytes, "gbk").trim();  //出掉结尾的空格
					Area Area = new Area();
					Area.setName(name);
					Area.setPcode(code);
					list.add(Area);
					cursor.moveToNext();
				}
				String code = cursor.getString(cursor.getColumnIndex("code"));
				byte bytes[] = cursor.getBlob(2);
				String name = new String(bytes, "gbk").trim();  //出掉结尾的空格

				Area Area = new Area();
				Area.setName(name);
				Area.setPcode(code);
				list.add(Area);
				//Log.e("多了空格", name);

			}
	        
	    } catch (Exception e) { 
	    	Log.i("wer", e.toString());
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
		return list;
		
	}
}
