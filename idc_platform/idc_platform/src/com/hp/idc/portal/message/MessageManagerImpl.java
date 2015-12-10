package com.hp.idc.portal.message;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class MessageManagerImpl extends JdbcDaoSupport implements IMessageManager {
	
	/**
	 * log4j��־
	 */
	private static Logger logger = Logger.getLogger(MessageManagerImpl.class);

	public int addMessage(final Message message) {
		// ��Ϣ�������ݳ���100������
		if (message.getTitle() != null && message.getTitle().length() > 100) {
			logger.error("��Ϣ���ѱ���̫��(ֻ��Ϊ100)�����飡����");
			return 0;
		}
		// ��Ϣ�������ݳ���4000������
		if (message.getContent() != null && message.getContent().length() > 4000) {
			logger.error("��Ϣ��������̫��(ֻ��Ϊ4000)�����飡����");
			return 0;
		}
		
		String sql = "INSERT INTO TF_PT_MESSAGE VALUES(TF_PT_SEQ.NEXTVAL,?,?,?,?,?,?)";
		int result = getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, message.getTitle());
				ps.setString(2, message.getContent());
				ps.setString(3, message.getUrl());
				ps.setInt(4, message.getUserId());
				ps.setInt(5, message.getStatus());
				ps.setString(6, message.getModule());
			}
		});
		return result;
	}
	
	public int addMessageList(final List<Message> messages) {
		// ������֤
		for (Message message : messages) {
			// ��Ϣ�������ݳ���100������
			if (message.getTitle() != null && message.getTitle().length() > 100) {
				logger.error("��Ϣ���ѱ���̫��(ֻ��Ϊ100)�����飡����");
				return 0;
			}
			// ��Ϣ�������ݳ���4000������
			if (message.getContent() != null && message.getContent().length() > 4000) {
				logger.error("��Ϣ��������̫��(ֻ��Ϊ4000)�����飡����");
				return 0;
			}
		}
		String sql = "INSERT INTO TF_PT_MESSAGE VALUES(TF_PT_SEQ.NEXTVAL,?,?,?,?,?,?)";

		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
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
			}

			public int getBatchSize() {
				return messages.size();
			}
		});
		return messages.size();
	}
}
