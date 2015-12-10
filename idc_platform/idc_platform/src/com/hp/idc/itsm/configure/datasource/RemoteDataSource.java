package com.hp.idc.itsm.configure.datasource;

import com.hp.idc.itsm.configure.FieldDataSource;

public class RemoteDataSource extends FieldDataSource{

	protected String dataURL = "";
	/**
	 * 获取数据源的数据
	 * 
	 * @param filter
	 *            过滤表达式
	 * @param style
	 *            生成的数据样式
	 * @return 返回数据源的数据
	 */
	public String getData(String filter, String style) {
		return "";
	}
	
	public void load(String data) {
		this.dataURL = data;
	}
}
