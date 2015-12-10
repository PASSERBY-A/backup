package com.hp.idc.portal.mgr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.hp.idc.portal.bean.MenuParams;
import com.hp.idc.portal.util.DBUtil;

/**
 * 菜单管理类
 * @author chengqp
 *
 */
public class MenuParamsMgr {
	private static String SELECT_BY_OID = "SELECT * FROM TF_PT_MENUPARAMS WHERE userId = ? and menuId = ?";
	private static String INSERT_REGULAR = "insert into TF_PT_MENUPARAMS values (?,?,?)";
	private static String UPDATE_REGULAR = "update TF_PT_MENUPARAMS set userId = ?, menuId = ?, PARAMS = ? where userId = ? and menuId = ?";
	private static String DELETE_REGULAR = "delete from TF_PT_MENUPARAMS where userId = ? and menuId = ?";

	/**
	 * 修改
	 * 
	 * @param bean
	 * @return
	 */
	public static boolean add(final MenuParams bean) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(INSERT_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, bean.getUserId());
				ps.setInt(2, bean.getMenuId());
				ps.setString(3, bean.getParams());
			}
		});
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}
	
	/**
	 * 修改
	 * 
	 * @param bean
	 * @return
	 */
	public static boolean update(final MenuParams bean) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(UPDATE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, bean.getUserId());
				ps.setInt(2, bean.getMenuId());
				ps.setString(3, bean.getParams());
				ps.setInt(4, bean.getUserId());
				ps.setInt(5, bean.getMenuId());
			}
		});
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 删除
	 * 
	 * @param userId 用户ID
	 * @param menuId 菜单ID
	 * @return
	 */
	public static boolean delete(final int userId,final int menuId) {
		boolean isSuccess = false;
		int o = DBUtil.getJdbcTemplate().update(DELETE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, userId);
				ps.setInt(2, menuId);
			}
		});
		if (o > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 根据用户ID和菜单ID获得菜单参数
	 * @param userId 用户ID
	 * @param menuId 菜单ID
	 * @return
	 */
	public static MenuParams getBeanById(final int userId,final int menuId) {
		MenuParams ret = DBUtil.getJdbcTemplate().query(SELECT_BY_OID, new Object[] { userId, menuId}, new ResultSetExtractor<MenuParams>() {
			public MenuParams extractData(ResultSet rs) throws SQLException, DataAccessException {
				MenuParams temp = new MenuParams();
				if (rs.next()) {
					temp.setUserId(rs.getInt("userId"));
					temp.setMenuId(rs.getInt("menuId"));
					temp.setParams(rs.getString("params"));
					return temp;
				}
				return null;
			}
		});
		return ret;
	}
}
