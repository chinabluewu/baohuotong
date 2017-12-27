package com.demo.app.bean;

import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * 接口URL实体类
 * @version 1.0
 * @created 2012-3-21
 */
public class URLs implements Serializable {
	
	//public final static String HOST = "m.bitauto.com";
	//public final static String HOST = "192.168.1.69:83";
	//public final static String HOST = "116.255.146.231:1983";  //实际服务器
	//public final static String HOST = "116.255.146.231";  //实际服务器
	//public final static String HOST = "116.255.141.207";  //实际服务器
	public final static String HOST = "120.76.244.44";  //实际阿里云服务器

	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	private final static String URL_SPLITTER = "/";
	private final static String URL_UNDERLINE = "_";
	
	private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;


	public final static String URL_FORUSER = URL_API_HOST+"/appsvr/usersvr.aspx";
	public final static String URL_FORDATA = URL_API_HOST+"/appsvr/datasvr.aspx";
	public final static String URL_SUBMIT = URL_API_HOST+"/appsvr/prosvr.aspx";
	public final static String URL_UPDATE_APK = URL_API_HOST+"/download/bxtUpdate.apk";

	public final static String URL_DOWNLOAD_PDF = URL_API_HOST+"/download/getbill.aspx";  //下载PDF保单的接口

	//public final static String NEWS_LIST = "http://m.bitauto.com"+"/appapi/News/List.ashx/"; //test
	public final static String NEWS_LIST = URL_API_HOST+"/appsvr/datasvr.aspx";
	
	
	
	
	public final static int URL_OBJ_TYPE_OTHER = 0x000;
	public final static int URL_OBJ_TYPE_NEWS = 0x001;
	public final static int URL_OBJ_TYPE_SOFTWARE = 0x002;
	public final static int URL_OBJ_TYPE_QUESTION = 0x003;
	public final static int URL_OBJ_TYPE_ZONE = 0x004;
	public final static int URL_OBJ_TYPE_BLOG = 0x005;
	public final static int URL_OBJ_TYPE_TWEET = 0x006;
	public final static int URL_OBJ_TYPE_QUESTION_TAG = 0x007;
	
	private int objId;
	private String objKey = "";
	private int objType;
	
	public int getObjId() {
		return objId;
	}
	public void setObjId(int objId) {
		this.objId = objId;
	}
	public String getObjKey() {
		return objKey;
	}
	public void setObjKey(String objKey) {
		this.objKey = objKey;
	}
	public int getObjType() {
		return objType;
	}
	public void setObjType(int objType) {
		this.objType = objType;
	}

	/**
	 * 解析url获得objId
	 * @param path
	 * @param url_type
	 * @return
	 */
	private final static String parseObjId(String path, String url_type){
		String objId = "";
		int p = 0;
		String str = "";
		String[] tmp = null;
		p = path.indexOf(url_type) + url_type.length();
		str = path.substring(p);
		if(str.contains(URL_SPLITTER)){
			tmp = str.split(URL_SPLITTER);
			objId = tmp[0];
		}else{
			objId = str;
		}
		return objId;
	}
	
	/**
	 * 解析url获得objKey
	 * @param path
	 * @param url_type
	 * @return
	 */
	private final static String parseObjKey(String path, String url_type){
		path = URLDecoder.decode(path);
		String objKey = "";
		int p = 0;
		String str = "";
		String[] tmp = null;
		p = path.indexOf(url_type) + url_type.length();
		str = path.substring(p);
		if(str.contains("?")){
			tmp = str.split("?");
			objKey = tmp[0];
		}else{
			objKey = str;
		}
		return objKey;
	}
	
	/**
	 * 对URL进行格式处理
	 * @param path
	 * @return
	 */
	private final static String formatURL(String path) {
		if(path.startsWith("http://") || path.startsWith("https://"))
			return path;
		return "http://" + URLEncoder.encode(path);
	}	
}
