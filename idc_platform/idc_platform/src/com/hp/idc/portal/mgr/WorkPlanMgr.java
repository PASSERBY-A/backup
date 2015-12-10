package com.hp.idc.portal.mgr;

import java.sql.*;
import java.util.*;
import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;

import com.hp.idc.portal.bean.WorkPlan;
import com.hp.idc.portal.util.DBUtil;

public class WorkPlanMgr {
	private static String SELECT_BY_OID = "SELECT * FROM tf_pt_workplan WHERE oid = ? order by oid desc";
	private static String INSERT_REGULAR = "insert into tf_pt_workplan (oid,userid,title,type,plantime,finishtime,createtime,description) values (TF_PT_SEQ.NEXTVAL,?,?,?,?,?,?,?)";
	private static String UPDATE_REGULAR = "update tf_pt_workplan set userid=?,title=?,type=?,plantime=?,finishtime=?,createtime=?,description=? where oid = ?";
	private static String DELETE_REGULAR = "delete from tf_pt_workplan where oid = ?";

	/**
	 * 添加
	 * @param bean
	 * @return
	 */
	public boolean add(final WorkPlan bean) {
		boolean isSuccess = false;
		JdbcTemplate jdbcTemplate = DBUtil.getJdbcTemplate();
		int ret = jdbcTemplate.update(INSERT_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, bean.getUserId());
				ps.setString(2, bean.getTitle());
				ps.setString(3, bean.getType());
				ps.setTimestamp(4, new Timestamp(bean.getPlanTime().getTime()));
				ps.setTimestamp(5, bean.getFinishTime()==null?null:new Timestamp(bean.getFinishTime().getTime()));
				ps.setTimestamp(6, new Timestamp(bean.getCreateTime().getTime()));
				ps.setString(7, bean.getDescription());
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
	public boolean update(final WorkPlan bean) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(UPDATE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, bean.getUserId());
				ps.setString(2, bean.getTitle());
				ps.setString(3, bean.getType());
				ps.setTimestamp(4, new Timestamp(bean.getPlanTime().getTime()));
				ps.setTimestamp(5, bean.getFinishTime()==null?null:new Timestamp(bean.getFinishTime().getTime()));
				ps.setTimestamp(6, new Timestamp(bean.getCreateTime().getTime()));
				ps.setString(7, bean.getDescription());
				ps.setString(8, bean.getOid());
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
	 * @param oid
	 * @return
	 */
	public boolean delete(final String oid) {
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
	 * 根据oid获取WorkPlan
	 * 
	 * @param oid
	 * @return
	 */
	public WorkPlan getBeanById(String oid) {
		WorkPlan ret = DBUtil.getJdbcTemplate().query(SELECT_BY_OID, new Object[] { oid },
				new ResultSetExtractor<WorkPlan>() {
					public WorkPlan extractData(ResultSet rs) throws SQLException, DataAccessException {
						WorkPlan temp = new WorkPlan();
						if (rs.next()) {
							temp.setOid(rs.getString("oid"));
							temp.setUserId(rs.getInt("userId"));
							temp.setTitle(rs.getString("title"));
							temp.setType(rs.getString("type"));
							temp.setPlanTime(rs.getTimestamp("planTime"));
							if(null==rs.getTimestamp("finishTime"))
								temp.setFinishTime(null);
							else
								temp.setFinishTime(rs.getTimestamp("finishTime"));
							temp.setCreateTime(rs.getTimestamp("createTime"));
							temp.setDescription(rs.getString("description"));
							return temp;
						}
						return null;
					}
				});
		return ret;
	}

	/**
	 * 根据用户ID查询所有WorkPlan
	 * 
	 * @param userId
	 * @return
	 */
	public List<WorkPlan> getBeanByUserId(int userId) {
		String sql = "SELECT * FROM tf_pt_WorkPlan WHERE userId = ? order by oid desc";
		List<WorkPlan> list = DBUtil.getJdbcTemplate().query(sql, new Object[] { userId },
				new WorkPlanRowMapper());
		return list;
	}
	/**
	 * 根据用户ID查询所有WorkPlan
	 * 
	 * @param userId
	 * @return
	 */
	public List<WorkPlan> getBeanByFilter(int userId,String type,String keyWords) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tf_pt_workplan t WHERE t.userid = ?");
		if(!"-1".equals(type)){
			sql.append(" and t.type='"+type+"'");
		}
		if(!"".equals(keyWords)){
			sql.append(" and instr(t.title,'"+keyWords+"')>0");
		}
		sql.append(" order by t.oid desc");
		List<WorkPlan> list = DBUtil.getJdbcTemplate().query(sql.toString(), new Object[] { userId},
				new WorkPlanRowMapper());
		return list;
	}
	
	/**
	 * 获得当前用户指定日期后的工作计划
	 * @param user	短信接收人
	 * @param date	
	 * @return
	 */
	public List<WorkPlan> getWorkPlanTopData(int user,Date date){
		final String sql = "select * from tf_pt_workplan where userid=? and to_char(plantime,'yyyy-mm-dd') in(select * from (select to_char(t.plantime,'yyyy-mm-dd') from tf_pt_workplan t where t.plantime>? and userid=? and finishtime is null group by to_char(t.plantime,'yyyy-mm-dd')) where rownum<5)"; 
        Object[] params = new Object[] { user, date, user};
        List<WorkPlan> list = DBUtil.getJdbcTemplate().query(sql.toString(), params, new WorkPlanRowMapper());
		return list;
	}
	
	private class WorkPlanRowMapper implements RowMapper<WorkPlan> {
		public WorkPlan mapRow(ResultSet rs, int rowNum) throws SQLException {
			WorkPlan temp = new WorkPlan();
			
			temp.setOid(rs.getString("oid"));
			temp.setUserId(rs.getInt("userId"));
			temp.setTitle(rs.getString("title"));
			temp.setType(rs.getString("type"));
			temp.setPlanTime(rs.getTimestamp("planTime"));
			if(null==rs.getTimestamp("finishTime"))
				temp.setFinishTime(null);
			else
				temp.setFinishTime(rs.getTimestamp("finishTime"));
			temp.setCreateTime(rs.getTimestamp("createTime"));
			temp.setDescription(rs.getString("description"));
			return temp;
		}
	}
}
