package com.hp.idc.portal.mgr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.hp.idc.portal.bean.*;
import com.hp.idc.portal.util.DBUtil;

/**
 * 视图管理类
 * @author chengqp
 *
 */
public class ViewMgr {
	private static String SELECT_BY_OID = "SELECT * FROM tf_pt_view WHERE oid = ? order by oid desc";
	private static String INSERT_REGULAR = "insert into tf_pt_view (oid,name,layoutid,nodes,create_by,create_time) values (TF_PT_SEQ.NEXTVAL,?,?,?,?,?)";
	private static String UPDATE_REGULAR = "update tf_pt_view set name=?,layoutid=?,nodes=?,create_by=?,create_time=? where oid = ?";
	private static String DELETE_REGULAR = "delete from tf_pt_view where oid = ?";

	/**
	 * 添加操作
	 * 
	 * @param bean
	 * @return
	 */
	public  boolean add(final View bean) {
		boolean isSuccess = false;
		JdbcTemplate jdbcTemplate = DBUtil.getJdbcTemplate();
		int ret = jdbcTemplate.update(INSERT_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				//oid,name,layoutid,nodes,create_by,create_time
				ps.setString(1, bean.getName());
				ps.setString(2, bean.getLayoutId());
				ps.setString(3, bean.getNodes());
				ps.setInt(4, bean.getCreator());
				ps.setTimestamp(5, new Timestamp(bean.getCreateTime().getTime()));
			}
		});
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 修改操作
	 * 
	 * @param bean
	 * @return
	 */
	public  boolean update(final View bean) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(UPDATE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, bean.getName());
				ps.setString(2, bean.getLayoutId());
				ps.setString(3, bean.getNodes());
				ps.setInt(4, bean.getCreator());
				ps.setTimestamp(5, new Timestamp(bean.getCreateTime().getTime()));
				ps.setString(6, bean.getOid());
			}
		});
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 删除操作
	 * 
	 * @param oid
	 * @return
	 */
	public  boolean delete(final String oid) {
		boolean isSuccess = false;
		int o = DBUtil.getJdbcTemplate().update(DELETE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, oid);
			}
		});
		if (o > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 根据oid获取View
	 * 
	 * @param oid
	 * @return
	 */
	public  View getBeanById(String oid) {
		View ret = DBUtil.getJdbcTemplate().query(SELECT_BY_OID, new Object[] { oid }, new ResultSetExtractor<View>() {
			public View extractData(ResultSet rs) throws SQLException, DataAccessException {
				View temp = new View();
				if (rs.next()) {
					//oid,name,layoutid,nodes,create_by,create_time
					temp.setOid(rs.getString("oid"));
					temp.setName(rs.getString("name"));
					temp.setLayoutId(rs.getString("layoutId"));
					temp.setNodes(rs.getString("nodes"));
					temp.setCreator(rs.getInt("create_by"));
					temp.setCreateTime(rs.getTimestamp("create_time"));
					return temp;
				}
				return null;
			}
		});
		return ret;
	}

	/**
	 * 根据所有View
	 * 
	 * @param userId
	 * @return
	 */
	public  List<View> getBeans() {
		String sql = "SELECT * FROM tf_pt_view order by oid desc";
		List<View> list = DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new ViewRowMapper());
		return list;
	}

	/**
	 * 根据用户ID查询所有View
	 * 
	 * @param userId
	 *            用户名
	 * @param type
	 *            菜单类型
	 * @return
	 */
	public  List<View> getBeanByUserId(int userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tf_pt_view t where t.create_by = ?");
		sql.append(" order by t.oid asc");
		List<View> list = DBUtil.getJdbcTemplate().query(sql.toString(), new Object[] { userId }, new ViewRowMapper());
		return list;
	}

	private  class ViewRowMapper implements RowMapper<View> {
		public View mapRow(ResultSet rs, int rowNum) throws SQLException {
			View temp = new View();

			temp.setOid(rs.getString("oid"));
			temp.setName(rs.getString("name"));
			temp.setLayoutId(rs.getString("layoutId"));
			temp.setNodes(rs.getString("nodes"));
			temp.setCreator(rs.getInt("create_by"));
			temp.setCreateTime(rs.getTimestamp("create_time"));
			return temp;
		}
	}
}
