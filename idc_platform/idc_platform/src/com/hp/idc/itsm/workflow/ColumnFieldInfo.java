package com.hp.idc.itsm.workflow;

import com.hp.idc.itsm.dbo.ColumnData;

public class ColumnFieldInfo {

	private String fieldName = "";
	
	private String columnName = "";
	
	/**
	 * 参见：ColumnData.type
	 */
	private char type;
	
	private int length = -1;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}
	
	public void setType(String type) {
		if (type == null || type.equals(""))
			this.type = ColumnData.TYPE_STRING;
		else if (type.equalsIgnoreCase("NULL"))
			this.type = ColumnData.TYPE_NULL;
		else if (type.equalsIgnoreCase("INTEGER"))
			this.type = ColumnData.TYPE_INTEGER;
		else if (type.equalsIgnoreCase("STRING"))
			this.type = ColumnData.TYPE_STRING;
		else if (type.equalsIgnoreCase("DATE"))
			this.type = ColumnData.TYPE_DATE;
		else if (type.equalsIgnoreCase("DATETIME"))
			this.type = ColumnData.TYPE_DATETIME;
		else
			System.out.println("不兼容类型:"+type);
	}
}
