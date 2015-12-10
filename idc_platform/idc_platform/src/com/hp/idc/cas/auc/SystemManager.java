package com.hp.idc.cas.auc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.hp.idc.cas.common.CommonInfo;
import com.hp.idc.cas.common.DBUtil;

/**
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class SystemManager{

	public static Map<String,String> getConfig() throws SQLException{
		Map<String,String> ret = new HashMap<String,String>();
		String sql = "select * from cas_password_regulation";
		List<String[]> l= DBUtil.getJdbcTemplate().query(sql,new RowMapper<String[]>(){
			public String[] mapRow(ResultSet rs, int arg1) throws SQLException {
				String[] rets = new String[2];
				rets[0] = rs.getString("SYS_NAME");
				rets[1] = rs.getString("SYS_VALUE");
				return rets;
			}}
		);
		
		for (int i= 0; i < l.size(); i++) {
			String[] ss = (String[])l.get(i);
			if (ss != null && ss[0]!=null && !ss[0].equals("")) {
				ret.put(ss[0], ss[1]==null?"":ss[1]);
			}
		}
		return ret;
	}
	
	public static void updateConfig(final Map<String,String> config) throws SQLException{
		final Object[] key = config.keySet().toArray();
		final String insertSQL = "insert into cas_password_regulation(SYS_NAME,SYS_VALUE,SYS_DESC) values(?,?,?)";
		final String deleteSQL = "delete from cas_password_regulation where SYS_NAME=?";
		
		DBUtil.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				DBUtil.getJdbcTemplate().batchUpdate(deleteSQL, new BatchPreparedStatementSetter(){
					public int getBatchSize() {
						return key.length;
					}
					public void setValues(PreparedStatement ps, int arg1) throws SQLException {
						ps.setString(1, key[arg1].toString());
					}}
				);
				DBUtil.getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter(){
					public int getBatchSize() {
						return key.length;
					}
					public void setValues(PreparedStatement ps, int arg1) throws SQLException {
						ps.setString(1, key[arg1].toString());
						ps.setString(2, config.get(key[arg1].toString()));
						ps.setString(3, "");
					}}
				);
				
			}}
		);
		CommonInfo.SYS_CONFIG = config;
	}
}
