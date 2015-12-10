/**
 * 
 */
package com.hp.idc.resm.ui;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询返回信息
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T> 返回的对象类型
 *
 */
public class PageInfo<T> implements Serializable {
	
	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 6916601239015269060L;

	/**
	 * 当前页，从1开始
	 */
	private int currentPage;

	/**
	 * 总记录条数
	 */
	private int totalCount;

	/**
	 * 总页数
	 */
	private int totalPage;
	
	/**
	 * 当前返回第一条记录的索引，从1开始
	 */
	private int index;
	
	/**
	 * 每页条数
	 */
	private int pageCount;
	
	/**
	 * 返回的数据
	 */
	private List<T> data;
	
	/**
	 * 获取当前页
	 * @return 当前页，从1开始
	 * @see #currentPage
	 */
	public int getCurrentPage() {
		return this.currentPage;
	}

	/**
	 * 设置当前页
	 * @param currentPage 当前页，从1开始
	 * @see #currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 获取总页数
	 * @return 总页数
	 * @see #totalPage
	 */
	public int getTotalPage() {
		return this.totalPage;
	}

	/**
	 * 设置总页数
	 * @param totalPage 总页数
	 * @see #totalPage
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 获取当前返回第一条记录的索引
	 * @return 当前返回第一条记录的索引，从1开始
	 * @see #index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * 设置当前返回第一条记录的索引
	 * @param index 当前返回第一条记录的索引，从1开始
	 * @see #index
	 */
	public void setIndex(int index) {
		this.index = index;
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

	/**
	 * 获取返回的数据
	 * @return 返回的数据
	 * @see #data
	 */
	public List<T> getData() {
		return this.data;
	}

	/**
	 * 设置返回的数据
	 * @param data 返回的数据
	 * @see #data
	 */
	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * 获取总记录条数
	 * @return 总记录条数
	 * @see #totalCount
	 */
	public int getTotalCount() {
		return this.totalCount;
	}

	/**
	 * 设置总记录条数
	 * @param totalCount 总记录条数
	 * @see #totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
