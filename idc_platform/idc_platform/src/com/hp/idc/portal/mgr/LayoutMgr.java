package com.hp.idc.portal.mgr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.portal.bean.*;
import com.hp.idc.portal.util.DBUtil;
import com.hp.idc.unitvelog.Log;
import com.hp.idc.unitvelog.manager.ILogManager;

/**
 * 布局管理操作类
 * 
 * @author chengqp
 * 
 */
public class LayoutMgr {
	private static Logger logger = Logger.getLogger(LayoutMgr.class);
	private ILogManager logManager = (ILogManager) ContextUtil.getBean("logManager");

	private static String SELECT_BY_OID = "SELECT * FROM tf_pt_layout WHERE oid = ? order by oid asc";
	private static String INSERT_REGULAR = "insert into tf_pt_layout (oid, name, path, areaNum) values (TF_PT_SEQ.NEXTVAL,?,?,?)";
	private static String UPDATE_REGULAR = "update tf_pt_layout set name = ?, path = ?, areaNum = ? where oid = ?";
	private static String DELETE_REGULAR = "delete from tf_pt_layout where oid = ?";

	/**
	 * 添加
	 * 
	 * @param bean
	 * @return
	 */
	public boolean add(final Layout bean,int userId) {
		boolean isSuccess = false;
		JdbcTemplate jdbcTemplate = DBUtil.getJdbcTemplate();
		int ret = jdbcTemplate.update(INSERT_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				// oid, title, name, url, alt, creator, createtime, isshow
				ps.setString(1, bean.getName());
				ps.setString(2, bean.getPath());
				ps.setInt(3, bean.getAreaNum());
			}
		});
		Log log = new Log();
		log.setAppType("portal");
		log.setModuleType("布局管理");
		log.setOperator(userId);
		if (ret > 0) {
			log.setOperatorResult(1);
			log.setContent("增加布局“"+bean.getName()+"”成功");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("布局管理："+userId+"增加布局“"+bean.getName()+"”成功");
			isSuccess = true;
		} else {
			log.setOperatorResult(0);
			log.setContent("增加布局“"+bean.getName()+"”失败");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("布局管理："+userId+"增加布局“"+bean.getName()+"”失败");
		}
		logManager.addLog(log);
		return isSuccess;
	}

	/**
	 * 修改
	 * 
	 * @param bean
	 * @return
	 */
	public boolean update(final Layout bean,int userId) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(UPDATE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, bean.getName());
				ps.setString(2, bean.getPath());
				ps.setInt(3, bean.getAreaNum());
				ps.setString(4, bean.getOid());
			}
		});
		Log log = new Log();
		log.setAppType("portal");
		log.setModuleType("布局管理");
		log.setOperator(userId);
		if (ret > 0) {
			log.setOperatorResult(1);
			log.setContent("修改布局“"+bean.getOid()+"”成功");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("布局管理："+userId+"修改布局“"+bean.getName()+"”成功");
			isSuccess = true;
		} else {
			log.setOperatorResult(0);
			log.setContent("修改布局“"+bean.getOid()+"”失败");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("布局管理："+userId+"修改布局“"+bean.getName()+"”失败");
		}
		logManager.addLog(log);
		return isSuccess;
	}

	/**
	 * 删除
	 * 
	 * @param oid
	 * @return
	 */
	public boolean delete(final String oid,int userId) {
		boolean isSuccess = false;
		int o = DBUtil.getJdbcTemplate().update(DELETE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, oid);
			}
		});
		Log log = new Log();
		log.setAppType("portal");
		log.setModuleType("布局管理");
		log.setOperator(userId);
		if (o > 0) {
			log.setOperatorResult(1);
			log.setContent("删除布局“"+oid+"”成功");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("布局管理："+userId+"删除布局“"+oid+"”成功");
			isSuccess = true;
		} else {
			log.setOperatorResult(0);
			log.setContent("删除布局“"+oid+"”失败");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("布局管理："+userId+"删除布局“"+oid+"”失败");
		}
		logManager.addLog(log);
		return isSuccess;
	}

	/**
	 * 根据oid获取Layout
	 * 
	 * @param oid
	 * @return
	 */
	public Layout getBeanById(String oid) {
		Layout ret = DBUtil.getJdbcTemplate().query(SELECT_BY_OID, new Object[] { oid },
				new ResultSetExtractor<Layout>() {
					public Layout extractData(ResultSet rs) throws SQLException, DataAccessException {
						Layout temp = new Layout();
						if (rs.next()) {
							// oid, title, name, url, alt, creator, createtime,
							// isshow
							temp.setOid(rs.getString("oid"));
							temp.setName(rs.getString("name"));
							temp.setPath(rs.getString("path"));
							temp.setAreaNum(rs.getInt("AreaNum"));
							return temp;
						}
						return null;
					}
				});
		return ret;
	}

	/**
	 * 根据所有Layout
	 * 
	 * @param userId
	 * @return
	 */
	public List<Layout> getBeans() {
		String sql = "SELECT * FROM tf_pt_layout order by oid asc";
		List<Layout> list = DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new LayoutRowMapper());
		return list;
	}

	private class LayoutRowMapper implements RowMapper<Layout> {
		public Layout mapRow(ResultSet rs, int rowNum) throws SQLException {
			Layout temp = new Layout();

			temp.setOid(rs.getString("oid"));
			temp.setName(rs.getString("name"));
			temp.setPath(rs.getString("path"));
			temp.setAreaNum(rs.getInt("AreaNum"));
			return temp;
		}
	}
}
