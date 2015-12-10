package com.hp.idc.itsm.dbo;


/**
 * 操作日志管理
 * @author 梅园
 */
public class LogManager {
	/**
	 * 获取指定表名的配置信息
	 * @param tableName 指定的表名
	 * @return 返回指定表名的配置信息
	 */
	static public LogInfo getLogInfo(String tableName) {
		return null;
	}
	
	/**
	 * 返回在对指定表名的指定列进行指定操作时是否需要记操作日志
	 * @param tableName 要操作的表名
	 * @param columnName 要操作的列名
	 * @param operType 操作类型, "update", "insert", "delete"
	 * @return 需要记录时返回 true, 否则返回 false
	 */
	public static boolean isLogable(String tableName, String columnName, String operType) {
		return false;
	}
}
