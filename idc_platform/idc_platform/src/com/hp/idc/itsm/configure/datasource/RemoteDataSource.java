package com.hp.idc.itsm.configure.datasource;

import com.hp.idc.itsm.configure.FieldDataSource;

public class RemoteDataSource extends FieldDataSource{

	protected String dataURL = "";
	/**
	 * ��ȡ����Դ������
	 * 
	 * @param filter
	 *            ���˱��ʽ
	 * @param style
	 *            ���ɵ�������ʽ
	 * @return ��������Դ������
	 */
	public String getData(String filter, String style) {
		return "";
	}
	
	public void load(String data) {
		this.dataURL = data;
	}
}
