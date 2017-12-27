package com.demo.app.model;

public class Area {
	private String code;  //本级的编码
	private String name;
	private String cname; //市名
	private String pname; //省名

	private String pcode; //上级的编码

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCname() {
		return cname;
	}
	public void setCname(String name) {
		this.cname = cname;
	}

	public String getPname() {
		return pname;
	}
	public void setPname(String name) {
		this.pname = pname;
	}


	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}


	public Area() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public String toString() {
		return "Area [code=" + code + ", name=" + name + ", pcode=" + pcode
				+ "]";
	}
	
	
}
