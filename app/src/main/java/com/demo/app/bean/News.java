package com.demo.app.bean;

public class News extends Base {
	private String compandName;
	private String name;

	private String SmallNo;
	private String TruckNo;
	private String state;
	private String publishTime;
    private String Detail;
	private String firstPicUrl;



	public String getCompandName() {
		return compandName;
	}
	public void setCompandName(String compandName) {
		this.compandName = compandName;
	}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public String getPublishTime() {return publishTime;}
	public void setPublishTime(String publishTime) {this.publishTime = publishTime;}


	public String getSmallNo() { return SmallNo; }        //add
	public void setSmallNo(String smallNo) {SmallNo = smallNo;}

	public String getTruckNo() {return TruckNo;}
	public void setTruckNo(String truckNo) {TruckNo = truckNo;}

	public String getState() {return state;}
	public void setState(String state) {this.state = state;}

	public String getDetail(){return Detail;}
	public void setDetail(String detail) {this.Detail = detail;}

	public String getFirstPicUrl() {
		return firstPicUrl;
	}

	public void setFirstPicUrl(String firstPicUrl) {
		this.firstPicUrl = firstPicUrl;
	}
}
