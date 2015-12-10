package com.hp.idc.itsm.dbo;

/**
 * 取数据时的分页信息
 * @author 梅园
 *
 */
public class PageInfo {
	/**
	 * 开始的页面
	 */
	protected int startPage = 0;
	
	/**
	 * 记录总数
	 */
	protected int count = 0;
	
	/**
	 * 每页的记录数
	 */
	protected int rowsPerPage = 50;
	
	/**
	 * 总页数
	 */
	protected int pageCount;

	/**
	 * 获取总页数
	 * @return 返回总页数
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * 设置总页数
	 * @param pageCount 总页数
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * 重新计算页码及页数
	 */
	public void recalc() {
		pageCount = (count - 1) / rowsPerPage + 1;
		if (startPage >= pageCount)
			startPage = pageCount - 1;
	}

	/**
	 * 获取记录总数
	 * @return 返回记录总数
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * 设置记录总数
	 * @param count 记录总数
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * 获取每页的记录数
	 * @return 返回每页的记录数
	 */
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	
	/**
	 * 设置每页的记录数
	 * @param rowsPerPage 每页的记录数
	 */
	public void setRowsPerPage(int rowsPerPage) {
		if (rowsPerPage <= 0)
			rowsPerPage = 50;
		this.rowsPerPage = rowsPerPage;
	}
	
	/**
	 * 获取开始的页码, 页码从 0 开始
	 * @return 返回开始的页码, 页码从 0 开始
	 */
	public int getStartPage() {
		return startPage;
	}
	
	/**
	 * 设置开始的页码, 页码从 0 开始
	 * @param startPage 开始的页码, 页码从 0 开始
	 */
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
}
