package com.hp.idc.report.util;

public class Page {
	// 当前页的第一条记录
	private int first;
	// 当前页的最后一条记录
	private int last;
	// 总的记录数
	private int size;
	// 当前页的记录数
	private int limit;

	// 查询记录的开始时间
	private String startTime;
	// 查询记录的结束时间
	private String endTime;

	// 报表类型：基本业务信息或增值业务信息
	private int reportType;
	// 登陆用户名
	private String loginUser;

	// 导出报表名
	private String title;

	public Page() {

	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
