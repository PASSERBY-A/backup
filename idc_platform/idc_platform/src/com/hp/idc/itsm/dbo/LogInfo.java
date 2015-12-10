package com.hp.idc.itsm.dbo;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录指定表中哪些字段在对应的删除、插入、更新时需要作操作记录
 * @author 梅园
 */
public class LogInfo {
	
	/**
	 * 本对象相关的表名
	 */
	protected String tableName;
	
	/**
	 * 相应表的描述
	 */
	protected String desc;
	
	/**
	 * 记录的 OID
	 */
	protected int oid;
	
	/**
	 * 主键列的列名
	 */
	protected String keyColumn;
	
	/**
	 * 插入时需要记日志的列名
	 */
	protected String[] insertColumns;
	
	/**
	 * 删除时需要记日志的列名
	 */
	protected String[] deleteColumns;
	
	/**
	 * 更新时需要记日志的列名
	 */
	protected String[] updateColumns;
	
	/**
	 * 返回在对指定的列进行指定操作时是否需要记操作日志
	 * @param columnName 要操作的列名
	 * @param operType 操作类型, "update", "insert", "delete"
	 * @return 需要记录时返回 true, 否则返回 false
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
	 * 将指定的字符串格式化为a,b,c的格式，多余的“,”和空格将被去除 
	 * @param str 要格式化的字符串
	 * @return 返回格式化后的字符串
	 */
	static public String formatString(String str) {
		String[] s = convertString(str);
		return formatString(s);
	}

	/**
	 * 将指定的字符串数组格式化为a,b,c的格式(以“,”分隔)
	 * @param s 要格式化的字符串数组
	 * @return 返回格式化后的字符串
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
	 * 将指定的以","分隔的字符串转换为相应的数组，空值将被排除
	 * @param str 要格式化的字符串
	 * @return 返回分隔后字符串数组
	 */
	static protected String[] convertString(String str) {
		if (str == null)
			return null;
		String[] ts = str.split(",");
		int count = 0;
		if (ts.length == 0)
			return null;
		
		// 过滤空值
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
	 * 获取在删除时需要记录日志的列名，以“,”分隔
	 * @return 返回删除时需要记录日志的列名，以“,”分隔
	 */
	public String getDeleteColumns() {
		return formatString(deleteColumns).toLowerCase();
	}
	
	/**
	 * 获取在删除时需要记录日志的列名列表
	 * @return 返回删除时需要记录日志的列名列表
	 */
	public List getDeleteList() {
		List l = new ArrayList();
		if (deleteColumns != null)
			for (int i = 0; i < deleteColumns.length; i++)
				l.add(deleteColumns[i]);
		return l;
	}

	/**
	 * 获取在插入时需要记录日志的列名列表
	 * @return 返回插入时需要记录日志的列名列表
	 */
	public List getInsertList() {
		List l = new ArrayList();
		if (insertColumns != null)
			for (int i = 0; i < insertColumns.length; i++)
				l.add(insertColumns[i]);
		return l;
	}

	/**
	 * 获取在更新时需要记录日志的列名列表
	 * @return 返回更新时需要记录日志的列名列表
	 */
	public List getUpdateList() {
		List l = new ArrayList();
		if (updateColumns != null)
			for (int i = 0; i < updateColumns.length; i++)
				l.add(updateColumns[i]);
		return l;
	}
	
	/**
	 * 获取表的描述
	 * @return 返回表的描述
	 */
	public String getDesc() {
		return desc;
	}
	
	/**
	 * 设置表的描述
	 * @param desc 指定的描述
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 获取在插入时需要记录日志的列名，以“,”分隔
	 * @return 返回插入时需要记录日志的列名，以“,”分隔
	 */
	public String getInsertColumns() {
		return formatString(insertColumns).toLowerCase();
	}
	
	/**
	 * 设置插入时需要记录日志的列名
	 * @param insertColumns 以“,”分隔的列名
	 */
	public void setInsertColumns(String insertColumns) {
		this.insertColumns = convertString(insertColumns);
	}
	
	/**
	 * 获取主键列的名称
	 * @return 返回主键列的名称
	 */
	public String getKeyColumn() {
		return keyColumn;
	}

	/**
	 * 设置主键列
	 * @param keyColumn 主键列的列名
	 */
	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn.toLowerCase();
	}
	
	/**
	 * 获取本对象的 OID
	 * @return 返回本对象的 OID
	 */
	public int getOid() {
		return oid;
	}
	
	/**
	 * 设置本对象的 OID
	 * @param oid 指定的OID
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}
	
	/**
	 * 获取本对象管理的表名
	 * @return 返回本对象管理的表名
	 */
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * 设置本对象管理的表名
	 * @param tableName 指定的表名
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName.toLowerCase();
	}
	
	/**
	 * 获取在更新时需要记录日志的列名，以“,”分隔
	 * @return 返回更新时需要记录日志的列名，以“,”分隔
	 */
	public String getUpdateColumns() {
		return formatString(updateColumns).toLowerCase();
	}
	
	/**
	 * 设置更新时需要记录日志的列名
	 * @param updateColumns 以“,”分隔的列名
	 */
	public void setUpdateColumns(String updateColumns) {
		this.updateColumns = convertString(updateColumns);
	}

	/**
	 * 设置删除时需要记录日志的列名
	 * @param deleteColumns 以“,”分隔的列名
	 */
	public void setDeleteColumns(String deleteColumns) {
		this.deleteColumns = convertString(deleteColumns);
	}
}
