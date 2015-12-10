package com.hp.idc.itsm.dbo;

/**
 * ȡ����ʱ�ķ�ҳ��Ϣ
 * @author ÷԰
 *
 */
public class PageInfo {
	/**
	 * ��ʼ��ҳ��
	 */
	protected int startPage = 0;
	
	/**
	 * ��¼����
	 */
	protected int count = 0;
	
	/**
	 * ÿҳ�ļ�¼��
	 */
	protected int rowsPerPage = 50;
	
	/**
	 * ��ҳ��
	 */
	protected int pageCount;

	/**
	 * ��ȡ��ҳ��
	 * @return ������ҳ��
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * ������ҳ��
	 * @param pageCount ��ҳ��
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * ���¼���ҳ�뼰ҳ��
	 */
	public void recalc() {
		pageCount = (count - 1) / rowsPerPage + 1;
		if (startPage >= pageCount)
			startPage = pageCount - 1;
	}

	/**
	 * ��ȡ��¼����
	 * @return ���ؼ�¼����
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * ���ü�¼����
	 * @param count ��¼����
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * ��ȡÿҳ�ļ�¼��
	 * @return ����ÿҳ�ļ�¼��
	 */
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	
	/**
	 * ����ÿҳ�ļ�¼��
	 * @param rowsPerPage ÿҳ�ļ�¼��
	 */
	public void setRowsPerPage(int rowsPerPage) {
		if (rowsPerPage <= 0)
			rowsPerPage = 50;
		this.rowsPerPage = rowsPerPage;
	}
	
	/**
	 * ��ȡ��ʼ��ҳ��, ҳ��� 0 ��ʼ
	 * @return ���ؿ�ʼ��ҳ��, ҳ��� 0 ��ʼ
	 */
	public int getStartPage() {
		return startPage;
	}
	
	/**
	 * ���ÿ�ʼ��ҳ��, ҳ��� 0 ��ʼ
	 * @param startPage ��ʼ��ҳ��, ҳ��� 0 ��ʼ
	 */
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
}
