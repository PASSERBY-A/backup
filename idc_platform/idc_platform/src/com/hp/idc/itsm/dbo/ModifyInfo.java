package com.hp.idc.itsm.dbo;

/**
 * ������־����Ϣ
 * @author ÷԰
 */
public class ModifyInfo {
	/**
	 * ������
	 */
	protected String operName;
	
	/**
	 * ����
	 */
	protected String tableName;
	
	/**
	 * ����
	 */
	protected String columnName;
	
	/**
	 * ԭʼֵ
	 */
	protected String fromValue;
	
	/**
	 * ��������
	 */
	protected String operType;
	
	/**
	 * ��ֵ
	 */
	protected String toValue;
	
	/**
	 * ����/����
	 */
	protected String content;
	
	/**
	 * ֵ����󳤶�
	 */
	private static final int MAX_SIZE = 1024;

	/**
	 * ʹ��ָ������Ϣ���� ModifyInfo ����
	 * @param operName ������
	 * @param operType ��������
	 * @param tableName ����
	 * @param columnName ����
	 * @param fromValue ԭʼֵ
	 * @param toValue ��ֵ
	 * @param content ����/����
	 */
	public ModifyInfo(String operName, String operType, String tableName, String columnName,
			String fromValue, String toValue, String content) {
		this.operName = operName;
		this.operType = operType;
		this.tableName = tableName;
		this.columnName = columnName;
		this.fromValue = fromValue;
		this.toValue = toValue;
		this.content = content;
	}
	
	/**
	 * ��ȡ������־������
	 * @return ���ز�����־������
	 */
	public String getColumnName() {
		return columnName;
	}
	
	/**
	 * ���ò�����־������
	 * @param columnName ������־������
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	/**
	 * ��ȡ������־��ԭʼֵ
	 * @return ���ز�����־��ԭʼֵ
	 */
	public String getFromValue() {
		if (fromValue != null && fromValue.length() > MAX_SIZE)
			return fromValue.substring(0, MAX_SIZE) + "...�˴�ʡ��" 
				+ (fromValue.length() - MAX_SIZE) + "��.";
		return fromValue;
	}
	
	/**
	 * ���ò�����־��ԭʼֵ
	 * @param fromValue ������־��ԭʼֵ
	 */
	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}
	
	/**
	 * ��ȡ������־�Ĳ�����
	 * @return ���ز�����־�Ĳ�����
	 */
	public String getOperName() {
		return operName;
	}
	
	/**
	 * ���ò�����־�Ĳ�����
	 * @param operName ������־�Ĳ�����
	 */
	public void setOperName(String operName) {
		this.operName = operName;
	}
	
	/**
	 * ��ȡ������־�ı���
	 * @return ���ز�����־�ı���
	 */
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * ���ò�����־�ı���
	 * @param tableName ������־�ı���
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * ��ȡ������־����ֵ
	 * @return ���ز�����־����ֵ
	 */
	public String getToValue() {
		if (toValue != null && toValue.length() > MAX_SIZE)
			return toValue.substring(0, MAX_SIZE) + "...�˴�ʡ��" 
				+ (toValue.length() - MAX_SIZE) + "��.";
		return toValue;
	}
	
	/**
	 * ���ò�����־����ֵ
	 * @param toValue ������־����ֵ
	 */
	public void setToValue(String toValue) {
		this.toValue = toValue;
	}

	/**
	 * ��ȡ������־�Ĳ�������
	 * @return ���ز�����־�Ĳ�������
	 */
	public String getOperType() {
		return operType;
	}

	/**
	 * ���ò�����־�Ĳ�������
	 * @param operType ������־�Ĳ�������
	 */
	public void setOperType(String operType) {
		this.operType = operType;
	}

	/**
	 * ��ȡ������־������/����
	 * @return ���ز�����־������/����
	 */
	public String getContent() {
		return content;
	}

	/**
	 * ���ò�����־������/����
	 * @param content ������־������/����
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
