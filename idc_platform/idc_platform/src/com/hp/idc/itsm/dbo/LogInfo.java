package com.hp.idc.itsm.dbo;

import java.util.ArrayList;
import java.util.List;

/**
 * ��¼ָ��������Щ�ֶ��ڶ�Ӧ��ɾ�������롢����ʱ��Ҫ��������¼
 * @author ÷԰
 */
public class LogInfo {
	
	/**
	 * ��������صı���
	 */
	protected String tableName;
	
	/**
	 * ��Ӧ�������
	 */
	protected String desc;
	
	/**
	 * ��¼�� OID
	 */
	protected int oid;
	
	/**
	 * �����е�����
	 */
	protected String keyColumn;
	
	/**
	 * ����ʱ��Ҫ����־������
	 */
	protected String[] insertColumns;
	
	/**
	 * ɾ��ʱ��Ҫ����־������
	 */
	protected String[] deleteColumns;
	
	/**
	 * ����ʱ��Ҫ����־������
	 */
	protected String[] updateColumns;
	
	/**
	 * �����ڶ�ָ�����н���ָ������ʱ�Ƿ���Ҫ�ǲ�����־
	 * @param columnName Ҫ����������
	 * @param operType ��������, "update", "insert", "delete"
	 * @return ��Ҫ��¼ʱ���� true, ���򷵻� false
	 */
	public boolean isLogable(String columnName, String operType) {
		String[] s = null;
		if (operType.equals("update"))
			s = updateColumns;
		else if (operType.equals("insert"))
			s = insertColumns;
		else if (operType.equals("delete"))
			s = deleteColumns;
		if (s == null || s.length == 0)
			return false;
		for (int i = 0; i < s.length; i++) {
			if (s[i].equals(columnName))
				return true;
		}
		return false;
	}

	/**
	 * ��ָ�����ַ�����ʽ��Ϊa,b,c�ĸ�ʽ������ġ�,���Ϳո񽫱�ȥ�� 
	 * @param str Ҫ��ʽ�����ַ���
	 * @return ���ظ�ʽ������ַ���
	 */
	static public String formatString(String str) {
		String[] s = convertString(str);
		return formatString(s);
	}

	/**
	 * ��ָ�����ַ��������ʽ��Ϊa,b,c�ĸ�ʽ(�ԡ�,���ָ�)
	 * @param s Ҫ��ʽ�����ַ�������
	 * @return ���ظ�ʽ������ַ���
	 */
	static public String formatString(String[] s) {
		if (s == null || s.length == 0)
			return "";
		String r = s[0];
		for (int i = 1; i < s.length; i++)
			r += "," + s[i];
		return r;
	}

	/**
	 * ��ָ������","�ָ����ַ���ת��Ϊ��Ӧ�����飬��ֵ�����ų�
	 * @param str Ҫ��ʽ�����ַ���
	 * @return ���طָ����ַ�������
	 */
	static protected String[] convertString(String str) {
		if (str == null)
			return null;
		String[] ts = str.split(",");
		int count = 0;
		if (ts.length == 0)
			return null;
		
		// ���˿�ֵ
		for (int i = 0; i < ts.length; i++) {
			ts[i] = ts[i].trim();
			if (ts[i].length() == 0)
				continue;
			if (i != count)
				ts[count] = ts[i];
			count++;
		}
		if (count == 0)
			return null;
		if (count == ts.length)
			return ts;
		String[] ret = new String[count];
		for (int i = 0; i < count; i++)
			ret[i] = ts[i];
		return ret;
	}

	/**
	 * ��ȡ��ɾ��ʱ��Ҫ��¼��־���������ԡ�,���ָ�
	 * @return ����ɾ��ʱ��Ҫ��¼��־���������ԡ�,���ָ�
	 */
	public String getDeleteColumns() {
		return formatString(deleteColumns).toLowerCase();
	}
	
	/**
	 * ��ȡ��ɾ��ʱ��Ҫ��¼��־�������б�
	 * @return ����ɾ��ʱ��Ҫ��¼��־�������б�
	 */
	public List getDeleteList() {
		List l = new ArrayList();
		if (deleteColumns != null)
			for (int i = 0; i < deleteColumns.length; i++)
				l.add(deleteColumns[i]);
		return l;
	}

	/**
	 * ��ȡ�ڲ���ʱ��Ҫ��¼��־�������б�
	 * @return ���ز���ʱ��Ҫ��¼��־�������б�
	 */
	public List getInsertList() {
		List l = new ArrayList();
		if (insertColumns != null)
			for (int i = 0; i < insertColumns.length; i++)
				l.add(insertColumns[i]);
		return l;
	}

	/**
	 * ��ȡ�ڸ���ʱ��Ҫ��¼��־�������б�
	 * @return ���ظ���ʱ��Ҫ��¼��־�������б�
	 */
	public List getUpdateList() {
		List l = new ArrayList();
		if (updateColumns != null)
			for (int i = 0; i < updateColumns.length; i++)
				l.add(updateColumns[i]);
		return l;
	}
	
	/**
	 * ��ȡ�������
	 * @return ���ر������
	 */
	public String getDesc() {
		return desc;
	}
	
	/**
	 * ���ñ������
	 * @param desc ָ��������
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * ��ȡ�ڲ���ʱ��Ҫ��¼��־���������ԡ�,���ָ�
	 * @return ���ز���ʱ��Ҫ��¼��־���������ԡ�,���ָ�
	 */
	public String getInsertColumns() {
		return formatString(insertColumns).toLowerCase();
	}
	
	/**
	 * ���ò���ʱ��Ҫ��¼��־������
	 * @param insertColumns �ԡ�,���ָ�������
	 */
	public void setInsertColumns(String insertColumns) {
		this.insertColumns = convertString(insertColumns);
	}
	
	/**
	 * ��ȡ�����е�����
	 * @return ���������е�����
	 */
	public String getKeyColumn() {
		return keyColumn;
	}

	/**
	 * ����������
	 * @param keyColumn �����е�����
	 */
	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn.toLowerCase();
	}
	
	/**
	 * ��ȡ������� OID
	 * @return ���ر������ OID
	 */
	public int getOid() {
		return oid;
	}
	
	/**
	 * ���ñ������ OID
	 * @param oid ָ����OID
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}
	
	/**
	 * ��ȡ���������ı���
	 * @return ���ر��������ı���
	 */
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * ���ñ��������ı���
	 * @param tableName ָ���ı���
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName.toLowerCase();
	}
	
	/**
	 * ��ȡ�ڸ���ʱ��Ҫ��¼��־���������ԡ�,���ָ�
	 * @return ���ظ���ʱ��Ҫ��¼��־���������ԡ�,���ָ�
	 */
	public String getUpdateColumns() {
		return formatString(updateColumns).toLowerCase();
	}
	
	/**
	 * ���ø���ʱ��Ҫ��¼��־������
	 * @param updateColumns �ԡ�,���ָ�������
	 */
	public void setUpdateColumns(String updateColumns) {
		this.updateColumns = convertString(updateColumns);
	}

	/**
	 * ����ɾ��ʱ��Ҫ��¼��־������
	 * @param deleteColumns �ԡ�,���ָ�������
	 */
	public void setDeleteColumns(String deleteColumns) {
		this.deleteColumns = convertString(deleteColumns);
	}
}
