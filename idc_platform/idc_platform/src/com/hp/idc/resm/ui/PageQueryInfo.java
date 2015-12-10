/**
 * 
 */
package com.hp.idc.resm.ui;

import java.io.Serializable;

/**
 * 分页查询请求信息
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class PageQueryInfo implements Serializable {
	
	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -6147105543182955527L;

	/**
	 * 请求的页面，从1开始
	 */
	private int startPage;
	
	/**
	 * 每页条数
	 */
	private int pageCount;
	
	/**
	 * 排序信息，”id,name,-field,..."，有减少的表示从大到小排序
	 */
	private String sortBy;
	
	/**
	 * 获取排序信息
	 * @return sortBy ”id,name,-field,..."，有减少的表示从大到小排序
	 * @see #sortBy
	 */
	public String getSortBy() {
		return this.sortBy;
	}

	/**
	 * 设置排序信息
	 * @param sortBy ”id,name,-field,..."，有减少的表示从大到小排序
	 * @see #sortBy
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	/**
	 * 获取请求的页面
	 * @return 请求的页面，从1开始
	 * @see #startPage
	 */
	public int getStartPage() {
		return this.startPage;
	}

	/**
	 * 设置请求的页面
	 * @param startPage 请求的页面，从1开始
	 * @see #startPage
	 */
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	/**
	 * 获取每页条数
	 * @return 每页条数
	 * @see #pageCount
	 */
	public int getPageCount() {
		return this.pageCount;
	}

	/**
	 * 设置每页条数
	 * @param pageCount 每页条数
	 * @see #pageCount
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
}
