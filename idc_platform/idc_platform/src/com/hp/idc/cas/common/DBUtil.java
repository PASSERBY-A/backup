/**
 * 
 */
package com.hp.idc.cas.common;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 
 * 数据库管理类
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class DBUtil {

	private static JdbcTemplate jdbcTemplate;
	
	private static TransactionTemplate transactionTemplate;
	
	//private static LobHandler lobHandler;
	
	/**
	 * 获取下一个OID
	 * @param module
	 * @return
	 */
	public static long getNextOid() {
		final String sql_select = "select seq_cas_auc.nextval oid from dual";
		long ret = jdbcTemplate.queryForLong(sql_select);
		return ret;
	}
	
	public static JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		DBUtil.jdbcTemplate = jdbcTemplate;
	}
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		DBUtil.transactionTemplate = transactionTemplate;
	}
	public static TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}
	
//	public void setLobHandler(LobHandler lobHandler) {
//		DBUtil.lobHandler = lobHandler;
//	}
//	public static LobHandler getLobHandler() {
//		return lobHandler;
//	}

}
