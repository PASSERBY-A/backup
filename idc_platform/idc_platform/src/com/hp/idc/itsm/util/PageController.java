package com.hp.idc.itsm.util;

import java.util.ArrayList;
import java.util.List;

public class PageController {

	private int pageNow = 0; // ��ǰҳ

	private int pagePrevious = 0; // ǰһҳ

	private int pageNext = 0; // ��һҳ

	private int pageAll = 0; // ��ҳ��

	private int pageCount = 20; // ��ҳ����
	
	private boolean hasPrevious = false; //�Ƿ�����һҳ
	
	private boolean hasNext = false; //�Ƿ�����һҳ

	private List records = null; // ȫ��¼

	private List pageRecords = null; // ѡȡ��ҳ���¼
	
	private List pageNumList = null; //���ҳ����,ֻ��10��

	public PageController() {
	}

	public PageController(List allRecords, int pageCount) {
		this.records = allRecords;
		this.pageCount = pageCount;
		if (this.records != null) {
			this.pageAll = (records.size() + this.pageCount - 1)
					/ this.pageCount;
			this.setPageNow(1);
		}
	}

	public PageController(List allRecords, int pageNow, int pageCount) {
		this.records = allRecords;
		this.pageCount = pageCount;
		if (records != null)
			this.pageAll = (records.size() + this.pageCount - 1)
					/ this.pageCount;
		this.setPageNow(pageNow);

	}
	
	public void getPreviousPage(){
		this.setPageNow(this.pageNow-1);
	}
	
	public void getNextPage() {
		this.setPageNow(this.pageNow+1);
	}
	
	public void getAppointedPage(int pageNum) {
		this.setPageNow(pageNum);
	}

	public List getPageRecords() {
		return pageRecords;
	}

	public void setPageRecords(List pageRecords) {
		this.pageRecords = pageRecords;
	}

	public List getRecords() {
		return records;
	}

	public void setRecords(List records) {
		this.records = records;
		if (this.records != null)
			this.pageAll = (records.size() + this.pageCount - 1)
					/ this.pageCount;
	}

	public int getPageAll() {
		return pageAll;
	}

	public void setPageAll(int pageAll) {
		this.pageAll = pageAll;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageNext() {
		return pageNext;
	}

	public void setPageNext(int pageNext) {
		this.pageNext = pageNext;
	}

	public int getPageNow() {
		return pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
		if (this.pageNow <= 1) {
			this.pageNow = 1;
			this.hasPrevious = false;
			if (this.pageNow<this.pageAll)
				this.hasNext = true;
			else 
				this.hasNext = false;
		} else if (this.pageNow >= this.pageAll) {
			this.pageNow = this.pageAll;
			this.hasNext = false;
			if (this.pageNow>1)
				this.hasPrevious = true;
			else
				this.hasPrevious = false;
		} else {
			this.hasNext = true;
			this.hasPrevious = true;
		}
		//��ȡҪ��ʾ������
		this.pagePrevious = this.pageNow - 1;
		if (this.pagePrevious < 1)
			this.pagePrevious = 1;
		this.pageNext = this.pageNow + 1;
		if (this.pageNext > this.pageAll)
			this.pageNext = this.pageAll;
		int beginIndex = (this.pageNow - 1) * pageCount;
		//System.out.println("beginIndex:"+beginIndex);
		int endIndex = this.pageNow * this.pageCount;
		if ( endIndex > this.records.size()) {
			endIndex = this.records.size();
		}
		//System.out.println("endIndex:"+endIndex);
		if (pageRecords!=null)
			pageRecords = null;
		pageRecords = new ArrayList();
		for (int i = beginIndex; i < endIndex; i++) {
			pageRecords.add(this.records.get(i));
		}
		//��ȡ��ʾ��ҳ������
		int pageNumBegin = 1;
		int pageNumEnd = 1;
		if (this.pageAll < 10)
			pageNumEnd = this.pageAll;
		else {
			
			pageNumBegin = this.pageNow - 4;
			pageNumEnd = this.pageNow + 4;
			if (pageNumBegin <= 1) {
				pageNumBegin = 1;
				pageNumEnd = 9;
			}
			if (pageNumEnd >= this.pageAll) {
				pageNumBegin = this.pageAll-8;
				pageNumEnd = this.pageAll;
			}
		}
		if (pageNumList!=null)
			pageNumList = null;
		pageNumList = new ArrayList();
		for (int i = pageNumBegin; i <= pageNumEnd; i++) {
			Integer ss = new Integer(i);
			
			this.pageNumList.add(ss);
		}
	}

	public int getPagePrevious() {
		return pagePrevious;
	}

	public void setPagePrevious(int pagePrevious) {
		this.pagePrevious = pagePrevious;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public boolean isHasPrevious() {
		return hasPrevious;
	}

	public void setHasPrevious(boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}

	public List getPageNumList() {
		return pageNumList;
	}

	public void setPageNumList(List pageNumList) {
		this.pageNumList = pageNumList;
	}
}
