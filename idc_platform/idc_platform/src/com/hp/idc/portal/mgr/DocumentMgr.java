package com.hp.idc.portal.mgr;

import java.sql.*;
import java.util.*;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;

import com.hp.idc.portal.bean.Document;
import com.hp.idc.portal.util.DBUtil;

public class DocumentMgr {
	private static String SELECT_BY_OID = "SELECT * FROM tf_pt_document WHERE oid = ? order by oid desc";
	private static String INSERT_REGULAR = "insert into tf_pt_document (oid,name,filesize,filetype,filepath,filename,updateTime,role,creator,createTime) values (TF_PT_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?)";
	private static String UPDATE_REGULAR = "update tf_pt_document set name=?,filesize=?,filetype=?,filepath=?,filename=?,updateTime=?,role=?,creator=?,createTime=? where oid = ?";
	private static String DELETE_REGULAR = "delete from tf_pt_document where oid = ?";

	/**
	 * 添加
	 * @param bean
	 * @return
	 */
	public boolean add(final Document bean) {
		boolean isSuccess = false;
		JdbcTemplate jdbcTemplate = DBUtil.getJdbcTemplate();
		int ret = jdbcTemplate.update(INSERT_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, bean.getName());
				ps.setString(2, bean.getFilesize());
				ps.setString(3, bean.getFiletype());
				ps.setString(4, bean.getFilepath());
				ps.setString(5, bean.getFilename());
				ps.setTimestamp(6, new Timestamp(bean.getUpdateTime().getTime()));
				ps.setString(7, bean.getRole());
				ps.setInt(8, bean.getCreator());
				ps.setTimestamp(9, new Timestamp(bean.getCreateTime().getTime()));
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
	public boolean update(final Document bean) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(UPDATE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, bean.getName());
				ps.setString(2, bean.getFilesize());
				ps.setString(3, bean.getFiletype());
				ps.setString(4, bean.getFilepath());
				ps.setString(5, bean.getFilename());
				ps.setTimestamp(6, new Timestamp(bean.getUpdateTime().getTime()));
				ps.setString(7, bean.getRole());
				ps.setInt(8, bean.getCreator());
				ps.setTimestamp(9, new Timestamp(bean.getCreateTime().getTime()));
				ps.setString(10, bean.getOid());
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
	 * 根据oid获取Document
	 * 
	 * @param oid
	 * @return
	 */
	public Document getBeanById(String oid) {
		Document ret = DBUtil.getJdbcTemplate().query(SELECT_BY_OID, new Object[] { oid },
				new ResultSetExtractor<Document>() {
					public Document extractData(ResultSet rs) throws SQLException, DataAccessException {
						Document temp = new Document();
						if (rs.next()) {
							temp.setOid(rs.getString("oid"));
							temp.setName(rs.getString("name"));
							temp.setFilesize(rs.getString("filesize"));
							temp.setFilename(rs.getString("filename"));
							temp.setFilepath(rs.getString("filepath"));
							temp.setFiletype(rs.getString("filetype"));
							temp.setRole(rs.getString("role"));
							temp.setUpdateTime(rs.getTimestamp("updateTime"));
							temp.setCreator(rs.getInt("creator"));
							temp.setCreateTime(rs.getTimestamp("createTime"));
							return temp;
						}
						return null;
					}
				});
		return ret;
	}

	/**
	 * 根据用户ID查询所有Document
	 * 
	 * @param userId
	 * @return
	 */
	public List<Document> getBeanByUserId(int userId) {
		String sql = "SELECT * FROM tf_pt_document WHERE creator = ? or instr(role, ?)>0 order by oid desc";
		List<Document> list = DBUtil.getJdbcTemplate().query(sql, new Object[] { userId,","+userId+"," },
				new DocumentRowMapper());
		return list;
	}
	
	/**
	 * 根据用户ID查询所有Document
	 * 
	 * @param userId 用户名
	 * @param type 文件类型
	 * @param keyWords 关键字
	 * @return
	 */
	public List<Document> getBeanByFilter(int userId,String type,String keyWords) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tf_pt_document t WHERE t.creator = ?");
		if(!"全部".equals(type)){
			sql.append(" and t.filetype='"+type+"'");
		}
		if(!"".equals(keyWords)){
			sql.append(" and instr(t.name,'"+keyWords+"')>0");
		}
		sql.append(" order by t.oid desc");
		List<Document> list = DBUtil.getJdbcTemplate().query(sql.toString(), new Object[] { userId},
				new DocumentRowMapper());
		return list;
	}

	private class DocumentRowMapper implements RowMapper<Document> {
		public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
			Document temp = new Document();
			
			temp.setOid(rs.getString("oid"));
			temp.setName(rs.getString("name"));
//			DecimalFormat df=new DecimalFormat("0.00"); //保留两位小数
//			if(rs.getDouble("filesize")>1024&&rs.getDouble("filesize")<1000*1024){
//				size = df.format((rs.getDouble("filesize")/1024))+"KB";
//			}else if(rs.getDouble("filesize")>1024*1024){
//				size = df.format((rs.getDouble("filesize")/1024/1024))+"MB";
//			}else{
//				size = df.format((rs.getDouble("filesize")))+"B";
//			}
			temp.setFilesize(rs.getString("filesize"));
			temp.setFilename(rs.getString("filename"));
			temp.setFilepath(rs.getString("filepath"));
			temp.setFiletype(rs.getString("filetype"));
			temp.setRole(rs.getString("role"));
			temp.setUpdateTime(rs.getTimestamp("updateTime"));
			temp.setCreator(rs.getInt("creator"));
			temp.setCreateTime(rs.getTimestamp("createTime"));
			return temp;
		}
	}
	
	/**
	 * 根据用户ID查询所有该用户所有文件的文件类型
	 * 
	 * @param userId
	 * @return
	 */
	public List<String> getFileTypeByUserId(int userId) {
		String sql = "select distinct(t.filetype) from tf_pt_document t where t.creator=?";
		List<String> list = DBUtil.getJdbcTemplate().query(sql, new Object[] { userId },
			new TypeRowMapper());
		return list;
	}
	
	private class TypeRowMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("filetype");
		}
	}
}
