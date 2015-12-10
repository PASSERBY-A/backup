/**
 * 
 */
package com.hp.idc.resm.ui;

import java.io.Serializable;
import java.util.List;

/**
 * ��ҳ��ѯ������Ϣ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T> ���صĶ�������
 *
 */
public class PageInfo<T> implements Serializable {
	
	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 6916601239015269060L;

	/**
	 * ��ǰҳ����1��ʼ
	 */
	private int currentPage;

	/**
	 * �ܼ�¼����
	 */
	private int totalCount;

	/**
	 * ��ҳ��
	 */
	private int totalPage;
	
	/**
	 * ��ǰ���ص�һ����¼����������1��ʼ
	 */
	private int index;
	
	/**
	 * ÿҳ����
	 */
	private int pageCount;
	
	/**
	 * ���ص�����
	 */
	private List<T> data;
	
	/**
	 * ��ȡ��ǰҳ
	 * @return ��ǰҳ����1��ʼ
	 * @see #currentPage
	 */
	public int getCurrentPage() {
		return this.currentPage;
	}

	/**
	 * ���õ�ǰҳ
	 * @param currentPage ��ǰҳ����1��ʼ
	 * @see #currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * ��ȡ��ҳ��
	 * @return ��ҳ��
	 * @see #totalPage
	 */
	public int getTotalPage() {
		return this.totalPage;
	}

	/**
	 * ������ҳ��
	 * @param totalPage ��ҳ��
	 * @see #totalPage
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * ��ȡ��ǰ���ص�һ����¼������
	 * @return ��ǰ���ص�һ����¼����������1��ʼ
	 * @see #index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * ���õ�ǰ���ص�һ����¼������
	 * @param index ��ǰ���ص�һ����¼����������1��ʼ
	 * @see #index
	 */
	public void setIndex(int index) {
		this.index = index;
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

	/**
	 * ��ȡ���ص�����
	 * @return ���ص�����
	 * @see #data
	 */
	public List<T> getData() {
		return this.data;
	}

	/**
	 * ���÷��ص�����
	 * @param data ���ص�����
	 * @see #data
	 */
	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * ��ȡ�ܼ�¼����
	 * @return �ܼ�¼����
	 * @see #totalCount
	 */
	public int getTotalCount() {
		return this.totalCount;
	}

	/**
	 * �����ܼ�¼����
	 * @param totalCount �ܼ�¼����
	 * @see #totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
