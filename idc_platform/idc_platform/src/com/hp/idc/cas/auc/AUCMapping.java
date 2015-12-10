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
 * ӳ�������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class AUCMapping {

	/**
	 * ��ȡ��Ӧ�ĵ�����ϵͳ���û�ID
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
	 * ��ȡ���еĶ�Ӧ�˻�
	 * @param userId ��ϵͳ�û�ID
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
	 * ��ȡ��Ӧ�ı�ϵͳ���û�ID
	 * 
	 * @param system
	 *            ϵͳ��ʶ
	 * @param thirdUserId
	 *            ������ϵͳ�û�ID
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
	 * ���ӳ���ϵ
	 * 
	 * @param mi
	 */
	public static void addMapping(AUCMappingInfo mi) {
		String insertSQL = "insert into cas_mapping(userId,thirdSystem,thirdUserId) values(?,?,?)";
		DBUtil.getJdbcTemplate().update(insertSQL,
				new Object[] { mi.getUserId(), mi.getThirdSystem(), mi.getThirdUserId() });
	}

	/**
	 * ����ӳ���ϵ
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
	 * ɾ��ӳ���ϵ
	 * 
	 * @param mi
	 */
	public static void deleteMapping(AUCMappingInfo mi) {
		String deleteSQL = "delete from cas_mapping where userId=? and thirdSystem=? and thirdUserId=?";
		DBUtil.getJdbcTemplate().update(deleteSQL,
				new Object[] { mi.getUserId(), mi.getThirdSystem(), mi.getThirdUserId() });

	}

}
