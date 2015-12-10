package com.hp.idc.itsm.configure;

import java.util.List;

public class ViewColumnInfo {
	
	protected String nameEN;
	
	protected String nameZH;
	
	protected boolean sort;
	
	protected boolean group;
	
	protected int groupLevel;
	
	protected boolean defaultSort;
	
	//所属工单系统
	protected String origin;
	
	/**
	 * 列集合List[ColumnsInfo]
	 */
	private List columns;

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public String getNameZH() {
		return nameZH;
	}

	public void setNameZH(String nameZH) {
		this.nameZH = nameZH;
	}

	public boolean isSort() {
		return sort;
	}

	public void setSort(boolean sort) {
		this.sort = sort;
	}

	public boolean isGroup() {
		return group;
	}

	public void setGroup(boolean group) {
		this.group = group;
	}

	public int getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(int groupLevel) {
		this.groupLevel = groupLevel;
	}

	public List getColumns() {
		return columns;
	}

	public void setColumns(List columns) {
		this.columns = columns;
	}

	public boolean isDefaultSort() {
		return defaultSort;
	}

	public void setDefaultSort(boolean defaultSort) {
		this.defaultSort = defaultSort;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
}
