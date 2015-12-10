/**
 * 
 */
package com.hp.idc.resm.ui;

import java.io.Serializable;

/**
 * ��ҳ��ѯ������Ϣ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class PageQueryInfo implements Serializable {
	
	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = -6147105543182955527L;

	/**
	 * �����ҳ�棬��1��ʼ
	 */
	private int startPage;
	
	/**
	 * ÿҳ����
	 */
	private int pageCount;
	
	/**
	 * ������Ϣ����id,name,-field,..."���м��ٵı�ʾ�Ӵ�С����
	 */
	private String sortBy;
	
	/**
	 * ��ȡ������Ϣ
	 * @return sortBy ��id,name,-field,..."���м��ٵı�ʾ�Ӵ�С����
	 * @see #sortBy
	 */
	public String getSortBy() {
		return this.sortBy;
	}

	/**
	 * ����������Ϣ
	 * @param sortBy ��id,name,-field,..."���м��ٵı�ʾ�Ӵ�С����
	 * @see #sortBy
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	/**
	 * ��ȡ�����ҳ��
	 * @return �����ҳ�棬��1��ʼ
	 * @see #startPage
	 */
	public int getStartPage() {
		return this.startPage;
	}

	/**
	 * ���������ҳ��
	 * @param startPage �����ҳ�棬��1��ʼ
	 * @see #startPage
	 */
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	/**
	 * ��ȡÿҳ����
	 * @return ÿҳ����
	 * @see #pageCount
	 */
	public int getPageCount() {
		return this.pageCount;
	}

	/**
	 * ����ÿҳ����
	 * @param pageCount ÿҳ����
	 * @see #pageCount
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
}
