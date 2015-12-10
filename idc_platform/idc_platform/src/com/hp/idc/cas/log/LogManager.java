package com.hp.idc.cas.log;

import java.sql.Date;
import java.util.List;

import com.hp.idc.cas.common.DBUtil;

public class LogManager {

	/**
	 * 增加操作日志
	 * 
	 * @param operUser 操作人
	 * @param operIp 操作人IP
	 * @param operType 操作类型
	 * @param operObjId 操作对象ID
	 * @param operObjName 操作对象名称
	 * @param content 操作具体描述
	 * @throws Exception
	 */
	public static void addLog(String operUser, String operIp, String operType, String operObjId,
			String operObjName, String content) throws Exception {
		String sql = "insert into cas_log(oid,oper_user,oper_time,oper_ip,oper_obj_id,oper_obj_name,oper_type,content) values(?,?,?,?,?,?,?,?)";
		long oid = getLogOid();
		DBUtil.getJdbcTemplate().update(
				sql,
				new Object[] { oid, operUser, new Date(System.currentTimeMillis()), operIp, operObjId,
						operObjName, operType, content });

	}

	public static List<LogInfo> getLog() {
		return null;
	}

	/**
	 * 获取日志OID
	 * @return
	 * @throws Exception
	 */
	public static long getLogOid() throws Exception {
		String sql = "select seq_cas_log.nextval oid from dual";
		long ret = DBUtil.getJdbcTemplate().queryForLong(sql);
		return ret;
	}

}
