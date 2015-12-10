/**
 * 
 */
package com.hp.idc.cas.auc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.hp.idc.cas.common.DBUtil;

/**
 * 映射管理类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class AUCMapping {

	/**
	 * 获取对应的第三方系统的用户ID
	 * 
	 * @param userId
	 * @param system
	 * @return
	 */
	public static List<AUCMappingInfo> getThirdUserId(String userId, String system) {
		String sql = "select * from cas_mapping where userId=? and thirdSystem=?";
		List<AUCMappingInfo> ret = DBUtil.getJdbcTemplate().query(sql, new Object[] { userId, system },
				new RowMapper<AUCMappingInfo>() {
					public AUCMappingInfo mapRow(ResultSet rs, int arg1) throws SQLException {
						AUCMappingInfo ret = getInfoFromResultSet(rs);
						return ret;
					}
				});
		return ret;
	}

	/**
	 * 获取所有的对应账户
	 * @param userId 本系统用户ID
	 * @return
	 */
	public static List<AUCMappingInfo> getThirdUserId(String userId) {
		
		String sql = "select * from cas_mapping where userId=?";
		List<AUCMappingInfo> ret = DBUtil.getJdbcTemplate().query(sql, new Object[] { userId },
				new RowMapper<AUCMappingInfo>() {
					public AUCMappingInfo mapRow(ResultSet rs, int arg1) throws SQLException {
						AUCMappingInfo ret = getInfoFromResultSet(rs);
						return ret;
					}
				});
		return ret;
	}

	/**
	 * 获取对应的本系统的用户ID
	 * 
	 * @param system
	 *            系统标识
	 * @param thirdUserId
	 *            第三方系统用户ID
	 * @return
	 */
	public static List<AUCMappingInfo> getUserId(String system, String thirdUserId) {
		String sql = "select * from cas_mapping where thirdSystem=? and thirdUserId=?";
		List<AUCMappingInfo> ret = (List<AUCMappingInfo>) DBUtil.getJdbcTemplate().query(sql,
				new Object[] { system, thirdUserId }, new RowMapper<AUCMappingInfo>() {
					public AUCMappingInfo mapRow(ResultSet rs, int arg1) throws SQLException {
						AUCMappingInfo ret = getInfoFromResultSet(rs);
						return ret;
					}
				});
		return ret;
	}

	private static AUCMappingInfo getInfoFromResultSet(ResultSet rs) throws SQLException {
		AUCMappingInfo mi = new AUCMappingInfo();
		mi.setUserId(rs.getString("userId"));
		mi.setThirdSystem(rs.getString("thirdSystem"));
		mi.setThirdUserId(rs.getString("thirdUserId"));
		return mi;
	}

	/**
	 * 添加映射关系
	 * 
	 * @param mi
	 */
	public static void addMapping(AUCMappingInfo mi) {
		String insertSQL = "insert into cas_mapping(userId,thirdSystem,thirdUserId) values(?,?,?)";
		DBUtil.getJdbcTemplate().update(insertSQL,
				new Object[] { mi.getUserId(), mi.getThirdSystem(), mi.getThirdUserId() });
	}

	/**
	 * 更新映射关系
	 * 
	 * @param mi
	 */
	public static void updateMapping(final AUCMappingInfo mi)throws Exception {

		DBUtil.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				deleteMapping(mi);
				addMapping(mi);
			}
		});
	}

	/**
	 * 删除映射关系
	 * 
	 * @param mi
	 */
	public static void deleteMapping(AUCMappingInfo mi) {
		String deleteSQL = "delete from cas_mapping where userId=? and thirdSystem=? and thirdUserId=?";
		DBUtil.getJdbcTemplate().update(deleteSQL,
				new Object[] { mi.getUserId(), mi.getThirdSystem(), mi.getThirdUserId() });

	}

}
