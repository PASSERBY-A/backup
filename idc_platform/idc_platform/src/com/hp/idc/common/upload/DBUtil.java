package com.hp.idc.common.upload;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 数据库管理类,采用spring的数据库访问对象
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 8:11:57 AM Aug 23, 2011
 * 
 */
public class DBUtil {

	private static JdbcTemplate jdbcTemplate;

	private static TransactionTemplate transactionTemplate;

	private static LobHandler defaultLobHandler;

	/**
	 * 获取下一个OID
	 * 
	 * @param module
	 * @return
	 */
	public static long getNextOid(final String module) {
		final String sql_select = "select value from kbm_sequence where module=?";
		final String sql_update = "update kbm_sequence set value=value+1 where module=?";
		Long ret = (Long) transactionTemplate
				.execute(new TransactionCallback<Object>() {

					public Long doInTransaction(TransactionStatus arg0) {
						long ret = 0;
						ret = jdbcTemplate.queryForLong(sql_select,
								new Object[] { module });
						jdbcTemplate
								.update(sql_update, new Object[] { module });
						return ret + 1;
					}
				});
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

	public void setDefaultLobHandler(LobHandler defaultLobHandler) {
		DBUtil.defaultLobHandler = defaultLobHandler;
	}

	public LobHandler getDefaultLobHandler() {
		return defaultLobHandler;
	}

}
