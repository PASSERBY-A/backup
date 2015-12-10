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
 * 系统配置页面
 * 
 * @author chengqp
 * 
 */
public class SystemConfig {
	/**
	 * 存储所有系统变量
	 */
	public static Map<String, String> Attribute = new HashMap<String, String>();

	protected static Log logger = LogFactory.getLog(SystemConfig.class);

	/**
	 * 初始化所有配置数据
	 */
	public static void initData() {
		reloadGlobalConfig();
	}

	/**
	 * 加载全局配置
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
	 * 更新系统配置属性
	 * 
	 * @param attrName
	 *            属性名
	 * @param attrValue
	 *            属性值
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
	 * 批量更新系统配置属性
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
	 * 是否是管理员
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
	 * 获取属性名对应的属性值
	 * 
	 * @param attrName 属性名
	 * @return 返回属性名对应的属性值
	 */
	public static String getAttributeValue(String attrName) {
		String ret = Attribute.get(attrName);
		if (ret != null)
			return ret;
		return "";
	}
	
	/**
	 * 修改
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
