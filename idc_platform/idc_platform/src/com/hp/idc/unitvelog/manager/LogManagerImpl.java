/*
 * @(#)LogManagerImpl.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.unitvelog.manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.hp.idc.unitvelog.Log;
import com.hp.idc.unitvelog.LogType;

/**
 * 日志管理类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, May 17, 2011 2011
 * 
 */

public class LogManagerImpl extends JdbcDaoSupport implements ILogManager {

	/**
	 * log4j日志
	 */
	private static Logger logger = Logger.getLogger(LogManagerImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.unitvelog.manager.ILogManager#addLog(com.hp.idc.unitvelog.Log)
	 */
	@Override
	public int addLog(Log log) {
		// 进行插入日志数据检查操作，如日志异常，返回0，表示未进行日志的插入操作

		if (log.getBeginTime() < 0 || log.getBeginTime() < 0) {
			logger.error("统一日志插入失败>>>>>>>>>>>>>>>>日志开始时间或结束时间错误，请检查！！！");
			return 0;
		}

		if (log.getEndTime() > 0 && log.getBeginTime() > log.getEndTime()) {
			logger.error("统一日志插入失败>>>>>>>>>>>>>>>>日志开始时间不能大于结束时间，请检查！！！");
			return 0;
		}
		// 若无结束时间，则结束时间为开始时间
		if (log.getEndTime() < 0) {
			log.setEndTime(log.getBeginTime());
		}
		// 日志内容超过4000，返回
		if (log.getContent() != null && log.getContent().length() > 4000) {
			logger.error("统一日志插入失败>>>>>>>>>>>>>>>>日志内容太长(只能为4000)，请检查！！！");
			return 0;
		}

		int oid = getSequenceNextValue("TF_UL_SEQ");
		String sql = "INSERT INTO ULOG_TF_BASE_INFO VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		LogType lt = getLogType(log.getTypeOid());
		if (lt.getOid() == 0) {
			logger.error("统一日志插入失败>>>>>>>>>>>>>>>>错误的日志类型，请检查！！！");
			return 0;
		}

		final int param_oid = oid;
		final int param_logType = log.getTypeOid();
		final String param_name = lt.getName();
		final String param_appType = lt.getAppType();
		final String param_moduleType = lt.getModuleType();
		final Timestamp param_beginTime = new Timestamp(log.getBeginTime());
		final Timestamp param_endTime = new Timestamp(log.getEndTime());
		final String param_object = log.getObject();
		final String param_objectName = log.getObjectName();
		final int param_operator = log.getOperator();
		final int param_operatorType = log.getOperatorType();
		final int param_operatorResult = log.getOperatorResult();
		final String param_content = log.getContent();
		int result = getJdbcTemplate().update(sql,
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setInt(1, param_oid);
						ps.setInt(2, param_logType);
						ps.setString(3, param_name);
						ps.setString(4, param_appType);
						ps.setString(5, param_moduleType);
						ps.setTimestamp(6, param_beginTime);
						ps.setTimestamp(7, param_endTime);
						ps.setString(8, param_object);
						ps.setString(9, param_objectName);
						ps.setInt(10, param_operator);
						ps.setInt(11, param_operatorType);
						ps.setString(12, param_content);
						ps.setInt(13, param_operatorResult);
					}
				});
		if (log.getExtendInfo() != null && log.getExtendInfo().size() > 0) {
			addExtendInfo(log.getTypeOid(), log.getExtendInfo(), oid);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.idc.unitvelog.manager.ILogManager#addLogList(java.util.List)
	 */
	@Override
	public int addLogList(List<Log> logs) {
		final List<Log> logList = logs;

		final List<Integer> logTypes = new ArrayList<Integer>();
		final List<Map<String, String>> extendInfos = new ArrayList<Map<String, String>>();
		final List<Integer> logOids = new ArrayList<Integer>();

		// 批量验证
		for (Log log : logList) {
			if (log.getBeginTime() < 0 || log.getBeginTime() < 0) {
				logger.error("统一日志插入失败>>>>>>>>>>>>>>>>日志开始时间或结束时间错误，请检查！！！");
				return 0;
			}

			if (log.getBeginTime() > log.getEndTime()) {
				logger.error("统一日志插入失败>>>>>>>>>>>>>>>>日志开始时间不能大于结束时间，请检查！！！");
				return 0;
			}
			// 若无结束时间，则结束时间为开始时间
			if (log.getEndTime() < 0) {
				log.setEndTime(log.getBeginTime());
			}
			// 日志内容超过4000，返回
			if (log.getContent() != null && log.getContent().length() > 4000) {
				logger.error("统一日志插入失败>>>>>>>>>>>>>>>>日志内容太长(只能为4000)，请检查！！！");
				return 0;
			}

			LogType lt = getLogType(log.getTypeOid());
			if (lt.getOid() == 0) {
				logger.error("统一日志插入失败>>>>>>>>>>>>>>>>错误的日志类型，请检查！！！");
				return 0;
			}
		}
		String sql = "INSERT INTO ULOG_TF_BASE_INFO VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

		this.getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						LogType lt = getLogType(logList.get(i).getTypeOid());

						int param_oid = getSequenceNextValue("TF_UL_SEQ");
						int param_logType = logList.get(i).getTypeOid();
						String param_name = lt.getName();
						String param_appType = lt.getAppType();
						String param_moduleType = lt.getModuleType();
						Timestamp param_beginTime = new Timestamp(logList
								.get(i).getBeginTime());
						Timestamp param_endTime = new Timestamp(logList.get(i)
								.getEndTime());
						String param_object = logList.get(i).getObject();
						String param_objectName = logList.get(i)
								.getObjectName();
						int param_operator = logList.get(i).getOperator();
						int param_operatorType = logList.get(i)
								.getOperatorType();
						int param_operatorResult = logList.get(i)
								.getOperatorResult();
						String param_content = logList.get(i).getContent();

						// 批量操作扩张表
						logTypes.add(lt.getOid());
						extendInfos.add(logList.get(i).getExtendInfo());
						logOids.add(param_oid);

						ps.setInt(1, param_oid);
						ps.setInt(2, param_logType);
						ps.setString(3, param_name);
						ps.setString(4, param_appType);
						ps.setString(5, param_moduleType);
						ps.setTimestamp(6, param_beginTime);
						ps.setTimestamp(7, param_endTime);
						ps.setString(8, param_object);
						ps.setString(9, param_objectName);
						ps.setInt(10, param_operator);
						ps.setInt(11, param_operatorType);
						ps.setString(12, param_content);
						ps.setInt(13, param_operatorResult);

					}

					public int getBatchSize() {
						return logList.size();
					}
				});

		this.addExtendInfoList(logTypes, extendInfos, logOids);

		return logList.size();
	}

	/**
	 * 添加扩展表信息，logType对应的扩展表是ULOG_TF_EXTEND_INFO_日志类型oid
	 * 
	 * @param logType
	 *            日志类型oid
	 * @param extendInfo
	 *            扩展信息
	 * @return
	 */
	private int addExtendInfo(int logType, Map<String, String> extendInfo,
			int logOid) {
		String extendTab = "ULOG_TF_EXTEND_INFO_" + logType;
		StringBuffer fieldname = new StringBuffer("(UNI_LOG_OID");
		StringBuffer fieldvalue = new StringBuffer("(?");
		List<Object> param = new ArrayList<Object>();
		param.add(logOid);
		for (Map.Entry<String, String> entry : extendInfo.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			fieldname.append(" ," + key);
			fieldvalue.append(" ,?");
			param.add(value);
		}
		fieldname.append(")");
		fieldvalue.append(")");
		String sql = "INSERT INTO " + extendTab + fieldname.toString()
				+ " VALUES " + fieldvalue.toString();
		return getJdbcTemplate().update(sql, param.toArray());
	}

	/**
	 * 批量添加日志扩展信息
	 * 
	 * @param logTypes
	 *            日志类型集合
	 * @param extendInfos
	 *            日志扩展信息
	 * @param logOids
	 *            日志oid
	 * @return
	 */
	private int addExtendInfoList(List<Integer> logTypes,
			List<Map<String, String>> extendInfos, List<Integer> logOids) {

		// 保存扩展信息插入sql
		List<String> sqls = new ArrayList<String>();
		for (int i = 0; i < logTypes.size(); i++) {
			String extendTab = "ULOG_TF_EXTEND_INFO_" + logTypes.get(i);
			StringBuffer fieldname = new StringBuffer("(UNI_LOG_OID");
			StringBuffer fieldvalue = new StringBuffer("(" + logOids.get(i));
			Map<String, String> name_value = extendInfos.get(i);
			for (Map.Entry<String, String> entry : name_value.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				fieldname.append(" ," + key);
				fieldvalue.append(" ,'" + value + "'");
			}
			fieldname.append(")");
			fieldvalue.append(")");
			String sql = "INSERT INTO " + extendTab + fieldname.toString()
					+ " VALUES " + fieldvalue.toString();
			sqls.add(sql);
		}

		String[] t = new String[sqls.size()];
		return getJdbcTemplate().batchUpdate(sqls.toArray(t)).length;

	}

	/**
	 * 根据oid获取logtype
	 * 
	 * @param typeOid
	 * @return
	 */
	public LogType getLogType(int typeOid) {
		final LogType lt = new LogType();
		getJdbcTemplate().query("SELECT * FROM ULOG_TD_TYPE WHERE OID = ?",
				new Object[] { typeOid }, new RowCallbackHandler() {
					public void processRow(ResultSet rs) throws SQLException {
						lt.setOid(rs.getInt("OID"));
						lt.setName(rs.getString("NAME"));
						lt.setModuleType(rs.getString("MODULE_TYPE"));
						lt.setAppType(rs.getString("APP_TYPE"));
						lt.setOperatorType(rs.getString("OPERATOR_TYPE"));
					}
				});
		return lt;
	}

	/**
	 * 返回sequence下一个值
	 * 
	 * @param sequenceName
	 * @return
	 * @throws Exception
	 */
	private int getSequenceNextValue(String sequenceName) {
		String sql = "select " + sequenceName + ".nextval from dual";
		return getJdbcTemplate().queryForInt(sql);
	}
}
