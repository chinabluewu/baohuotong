package com.demo.app.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.demo.app.R;
import com.demo.app.bean.URLs;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *@author coolszy
 *@date 2012-4-26
 *@blog http://blog.92coding.com
 */

public class UpdateManager
{
    public final static int VERSION_CODE = 70217; //60313指2016-03-13的版本
   // String urlStr="http://192.168.1.69:83/download/bxtUpdate.apk ";
    String urlStr = null;  //下载地址

    final String newFileName = "bxtUpdate.apk";
    int versionCode = VERSION_CODE;
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;

    private static final int IS_UPDATE = 3;
    private static final int IS_NEWEST =4;

    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;


    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            case IS_UPDATE:
                    showNoticeDialog();
                    break;

            case IS_NEWEST:
                Toast.makeText(mContext, "当前已是最新版本，版本号为："+ versionCode, Toast.LENGTH_LONG).show();
                    break;
            // 正在下载
            case DOWNLOAD:
                // 设置进度条位置
                mProgress.setProgress(progress);
                break;
            case DOWNLOAD_FINISH:
                // 安装文件
                installApk();
                break;

            default:
                break;
            }
        }
    };

    public UpdateManager(Context context)
    {
        this.mContext = context;
    }

    /**
     * 检测软件更新

    public void checkUpdate()
    {
       // Toast.makeText(mContext, "当前已是最新版本", Toast.LENGTH_LONG).show();
       // if (isUpdate())
        {
            // 显示提示对话框
          //  showNoticeDialog();

        } //else
       {
           // Toast.makeText(mContext, "当前已是最新版本", Toast.LENGTH_LONG).show();
        }
    }
    */
    /**
     * 检查软件是否有更新版本
     * 
     * @return
     */
    /**/
    public void checkUpdate() {
        // 获取当前软件版本
        //int versionCode = getVersionCode(mContext);



        new Thread() {

            public void run() {
                Message msg =new Message();
                HttpPost httpPost = new HttpPost(URLs.URL_FORUSER);
                JSONObject user = new JSONObject();
                try {
                    user.put("Act", "gVersion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try

                {
                    httpPost.setEntity(new StringEntity(user.toString(), "UTF-8"));
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpResponse response = httpClient.execute(httpPost);
                    String retSrc = EntityUtils.toString(response.getEntity());

                    System.out.println("获得的JSON为：" + retSrc);

                    JSONObject result = new JSONObject(retSrc);
                    JSONArray info = result.getJSONArray("info");
                    JSONObject versionInfo = info.getJSONObject(0);

                    versionCode = Integer.parseInt(versionInfo.getString("versionCode"));//新的版本号
                    urlStr = versionInfo.getString("apkUrl");  //新版本的下载地址


                    System.out.println("JSON的版本号为0：" + versionInfo.getString("versionCode"));
                    System.out.println("获得的版本号为0：" + versionCode);
                    System.out.println("获得的下载地址为：" + urlStr);

                    if (versionCode > VERSION_CODE)
                    {
                        mHandler.sendEmptyMessage(IS_UPDATE);
                        // return true;
                        //showNoticeDialog();
                    }else {
                        //当前是最新版
                        mHandler.sendEmptyMessage(IS_NEWEST);
                    }

                } catch (UnsupportedEncodingException e)
                {e.printStackTrace();
                } catch (ClientProtocolException e)
                {e.printStackTrace();
                } catch (IOException e)
                {e.printStackTrace();
                } catch (JSONException e)
                {e.printStackTrace();
                }
            }
        }.start();
        System.out.println("当前的版本号为1：" + versionCode);

        /*
        // 把version.xml放到网络上，然后获取文件信息
        InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
        // 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
        ParseXmlService service = new ParseXmlService();

        try
        {
            mHashMap = service.parseXml(inStream);
        } catch (Exception e)
        {
            e.printStackTrace();
        }*/
                //if (null != mHashMap)
                //{
                // int serviceCode = Integer.valueOf(mHashMap.get("version"));
                // 版本判断

                //}
                //return false;

                //mHandler.sendMessage(msg);


        //return true;
    }

/**
 * 获取软件版本号
 * 
 * @param context
 * @return
 */
private int getVersionCode(Context context)
{
    int versionCode = 0;
    try
    {
        // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
        versionCode = context.getPackageManager().getPackageInfo("com.demo.app", 0).versionCode;
    } catch (NameNotFoundException e)
    {
        e.printStackTrace();
    }
    return versionCode;
}

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog()
    {
        //System.out.println("弹出对话框0");
        // 构造对话框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.txt_newVersion);
        builder.setMessage(R.string.txt_newVersionInfo);
        // 更新
        builder.setPositiveButton("更新", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton("下次更新", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog()
    {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.txt_dowloadNewVersion);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        // mProgress = (SeekBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton(R.string.cancle, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk()
    {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     * 
     * @author coolszy
     *@date 2012-4-26
     *@blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread
    {
        @Override
        public void run()
        {
           // System.out.println("开始下载APK");
            try
            {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    //URL url = new URL(mHashMap.get("url"));
                    URL url = new URL(urlStr); //APK 更新地址
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    //System.out.println("连上服务器");

                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists())
                    {
                        file.mkdir();
                    }
                   // File apkFile = new File(mSavePath, mHashMap.get("name"));
                    File apkFile = new File(mSavePath,newFileName);

                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        //if (numread <= 0)
                        if (numread == -1)
                        {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);

                       // System.out.println("buffer值= "+numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.

                    fos.flush();
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();

        }
    };

    /**
     * 安装APK文件
     */
    private void installApk()
    {
       // File apkfile = new File(mSavePath, mHashMap.get("name"));
        File apkfile = new File(mSavePath, newFileName);
        if (!apkfile.exists())
        {
            return;
        }
        // 通过Intent安装APK文件

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}