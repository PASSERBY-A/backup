package com.hp.idc.cas.log;

public class LogInfo {
	
	public static final String OPER_TYPE_ADD = "add";
	public static final String OPER_TYPE_UPDATE = "update";
	public static final String OPER_TYPE_DELETE = "delete";

	private String operUser = "";
	
	private long operTime = 0;
	
	private String operIp = "";
	
	private String operType = "";
	
	private String operObjOId = "";
	
	private String operObjName = "";
	
	public String getOperUser() {
		return operUser;
	}

	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}

	public long getOperTime() {
		return operTime;
	}

	public void setOperTime(long operTime) {
		this.operTime = operTime;
	}

	public String getOperIp() {
		return operIp;
	}

	public void setOperIp(String operIp) {
		this.operIp = operIp;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getOperObjOId() {
		return operObjOId;
	}

	public void setOperObjOId(String operObjOId) {
		this.operObjOId = operObjOId;
	}

	public String getOperObjName() {
		return operObjName;
	}

	public void setOperObjName(String operObjName) {
		this.operObjName = operObjName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private String content = "";
}
