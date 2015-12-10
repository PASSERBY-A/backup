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
	 * ���
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
	 * �޸�
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
	 * ɾ��
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
	 * ����oid��ȡDocument
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
	 * �����û�ID��ѯ����Document
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
	 * �����û�ID��ѯ����Document
	 * 
	 * @param userId �û���
	 * @param type �ļ�����
	 * @param keyWords �ؼ���
	 * @return
	 */
	public List<Document> getBeanByFilter(int userId,String type,String keyWords) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tf_pt_document t WHERE t.creator = ?");
		if(!"ȫ��".equals(type)){
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
//			DecimalFormat df=new DecimalFormat("0.00"); //������λС��
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
	 * �����û�ID��ѯ���и��û������ļ����ļ�����
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
