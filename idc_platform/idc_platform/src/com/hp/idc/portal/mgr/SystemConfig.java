package com.hp.idc.portal.mgr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.hp.idc.portal.util.DBUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ϵͳ����ҳ��
 * 
 * @author chengqp
 * 
 */
public class SystemConfig {
	/**
	 * �洢����ϵͳ����
	 */
	public static Map<String, String> Attribute = new HashMap<String, String>();

	protected static Log logger = LogFactory.getLog(SystemConfig.class);

	/**
	 * ��ʼ��������������
	 */
	public static void initData() {
		reloadGlobalConfig();
	}

	/**
	 * ����ȫ������
	 */
	public static void reloadGlobalConfig() {
		logger.info("loading system config ...");
		String sql = "select * from TF_PT_SYSTEM";
		DBUtil.getJdbcTemplate().query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				Attribute.put(rs.getString("ATTR_NAME"), rs.getString("ATTR_VALUE"));
			}
		});
	}
	
	/**
	 * ����ϵͳ��������
	 * 
	 * @param attrName
	 *            ������
	 * @param attrValue
	 *            ����ֵ
	 */
	public static void updateAttribute(String attrName, String attrValue) {
		if (attrName == null || attrName.equals(""))
			return;
		if (attrValue == null || attrValue.equals(""))
			return;
		if(update(attrName, attrValue))
			Attribute.put(attrName, attrValue);
	}
	
	/**
	 * ��������ϵͳ��������
	 * 
	 * @param attr
	 */
	public static void updateAttribute(Map<String, String> attr) {
		if (attr == null)
			return;
		Set<String> key = attr.keySet();
		for (Iterator<String> ite = key.iterator(); ite.hasNext();) {
			String attrName = ite.next();
			if (attrName == null || attrName.equals(""))
				continue;
			String attrValue = attr.get(attrName);
			if (attrValue == null || attrValue.equals(""))
				continue;
			if(update(attrName, attrValue))
				Attribute.put(attrName, attrValue);
		}
	}
	
	/**
	 * �Ƿ��ǹ���Ա
	 * @param userId
	 * @return
	 */
	public static boolean isAdmin(String userId){
		if (userId== null || userId.equals(""))
			return false;
		String admin = getAttributeValue("admin");
		if (admin == null || admin.equals(""))
			return false;
		return admin.contains(","+ userId +",");
	}
	
	/**
	 * ��ȡ��������Ӧ������ֵ
	 * 
	 * @param attrName ������
	 * @return ������������Ӧ������ֵ
	 */
	public static String getAttributeValue(String attrName) {
		String ret = Attribute.get(attrName);
		if (ret != null)
			return ret;
		return "";
	}
	
	/**
	 * �޸�
	 * 
	 * @param bean
	 * @return
	 */
	public static boolean update(final String attrName, final String attrValue) {
		boolean isSuccess = false;
		String sql = "update TF_PT_SYSTEM set ATTR_NAME=?,ATTR_VALUE=? where ATTR_NAME = ?";
		int ret = DBUtil.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, attrName);
				ps.setString(2, attrValue);
				ps.setString(3, attrName);
			}
		});
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}
}
