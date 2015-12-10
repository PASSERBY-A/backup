package com.hp.idc.portal.mgr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.hp.idc.portal.bean.Favorites;
import com.hp.idc.portal.util.DBUtil;

public class FavoritesMgr {
	private static String SELECT_BY_OID = "SELECT * FROM tf_pt_favorites WHERE userId = ?";
	private static String INSERT_REGULAR = "insert into tf_pt_favorites (userId,menuIds) values (?,?)";
	private static String UPDATE_REGULAR = "update tf_pt_favorites set userId=?, menuIds=? where userId = ?";
	private static String DELETE_REGULAR = "delete from tf_pt_favorites where userId = ?";

	/**
	 * 添加
	 * 
	 * @param bean
	 * @return
	 */
	public boolean add(final Favorites bean) {
		boolean isSuccess = false;
		JdbcTemplate jdbcTemplate = DBUtil.getJdbcTemplate();
		int ret = jdbcTemplate.update(INSERT_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				//oid, title, name, url, alt, creator, createtime, isshow
				ps.setInt(1, bean.getUserId());
				ps.setString(2, bean.getMenuIds());
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
	public boolean update(final Favorites bean) {
		boolean isSuccess = false;
		if(getBeanByUserId(bean.getUserId())!=null){//如果记录不存在，插入该记录
			if(!add(bean)){
				return false;
			}else{
				return true;
			}
		}
		int ret = DBUtil.getJdbcTemplate().update(UPDATE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, bean.getUserId());
				ps.setString(2, bean.getMenuIds());
				ps.setInt(3, bean.getUserId());
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
	 * @param userId
	 * @return
	 */
	public boolean delete(final int userId) {
		boolean isSuccess = false;
		int o = DBUtil.getJdbcTemplate().update(DELETE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, userId);
			}
		});
		if (o > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 根据userId获取Favorites
	 * 
	 * @param userId
	 * @return
	 */
	public Favorites getBeanByUserId(int userId) {
		Favorites ret = DBUtil.getJdbcTemplate().query(SELECT_BY_OID, new Object[] { userId }, new ResultSetExtractor<Favorites>() {
			public Favorites extractData(ResultSet rs) throws SQLException, DataAccessException {
				Favorites temp = new Favorites();
				if (rs.next()) {
					//oid, title, name, url, alt, creator, createtime, isshow
					temp.setUserId(rs.getInt("userId"));
					temp.setMenuIds(rs.getString("menuIds"));
					return temp;
				}
				return temp;
			}
		});
		return ret;
	}
}
