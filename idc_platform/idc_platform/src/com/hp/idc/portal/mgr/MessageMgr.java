package com.hp.idc.portal.mgr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.hp.idc.portal.message.Message;
import com.hp.idc.portal.message.MessageManagerImpl;
import com.hp.idc.portal.util.DBUtil;

public class MessageMgr extends MessageManagerImpl {
	/**
	 * 更新操作
	 * @param messages
	 * @return
	 */
	public static int update(final List<Message> messages) {
		String sql = "update tf_pt_message t set t.title=?,t.content=?,t.url=?,t.userid=?,t.status=?,t.module=? where t.oid=?";

		DBUtil.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				int oid = messages.get(i).getOid();
				String title = messages.get(i).getTitle();
				String content = messages.get(i).getContent();
				String url = messages.get(i).getUrl();
				int userId = messages.get(i).getUserId();
				int status = messages.get(i).getStatus();
				String module = messages.get(i).getModule();
				
				ps.setString(1, title);
				ps.setString(2, content);
				ps.setString(3, url);
				ps.setInt(4, userId);
				ps.setInt(5, status);
				ps.setString(6, module);
				ps.setInt(7, oid);
			}

			public int getBatchSize() {
				return messages.size();
			}
		});
		return messages.size();
	}
	
	/**
	 * 更新状态
	 * @param messages
	 * @param status
	 * @return
	 */
	public static int updateStatus(final List<Message> messages,final int status) {
		String sql = "update tf_pt_message t set t.status=? where t.oid=?";

		DBUtil.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				int oid = messages.get(i).getOid();
				
				ps.setInt(1, status);
				ps.setInt(2, oid);
			}

			public int getBatchSize() {
				return messages.size();
			}
		});
		return messages.size();
	}
	
	/**
	 * 删除消除提醒的内部方法
	 * @param oids	消息记录OID集合
	 * @return
	 */
	public static int deleteMessage(final List<String> oids){
		String sql = "delete from tf_pt_message where oid = ?";

		DBUtil.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				String oid = oids.get(i);
				ps.setString(1, oid);
			}
			public int getBatchSize() {
				return oids.size();
			}
		});
		return oids.size();
	}
	
	/**
	 * 根据用户ID查询所有Document
	 * 
	 * @param userId 消息接收人ID
	 * @param status 状态
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Message> getBeanByUserId(int userId,int status) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from tf_pt_message where userid = ? ");
		List params = new ArrayList();
		params.add(userId);
		if(status!=-1){
			sb.append(" and status = ?");
			params.add(status);
		}
		sb.append(" order by oid desc");
		Object[] obj = params.toArray();
		List<Message> list = DBUtil.getJdbcTemplate().query(sb.toString(), obj, new MessageRowMapper());
		return list;
	}
	/**
	 * 根据用户ID查询其未读消息数
	 * 
	 * @param userId 消息接收人ID
	 * @return
	 */
	public static int getUnreadCount(int userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from tf_pt_message where userid = ? and status = 1");
		int ret = DBUtil.getJdbcTemplate().queryForInt(sb.toString(), new Object[]{userId});
		return ret;
	}
	
	/**
	 * 根据用户ID查询其未读消息数
	 * 
	 * @param userId 消息接收人ID
	 * @return
	 */
	public static Message getMessageById(int oid) {
		String sql = "select * from tf_pt_message t where t.oid = ?";
		Message ret = DBUtil.getJdbcTemplate().query(sql, new Object[]{oid},new ResultSetExtractor<Message>() {
			public Message extractData(ResultSet rs) throws SQLException, DataAccessException {
				Message temp = new Message();
				if (rs.next()) {
					temp.setOid(rs.getInt("oid"));
					temp.setTitle(rs.getString("title"));
					temp.setContent(rs.getString("content"));
					temp.setUrl(rs.getString("url"));
					temp.setStatus(rs.getInt("status"));
					temp.setUserId(rs.getInt("userid"));
					temp.setModule(rs.getString("module"));
					return temp;
				}
				return null;
			}
		});
		return ret;
	}
	
	private static class MessageRowMapper implements RowMapper<Message> {
		public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
			Message temp = new Message();
			
			temp.setOid(rs.getInt("oid"));
			temp.setTitle(rs.getString("title"));
			temp.setContent(rs.getString("content"));
			temp.setUrl(rs.getString("url"));
			temp.setStatus(rs.getInt("status"));
			temp.setUserId(rs.getInt("userid"));
			temp.setModule(rs.getString("module"));
			
			return temp;
		}
	}
}
