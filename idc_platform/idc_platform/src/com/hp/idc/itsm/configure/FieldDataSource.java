package com.hp.idc.itsm.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��ʾ�ֶε�����Դ
 * 
 * @author ÷԰
 * 
 */
public class FieldDataSource {
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
		// ������������д�˺���
		return "";
	}
	
	/**
	 * 
	 * @param filter
	 * @param style �������ݵ���ʽ���б���"list"��������״"tree"
	 * @param idType ���ɵ�node�ڵ��id��ȡOID������ȡID<br>
	 * ������������������ID��Ҳ��OID���˲��������ƶ���ID������OID�����ɽڵ��ID<br>
	 * ȡֵ��"OID","ID"
	 * @return
	 */
	public String getData(String filter, String style, String idType){
		return getData(filter,style);
	}

	/**
	 * ��������Դ��������Ϣ
	 * 
	 * @param data
	 *            ����Դ��������Ϣ
	 */
	public void load(String data) {
		// ������������д�˺���
	}

	/**
	 * ��ȡ����������������м�ֵ
	 * 
	 * @param filter
	 *            ���˱��ʽ
	 * @return ��������������������м�ֵ List<String>
	 */
	public List getKeys(String filter) {
		return new ArrayList();
	}

	/**
	 * ��ȡָ����ֵ����ʾ����
	 * @param id ��ֵ
	 * @return ����ָ����ֵ����ʾ����
	 */
	public String getDisplayText(String id) {
		// ������������д�˺���
		return id;
	}
	
	public String getIdByText(String text) {
		// ������������д�˺���
		return text;
	}
	/**
	 * 
	 * @param id
	 * @param idType id�����ͣ�ȡֵΪID/OID
	 * @return
	 */
	public String getDisplayText(String id,String idType) {
		return getDisplayText(id);
	}

	/**
	 * ����������������������FieldDataSource��ʵ��
	 * @param type ����,��com.hp.idc.itsm.configure.datasource.TextDataSource
	 * @param data ��������
	 * @return �������ɵ�ʵ��
	 */
	static public FieldDataSource create(String type, String data) {
		FieldDataSource ds = null;
		try {
			Class c = Class.forName(type);
			ds = (FieldDataSource) c.newInstance();
			ds.load(data);
			return ds;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ����Դ���͵��б�
	 * @return ��������Դ���͵��б�Map<String(����), String(����)>
	 */
	static public Map getDataSourceTypes() {
		Map m = new HashMap();
		m.put("com.hp.idc.itsm.configure.datasource.TextDataSource",
				"���ָ������ı�����");
		m.put("com.hp.idc.itsm.configure.datasource.CodeDataSource",
				"��������");
		m.put("com.hp.idc.itsm.configure.datasource.CIDataSource",
				"����������");
		// m.put("com.hp.idc.itsm.configure.datasource.CodeTypeDataSource",
		// "�����������");
		return m;
	}

	/**
	 * ��ȡ�Էָ����ָ�����������Դ�����б���ַ���
	 * @return �����Էָ����ָ�����������Դ�����б���ַ���
	 */
	static public String getDataSourceTypeText() {
		Map m = getDataSourceTypes();
		Object objs[] = m.keySet().toArray();
		String ret = "";
		for (int i = 0; i < objs.length; i++) {
			if (i > 0)
				ret += "|";
			ret += objs[i].toString() + ";" + m.get(objs[i]).toString();
		}
		return ret;
	}
}
