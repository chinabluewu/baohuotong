package com.demo.app.cascadingmenu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.demo.app.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wu on 2016/2/22.
 */
public class DBManager {


    public final static String DB_NAME = "city_cn.s3db";
    public final static String PACKAGE_NAME = "com.demo.app";  //这个地方一定要正确，否则无法创建
   public final static String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath()
                                             +"/"+PACKAGE_NAME;

    //public final static String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
    //        +"/city";

    private final int BUFFER_SIZE =1024;
    private Context context;
    private SQLiteDatabase database;
    private File file = null;



    DBManager(Context context){
        this.context = context;
    }

    public void openDatabase(){
        this.database = this.openDatabase(DB_PATH + "/" +DB_NAME);
    }

    public SQLiteDatabase getDatabase(){
        return this.database;
    }
    private SQLiteDatabase openDatabase(String dbfile){
        try {
            file = new File(dbfile);
            if (!file.exists()) {
                InputStream is = context.getResources().openRawResource(R.raw.city);
                if(is!=null){
                }else{
                }
                FileOutputStream fos = new FileOutputStream(dbfile);
                System.out.println("文件打开正常"+fos.toString());
                if(is!=null){
                }else{
                }
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count =is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                    fos.flush();
                }
                fos.close();
                is.close();
            }
            //System.out.println("文件已经存在");
            database = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
            return database;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
        }
        return null;

    }
    public void closeDatabase() {
        if(this.database!=null)
            this.database.close();
    }

}
