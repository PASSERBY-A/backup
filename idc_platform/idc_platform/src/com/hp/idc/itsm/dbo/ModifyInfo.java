package com.hp.idc.itsm.dbo;

/**
 * 操作日志的信息
 * @author 梅园
 */
public class ModifyInfo {
	/**
	 * 操作人
	 */
	protected String operName;
	
	/**
	 * 表名
	 */
	protected String tableName;
	
	/**
	 * 列名
	 */
	protected String columnName;
	
	/**
	 * 原始值
	 */
	protected String fromValue;
	
	/**
	 * 操作类型
	 */
	protected String operType;
	
	/**
	 * 新值
	 */
	protected String toValue;
	
	/**
	 * 内容/主键
	 */
	protected String content;
	
	/**
	 * 值的最大长度
	 */
	private static final int MAX_SIZE = 1024;

	/**
	 * 使用指定的信息构造 ModifyInfo 对象
	 * @param operName 操作人
	 * @param operType 操作类型
	 * @param tableName 表名
	 * @param columnName 列名
	 * @param fromValue 原始值
	 * @param toValue 新值
	 * @param content 内容/主键
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
	 * 获取操作日志的列名
	 * @return 返回操作日志的列名
	 */
	public String getColumnName() {
		return columnName;
	}
	
	/**
	 * 设置操作日志的列名
	 * @param columnName 操作日志的列名
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	/**
	 * 获取操作日志的原始值
	 * @return 返回操作日志的原始值
	 */
	public String getFromValue() {
		if (fromValue != null && fromValue.length() > MAX_SIZE)
			return fromValue.substring(0, MAX_SIZE) + "...此处省略" 
				+ (fromValue.length() - MAX_SIZE) + "字.";
		return fromValue;
	}
	
	/**
	 * 设置操作日志的原始值
	 * @param fromValue 操作日志的原始值
	 */
	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}
	
	/**
	 * 获取操作日志的操作人
	 * @return 返回操作日志的操作人
	 */
	public String getOperName() {
		return operName;
	}
	
	/**
	 * 设置操作日志的操作人
	 * @param operName 操作日志的操作人
	 */
	public void setOperName(String operName) {
		this.operName = operName;
	}
	
	/**
	 * 获取操作日志的表名
	 * @return 返回操作日志的表名
	 */
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * 设置操作日志的表名
	 * @param tableName 操作日志的表名
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * 获取操作日志的新值
	 * @return 返回操作日志的新值
	 */
	public String getToValue() {
		if (toValue != null && toValue.length() > MAX_SIZE)
			return toValue.substring(0, MAX_SIZE) + "...此处省略" 
				+ (toValue.length() - MAX_SIZE) + "字.";
		return toValue;
	}
	
	/**
	 * 设置操作日志的新值
	 * @param toValue 操作日志的新值
	 */
	public void setToValue(String toValue) {
		this.toValue = toValue;
	}

	/**
	 * 获取操作日志的操作类型
	 * @return 返回操作日志的操作类型
	 */
	public String getOperType() {
		return operType;
	}

	/**
	 * 设置操作日志的操作类型
	 * @param operType 操作日志的操作类型
	 */
	public void setOperType(String operType) {
		this.operType = operType;
	}

	/**
	 * 获取操作日志的内容/主键
	 * @return 返回操作日志的内容/主键
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置操作日志的内容/主键
	 * @param content 操作日志的内容/主键
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
