package com.hp.idc.report.util;

public class Page {
	// ��ǰҳ�ĵ�һ����¼
	private int first;
	// ��ǰҳ�����һ����¼
	private int last;
	// �ܵļ�¼��
	private int size;
	// ��ǰҳ�ļ�¼��
	private int limit;

	// ��ѯ��¼�Ŀ�ʼʱ��
	private String startTime;
	// ��ѯ��¼�Ľ���ʱ��
	private String endTime;

	// �������ͣ�����ҵ����Ϣ����ֵҵ����Ϣ
	private int reportType;
	// ��½�û���
	private String loginUser;

	// ����������
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
