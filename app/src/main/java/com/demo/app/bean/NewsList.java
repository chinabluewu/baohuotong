package com.demo.app.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.app.AppException;

public class NewsList extends Base {
	private int catalog;
	private int pageSize;
	private int newsCount;
	private List<News> newslist = new ArrayList<News>();

	public int getCatalog() {
		return catalog;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getNewsCount() {
		return newsCount;
	}

	public List<News> getNewslist() {
		return newslist;
	}

	public static NewsList parse(JSONArray obj) throws IOException,
			AppException, JSONException {
		NewsList newslist = new NewsList();

		if (null != obj) {
			newslist.newsCount = obj.length();
			for (int i = 0; i < obj.length(); i++) {
				JSONObject newsJson = obj.getJSONObject(i);
				News news = new News();
				/*
				news.setId(newsJson.getString("ID"));
				news.setTitle(newsJson.getString("Title"));
				news.setFirstPicUrl(newsJson.getString("FirstPicUrl"));
				news.setPublishTime(newsJson.getString("PublishTime"));
                */
				//news.setSmallNo("保单号:" + newsJson.getString("InsuSmallNo"));  //保单号
				news.setSmallNo( newsJson.getString("InsuSmallNo"));  //保单号

				news.setCompandName(newsJson.getString("InsuCompanyName") ); //保险公司

				news.setName("" + newsJson.getString("InsuInsured")); //被保险人
				news.setTruckNo( newsJson.getString("InsuTruckNo")); //车牌号
				//news.setDetail(" 详  情 ");

				//String a = "2",b;
				//b = newsJson.getString("InsuState").equals(a)? "成功" :"审核中";

				news.setState((newsJson.getString("InsuState").equals("2")? "成功" :"审核中"));//状态
				news.setPublishTime("" + newsJson.getString("AddTime"));  //时间

				newslist.newslist.add(news);
			}
		}
		return newslist;
	}
}
